package ua.yatsergray.football.manager.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.yatsergray.football.manager.domain.entity.Player;
import ua.yatsergray.football.manager.domain.entity.Team;
import ua.yatsergray.football.manager.domain.entity.Transfer;
import ua.yatsergray.football.manager.repository.TeamRepository;

import java.util.*;

@Repository("teamRepositoryImpl")
public class TeamRepositoryImpl implements TeamRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TeamRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Team save(Team entity) {
        UUID entityId;

        if (findById(entity.getId()).isPresent()) {
            entityId = entity.getId();

            String updateTeamSqlStatement = """
                    UPDATE teams tm
                    SET name = ?, commission_percentage = ?, bank_account_balance = ?
                    WHERE tm.id = ?
                    """;

            jdbcTemplate.update(updateTeamSqlStatement, entity.getName(), entity.getCommissionPercentage(), entity.getBankAccountBalance(), entityId);
        } else {
            entityId = UUID.randomUUID();

            String insertTeamSqlStatement = """
                    INSERT INTO teams (id, name, commission_percentage, bank_account_balance)
                    VALUES (?, ?, ?, ?)
                    """;

            jdbcTemplate.update(insertTeamSqlStatement, entityId, entity.getName(), entity.getCommissionPercentage(), entity.getBankAccountBalance());
        }

