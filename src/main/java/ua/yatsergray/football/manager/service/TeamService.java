package ua.yatsergray.football.manager.service;

import ua.yatsergray.football.manager.domain.dto.TeamDTO;
import ua.yatsergray.football.manager.domain.request.TeamCreateUpdateRequest;
import ua.yatsergray.football.manager.exception.NoSuchTeamException;
import ua.yatsergray.football.manager.exception.TeamAlreadyExistsException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamService {

    TeamDTO addTeam(TeamCreateUpdateRequest teamCreateUpdateRequest) throws TeamAlreadyExistsException;

    Optional<TeamDTO> getTeamById(UUID teamId);

    List<TeamDTO> getAllTeams();

    TeamDTO modifyTeamById(UUID teamId, TeamCreateUpdateRequest teamCreateUpdateRequest) throws NoSuchTeamException, TeamAlreadyExistsException;

    void removeTeamById(UUID teamId) throws NoSuchTeamException;
}
