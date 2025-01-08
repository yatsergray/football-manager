package ua.yatsergray.football.manager.repository;

import ua.yatsergray.football.manager.domain.entity.Player;

import java.util.UUID;

public interface PlayerRepository extends CrudRepository<Player> {

    boolean existsById(UUID playerId);
}