        return findById(entityId)
                .orElseThrow();
    }

    @Override
    public Optional<Team> findById(UUID entityId) {
        Map<UUID, Team> teamsMap = new HashMap<>();
        Map<UUID, Player> playersMap = new HashMap<>();
        Map<UUID, Transfer> transfersMap = new HashMap<>();

        String selectTeamSqlStatement = """
                SELECT tm.id AS team_id,
                       tm.name AS team_name,
                       tm.bank_account_balance AS team_bank_account_balance,
                       tm.commission_percentage AS team_commission_percentage,
                       p.id AS player_id,
                       p.first_name AS player_first_name,
                       p.last_name AS player_last_name,
                       p.age AS player_age,
                       p.months_of_experience AS player_months_of_experience,
                       tr.id AS transfer_id,
                       tr.total_cost AS transfer_total_cost,
                       tr.date AS transfer_date,
                       stm.id AS selling_team_id,
                       stm.name AS selling_team_name,
                       stm.commission_percentage AS selling_team_commission_percentage,
                       stm.bank_account_balance AS selling_team_bank_account_balance,
                       btm.id AS buying_team_id,
                       btm.name AS buying_team_name,
                       btm.commission_percentage AS buying_team_commission_percentage,
                       btm.bank_account_balance AS buying_team_bank_account_balance
                FROM teams tm
                         LEFT JOIN players p ON p.team_id = tm.id
                         LEFT JOIN transfers tr ON tr.player_id = p.id
                         LEFT JOIN teams stm ON stm.id = tr.selling_team_id
                         LEFT JOIN teams btm ON btm.id = tr.buying_team_id
                WHERE tm.id = ?
                """;

        jdbcTemplate.query(selectTeamSqlStatement, (rs, rowNum) -> {
            UUID teamId = UUID.fromString(rs.getString("team_id"));

            if (!teamsMap.containsKey(teamId)) {
                teamsMap.put(teamId, Team.builder()
                        .id(teamId)
                        .name(rs.getString("team_name"))
                        .commissionPercentage(rs.getInt("team_commission_percentage"))
                        .bankAccountBalance(rs.getBigDecimal("team_bank_account_balance"))
                        .build());
            }

            UUID playerId = rs.getString("player_id") != null ? UUID.fromString(rs.getString("player_id")) : null;
            if (playerId != null && !playersMap.containsKey(playerId)) {
                playersMap.put(playerId, Player.builder()
                        .id(playerId)
                        .firstName(rs.getString("player_first_name"))
                        .lastName(rs.getString("player_last_name"))
                        .age(rs.getInt("player_age"))
                        .monthsOfExperience(rs.getInt("player_months_of_experience"))
                        .team(teamsMap.get(teamId))
                        .build());
            }

            UUID transferId = rs.getString("transfer_id") != null ? UUID.fromString(rs.getString("transfer_id")) : null;
            if (transferId != null && !transfersMap.containsKey(transferId)) {
                transfersMap.put(transferId, Transfer.builder()
                        .id(transferId)
                        .totalCost(rs.getBigDecimal("transfer_total_cost"))
                        .date(rs.getDate("transfer_date") != null ? rs.getDate("transfer_date").toLocalDate() : null)
                        .player(playersMap.get(playerId))
                        .sellingTeam(rs.getString("selling_team_id") != null ? Team.builder()
                                .id(UUID.fromString(rs.getString("selling_team_id")))
                                .name(rs.getString("selling_team_name"))
                                .commissionPercentage(rs.getInt("selling_team_commission_percentage"))
                                .bankAccountBalance(rs.getBigDecimal("selling_team_bank_account_balance"))
                                .build() : null)
                        .buyingTeam(rs.getString("buying_team_id") != null ? Team.builder()
                                .id(UUID.fromString(rs.getString("buying_team_id")))
                                .name(rs.getString("buying_team_name"))
                                .commissionPercentage(rs.getInt("buying_team_commission_percentage"))
                                .bankAccountBalance(rs.getBigDecimal("buying_team_bank_account_balance"))
                                .build() : null)
                        .build());
            }

            return null;
        }, entityId);

        playersMap.values().forEach(player -> {
            Team team = teamsMap.get(player.getTeam().getId());
            if (team != null && !team.getPlayers().contains(player)) {
                team.getPlayers().add(player);
                player.setTeam(null);
            }
        });

        transfersMap.values().forEach(transfer -> {
            if (transfer.getPlayer() != null) {
                Player player = playersMap.get(transfer.getPlayer().getId());
                if (player != null && !player.getTransfers().contains(transfer)) {
                    player.getTransfers().add(transfer);
                }
            }

            if (transfer.getSellingTeam() != null) {
                Team sellingTeam = teamsMap.get(transfer.getSellingTeam().getId());
                if (sellingTeam != null && !sellingTeam.getSellingTransfers().contains(transfer)) {
                    sellingTeam.getSellingTransfers().add(transfer);
                }
            }

            if (transfer.getBuyingTeam() != null) {
                Team buyingTeam = teamsMap.get(transfer.getBuyingTeam().getId());
                if (buyingTeam != null && !buyingTeam.getBuyingTransfers().contains(transfer)) {
                    buyingTeam.getBuyingTransfers().add(transfer);
                }
            }
        });

        return teamsMap.values().stream().findFirst();
    }

    @Override
    public List<Team> findAll() {
        Map<UUID, Team> teamsMap = new HashMap<>();
        Map<UUID, Player> playersMap = new HashMap<>();
        Map<UUID, Transfer> transfersMap = new HashMap<>();

        String selectAllTeamsSqlStatement = """
                SELECT tm.id AS team_id,
                       tm.name AS team_name,
                       tm.bank_account_balance AS team_bank_account_balance,
                       tm.commission_percentage AS team_commission_percentage,
                       p.id AS player_id,
                       p.first_name AS player_first_name,
                       p.last_name AS player_last_name,
                       p.age AS player_age,
                       p.months_of_experience AS player_months_of_experience,
                       tr.id AS transfer_id,
                       tr.total_cost AS transfer_total_cost,
                       tr.date AS transfer_date,
                       stm.id AS selling_team_id,
                       stm.name AS selling_team_name,
                       stm.commission_percentage AS selling_team_commission_percentage,
                       stm.bank_account_balance AS selling_team_bank_account_balance,
                       btm.id AS buying_team_id,
                       btm.name AS buying_team_name,
                       btm.commission_percentage AS buying_team_commission_percentage,
                       btm.bank_account_balance AS buying_team_bank_account_balance
                FROM teams tm
                         LEFT JOIN players p ON p.team_id = tm.id
                         LEFT JOIN transfers tr ON tr.player_id = p.id
                         LEFT JOIN teams stm ON stm.id = tr.selling_team_id
                         LEFT JOIN teams btm ON btm.id = tr.buying_team_id
                """;

        jdbcTemplate.query(selectAllTeamsSqlStatement, (rs, rowNum) -> {
            UUID teamId = UUID.fromString(rs.getString("team_id"));

            if (!teamsMap.containsKey(teamId)) {
                teamsMap.put(teamId, Team.builder()
                        .id(teamId)
                        .name(rs.getString("team_name"))
                        .commissionPercentage(rs.getInt("team_commission_percentage"))
                        .bankAccountBalance(rs.getBigDecimal("team_bank_account_balance"))
                        .build());
            }

            UUID playerId = rs.getString("player_id") != null ? UUID.fromString(rs.getString("player_id")) : null;

            if (playerId != null && !playersMap.containsKey(playerId)) {
                playersMap.put(playerId, Player.builder()
                        .id(playerId)
                        .firstName(rs.getString("player_first_name"))
                        .lastName(rs.getString("player_last_name"))
                        .age(rs.getInt("player_age"))
                        .monthsOfExperience(rs.getInt("player_months_of_experience"))
                        .team(teamsMap.get(teamId))
                        .build());
            }

            UUID transferId = rs.getString("transfer_id") != null ? UUID.fromString(rs.getString("transfer_id")) : null;

            if (transferId != null && !transfersMap.containsKey(transferId)) {
                transfersMap.put(transferId, Transfer.builder()
                        .id(transferId)
                        .totalCost(rs.getBigDecimal("transfer_total_cost"))
                        .date(rs.getDate("transfer_date") != null ? rs.getDate("transfer_date").toLocalDate() : null)
                        .player(playersMap.get(playerId))
                        .sellingTeam(rs.getString("selling_team_id") != null ? Team.builder()
                                .id(UUID.fromString(rs.getString("selling_team_id")))
                                .name(rs.getString("selling_team_name"))
                                .commissionPercentage(rs.getInt("selling_team_commission_percentage"))
                                .bankAccountBalance(rs.getBigDecimal("selling_team_bank_account_balance"))
                                .build() : null)
                        .buyingTeam(rs.getString("buying_team_id") != null ? Team.builder()
                                .id(UUID.fromString(rs.getString("buying_team_id")))
                                .name(rs.getString("buying_team_name"))
                                .commissionPercentage(rs.getInt("buying_team_commission_percentage"))
                                .bankAccountBalance(rs.getBigDecimal("buying_team_bank_account_balance"))
                                .build() : null)
                        .build());
            }

            return null;
        });

        playersMap.values().forEach(player -> {
            Team team = teamsMap.get(player.getTeam().getId());
            if (team != null && !team.getPlayers().contains(player)) {
                team.getPlayers().add(player);
                player.setTeam(null);
            }
        });

        transfersMap.values().forEach(transfer -> {
            if (transfer.getPlayer() != null) {
                Player player = playersMap.get(transfer.getPlayer().getId());
                if (player != null && !player.getTransfers().contains(transfer)) {
                    player.getTransfers().add(transfer);
                }
            }

            if (transfer.getSellingTeam() != null) {
                Team sellingTeam = teamsMap.get(transfer.getSellingTeam().getId());
                if (sellingTeam != null && !sellingTeam.getSellingTransfers().contains(transfer)) {
                    sellingTeam.getSellingTransfers().add(transfer);
                }
            }

            if (transfer.getBuyingTeam() != null) {
                Team buyingTeam = teamsMap.get(transfer.getBuyingTeam().getId());
                if (buyingTeam != null && !buyingTeam.getBuyingTransfers().contains(transfer)) {
                    buyingTeam.getBuyingTransfers().add(transfer);
                }
            }
        });

        return teamsMap.values().stream().toList();
    }

    @Override
    public void deleteById(UUID entityId) {
        String deleteTeamSqlStatement = """
                DELETE
                FROM teams
                WHERE id = ?
                """;

        jdbcTemplate.update(deleteTeamSqlStatement, entityId);
    }

    @Override
    public boolean existsByName(String teamName) {
        String countTeamSqlStatement = """
                SELECT COUNT(*)
                FROM teams t
                WHERE t.name = ?
                """;

        Integer count = jdbcTemplate.queryForObject(countTeamSqlStatement, Integer.class, teamName);

        return count != null && count > 0;
    }

    @Override
    public boolean existsById(UUID teamId) {
        String countTeamSqlStatement = """
                SELECT COUNT(*)
                FROM teams t
                WHERE t.id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(countTeamSqlStatement, Integer.class, teamId);

        return count != null && count > 0;
    }
}
