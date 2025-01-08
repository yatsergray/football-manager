package ua.yatsergray.football.manager.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.yatsergray.football.manager.domain.entity.Player;
import ua.yatsergray.football.manager.domain.entity.Team;
import ua.yatsergray.football.manager.domain.entity.Transfer;
import ua.yatsergray.football.manager.repository.TransferRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("transferRepositoryImpl")
public class TransferRepositoryImpl implements TransferRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TransferRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer save(Transfer entity) {
        UUID entityId;
        if (findById(entity.getId()).isPresent()) {
            entityId = entity.getId();

            String updateTransferSqlStatement = """
                    UPDATE transfers
                    SET total_cost = ?, date = ?, player_id = ?, selling_team_id = ?, buying_team_id = ?
                    WHERE id = ?
                    """;

            jdbcTemplate.update(updateTransferSqlStatement, entity.getTotalCost(), entity.getDate(), entity.getPlayer().getId(), entity.getSellingTeam().getId(), entity.getBuyingTeam().getId(), entityId);
        } else {
            entityId = UUID.randomUUID();

            String insertTransferSqlStatement = """
                    INSERT INTO transfers (id, total_cost, date, player_id, selling_team_id, buying_team_id)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """;

            jdbcTemplate.update(insertTransferSqlStatement, entityId, entity.getTotalCost(), entity.getDate(), entity.getPlayer().getId(), entity.getSellingTeam().getId(), entity.getBuyingTeam().getId());
        }
        return findById(entityId)
                .orElseThrow();
    }

    @Override
    public Optional<Transfer> findById(UUID entityId) {
        String selectTransferSqlStatement = """
                SELECT tr.id AS transfer_id, 
                       tr.total_cost AS transfer_total_cost, 
                       tr.date AS transfer_date,
                       p.id AS player_id, 
                       p.first_name AS player_first_name, 
                       p.last_name AS player_last_name, 
                       p.age AS player_age, 
                       p.months_of_experience AS player_months_of_experience,
                       tm.id AS player_team_id, 
                       tm.name AS player_team_name, 
                       tm.bank_account_balance AS player_team_bank_account_balance, 
                       tm.commission_percentage AS player_team_commission_percentage,
                       stm.id AS selling_team_id, 
                       stm.name AS selling_team_name, 
                       stm.bank_account_balance AS selling_team_bank_account_balance, 
                       stm.commission_percentage AS selling_team_commission_percentage,
                       btm.id AS buying_team_id, 
                       btm.name AS buying_team_name, 
                       btm.bank_account_balance AS buying_team_bank_account_balance, 
                       btm.commission_percentage AS buying_team_commission_percentage
                FROM transfers tr
                         JOIN players p ON tr.player_id = p.id
                         JOIN teams tm ON p.team_id = tm.id
                         JOIN teams stm ON tr.selling_team_id = stm.id
                         JOIN teams btm ON tr.buying_team_id = btm.id
                WHERE tr.id = ?
                """;

        return jdbcTemplate.query(selectTransferSqlStatement, rs -> {
            if (!rs.next()) {
                return Optional.empty();
            }

            Transfer transfer = Transfer.builder()
                    .id(UUID.fromString(rs.getString("transfer_id")))
                    .totalCost(rs.getBigDecimal("transfer_total_cost"))
                    .date(rs.getDate("transfer_date") != null ? rs.getDate("transfer_date").toLocalDate() : null)
                    .player(Player.builder()
                            .id(UUID.fromString(rs.getString("player_id")))
                            .firstName(rs.getString("player_first_name"))
                            .lastName(rs.getString("player_last_name"))
                            .age(rs.getInt("player_age"))
                            .monthsOfExperience(rs.getInt("player_months_of_experience"))
                            .team(Team.builder()
                                    .id(UUID.fromString(rs.getString("player_team_id")))
                                    .name(rs.getString("player_team_name"))
                                    .commissionPercentage(rs.getInt("player_team_commission_percentage"))
                                    .bankAccountBalance(rs.getBigDecimal("player_team_bank_account_balance"))
                                    .build())
                            .build())
                    .sellingTeam(Team.builder()
                            .id(UUID.fromString(rs.getString("selling_team_id")))
                            .name(rs.getString("selling_team_name"))
                            .commissionPercentage(rs.getInt("selling_team_commission_percentage"))
                            .bankAccountBalance(rs.getBigDecimal("selling_team_bank_account_balance"))
                            .build())
                    .buyingTeam(Team.builder()
                            .id(UUID.fromString(rs.getString("buying_team_id")))
                            .name(rs.getString("buying_team_name"))
                            .commissionPercentage(rs.getInt("buying_team_commission_percentage"))
                            .bankAccountBalance(rs.getBigDecimal("buying_team_bank_account_balance"))
                            .build())
                    .build();

            return Optional.of(transfer);
        }, entityId);
    }

    @Override
    public List<Transfer> findAll() {
        String selectAllTransfersSqlStatement = """
                SELECT tr.id AS transfer_id,
                       tr.total_cost AS transfer_total_cost,
                       tr.date AS transfer_date,
                       p.id AS player_id, 
                       p.first_name AS player_first_name,
                       p.last_name AS player_last_name, 
                       p.age AS player_age, 
                       p.months_of_experience AS player_months_of_experience,
                       tm.id AS player_team_id, 
                       tm.name AS player_team_name, 
                       tm.bank_account_balance AS player_team_bank_account_balance, 
                       tm.commission_percentage AS player_team_commission_percentage,
                       stm.id AS selling_team_id, 
                       stm.name AS selling_team_name, 
                       stm.bank_account_balance AS selling_team_bank_account_balance, 
                       stm.commission_percentage AS selling_team_commission_percentage,
                       btm.id AS buying_team_id, 
                       btm.name AS buying_team_name, 
                       btm.bank_account_balance AS buying_team_bank_account_balance, 
                       btm.commission_percentage AS buying_team_commission_percentage
                FROM transfers tr
                         JOIN players p ON tr.player_id = p.id
                         JOIN teams tm ON p.team_id = tm.id
                         JOIN teams stm ON tr.selling_team_id = stm.id
                         JOIN teams btm ON tr.buying_team_id = btm.id
                """;

        return jdbcTemplate.query(selectAllTransfersSqlStatement, (rs, rowNum) ->
                Transfer.builder()
                        .id(UUID.fromString(rs.getString("transfer_id")))
                        .totalCost(rs.getBigDecimal("transfer_total_cost"))
                        .date(rs.getDate("transfer_date") != null ? rs.getDate("transfer_date").toLocalDate() : null)
                        .player(Player.builder()
                                .id(UUID.fromString(rs.getString("player_id")))
                                .firstName(rs.getString("player_first_name"))
                                .lastName(rs.getString("player_last_name"))
                                .age(rs.getInt("player_age"))
                                .monthsOfExperience(rs.getInt("player_months_of_experience"))
                                .team(Team.builder()
                                        .id(UUID.fromString(rs.getString("player_team_id")))
                                        .name(rs.getString("player_team_name"))
                                        .commissionPercentage(rs.getInt("player_team_commission_percentage"))
                                        .bankAccountBalance(rs.getBigDecimal("player_team_bank_account_balance"))
                                        .build())
                                .build())
                        .sellingTeam(Team.builder()
                                .id(UUID.fromString(rs.getString("selling_team_id")))
                                .name(rs.getString("selling_team_name"))
                                .commissionPercentage(rs.getInt("selling_team_commission_percentage"))
                                .bankAccountBalance(rs.getBigDecimal("selling_team_bank_account_balance"))
                                .build())
                        .buyingTeam(Team.builder()
                                .id(UUID.fromString(rs.getString("buying_team_id")))
                                .name(rs.getString("buying_team_name"))
                                .commissionPercentage(rs.getInt("buying_team_commission_percentage"))
                                .bankAccountBalance(rs.getBigDecimal("buying_team_bank_account_balance"))
                                .build())
                        .build());
    }

    @Override
    public void deleteById(UUID entityId) {
        String deleteTransferSqlStatement = """
                DELETE
                FROM transfers
                WHERE id = ?
                """;

        jdbcTemplate.update(deleteTransferSqlStatement, entityId);
    }

    @Override
    public void deleteAllAvailableToDeleteByPlayerId(UUID playerId) {
        String deleteAvailableToDeletePlayerTransfersSqlStatement = """
                DELETE
                FROM transfers t
                WHERE t.player_id = :playerId AND t.selling_team_id IS NULL AND t.buying_team_id IS NULL
                """;

        jdbcTemplate.update(deleteAvailableToDeletePlayerTransfersSqlStatement, playerId);
    }

    @Override
    public void deleteAllAvailableToDeleteByTeamId(UUID teamId) {
        String deleteAvailableToDeleteTeamTransfersSqlStatement = """
                DELETE
                FROM transfers t
                WHERE (t.selling_team_id = ? AND t.buying_team_id IS NULL AND t.player_id IS NULL)
                   OR (t.selling_team_id IS NULL AND t.buying_team_id = ? AND t.player_id IS NULL)
                """;

        jdbcTemplate.update(deleteAvailableToDeleteTeamTransfersSqlStatement, teamId);
    }
}
