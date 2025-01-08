package ua.yatsergray.football.manager.repository;

import ua.yatsergray.football.manager.domain.entity.Team;

import java.util.UUID;

public interface TeamRepository extends CrudRepository<Team> {

    boolean existsByName(String teamName);

    boolean existsById(UUID teamId);
}
