package ua.yatsergray.football.manager.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.yatsergray.football.manager.domain.entity.Player;
import ua.yatsergray.football.manager.domain.entity.Team;
import ua.yatsergray.football.manager.domain.entity.Transfer;
import ua.yatsergray.football.manager.repository.PlayerRepository;

import java.util.*;

@Repository("playerRepositoryImpl")
public class PlayerRepositoryImpl implements PlayerRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlayerRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Player save(Player entity) {
        UUID entityId;

        if (findById(entity.getId()).isPresent()) {
            entityId = entity.getId();

            String updatePlayerSqlStatement = """
                    UPDATE players p
                    SET first_name = ?, last_name = ?, age = ?, months_of_experience = ?, team_id = ?
                    WHERE p.id = ?
                    """;

            jdbcTemplate.update(updatePlayerSqlStatement, entity.getFirstName(), entity.getLastName(), entity.getAge(), entity.getMonthsOfExperience(), entity.getTeam().getId(), entityId);
        } else {
            entityId = UUID.randomUUID();

            String insertPlayerSqlStatement = """
                    INSERT INTO players (id, first_name, last_name, age, months_of_experience, team_id)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """;

            jdbcTemplate.update(insertPlayerSqlStatement, entityId, entity.getFirstName(), entity.getLastName(), entity.getAge(), entity.getMonthsOfExperience(), entity.getTeam().getId());
        }

        return findById(entityId)
                .orElseThrow();
    }

    @Override
    public Optional<Player> findById(UUID entityId) {
        String selectPlayerSqlStatement = """
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
            WHERE p.id = ?
            """;

        Map<UUID, Player> playersMap = new LinkedHashMap<>();
        Map<UUID, Transfer> transfersMap = new LinkedHashMap<>();

        jdbcTemplate.query(selectPlayerSqlStatement, (rs, rowNum) -> {
            UUID playerId = rs.getString("player_id") != null ? UUID.fromString(rs.getString("player_id")) : null;

            if (playerId != null && !playersMap.containsKey(playerId)) {
                playersMap.put(playerId, Player.builder()
                        .id(playerId)
                        .firstName(rs.getString("player_first_name"))
                        .lastName(rs.getString("player_last_name"))
                        .age(rs.getInt("player_age"))
                        .monthsOfExperience(rs.getInt("player_months_of_experience"))
                        .team(Team.builder()
                                .id(UUID.fromString(rs.getString("team_id")))
                                .name(rs.getString("team_name"))
                                .commissionPercentage(rs.getInt("team_commission_percentage"))
                                .bankAccountBalance(rs.getBigDecimal("team_bank_account_balance"))
                                .build())
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

        transfersMap.values().forEach(transfer -> {
            Player player = playersMap.get(transfer.getPlayer().getId());
            if (player != null) {
                player.getTransfers().add(transfer);
            }
        });

        return playersMap.values().stream().findFirst();
    }

    @Override
    public List<Player> findAll() {
        String selectTeamPlayersSqlStatement = """
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

        Map<UUID, Player> playersMap = new LinkedHashMap<>();
        Map<UUID, Transfer> transfersMap = new LinkedHashMap<>();

        jdbcTemplate.query(selectTeamPlayersSqlStatement, (rs, rowNum) -> {
            UUID playerId = rs.getString("player_id") != null ? UUID.fromString(rs.getString("player_id")) : null;

            if (playerId != null && !playersMap.containsKey(playerId)) {
                playersMap.put(playerId, Player.builder()
                        .id(playerId)
                        .firstName(rs.getString("player_first_name"))
                        .lastName(rs.getString("player_last_name"))
                        .age(rs.getInt("player_age"))
                        .monthsOfExperience(rs.getInt("player_months_of_experience"))
                        .team(Team.builder()
                                .id(UUID.fromString(rs.getString("team_id")))
                                .name(rs.getString("team_name"))
                                .commissionPercentage(rs.getInt("team_commission_percentage"))
                                .bankAccountBalance(rs.getBigDecimal("team_bank_account_balance"))
                                .build())
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

        transfersMap.values().forEach(transfer -> {
            Player player = playersMap.get(transfer.getPlayer().getId());
            if (player != null) {
                player.getTransfers().add(transfer);
            }
        });

        return playersMap.values().stream().toList();
    }

    @Override
    public void deleteById(UUID entityId) {
        String deletePlayerSqlStatement = """
                DELETE FROM players
                WHERE id = ?
                """;

        jdbcTemplate.update(deletePlayerSqlStatement, entityId);
    }

    @Override
    public boolean existsById(UUID playerId) {
        String countTeamSqlStatement = """
                SELECT COUNT(*)
                FROM players p
                WHERE p.id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(countTeamSqlStatement, Integer.class, playerId);

        return count != null && count > 0;
    }
}
