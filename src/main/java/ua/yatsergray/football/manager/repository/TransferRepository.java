package ua.yatsergray.football.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.yatsergray.football.manager.domain.entity.Transfer;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    @Query(value = """
            SELECT t.id, t.total_cost, t.date, t.player_id, t.selling_team_id, t.buying_team_id
            FROM transfers t
            WHERE t.player_id = :playerId AND t.selling_team_id IS NULL AND t.buying_team_id IS NULL
            """, nativeQuery = true)
    List<Transfer> findAvailableToDeleteByPlayerId(@Param("playerId") UUID playerId);

    @Query(value = """
            SELECT t.id, t.total_cost, t.date, t.player_id, t.selling_team_id, t.buying_team_id
            FROM transfers t
            WHERE (t.selling_team_id = :teamId AND t.buying_team_id IS NULL AND t.player_id IS NULL)
               OR (t.selling_team_id IS NULL AND t.buying_team_id = :teamId AND t.player_id IS NULL)
            """, nativeQuery = true)
    List<Transfer> findAvailableToDeleteByTeamId(@Param("teamId") UUID teamId);
}
