package ua.yatsergray.football.manager.repository;

import ua.yatsergray.football.manager.domain.entity.Transfer;

import java.util.UUID;

public interface TransferRepository extends CrudRepository<Transfer> {

    void deleteAllAvailableToDeleteByPlayerId(UUID playerId);

    void deleteAllAvailableToDeleteByTeamId(UUID teamId);
}
