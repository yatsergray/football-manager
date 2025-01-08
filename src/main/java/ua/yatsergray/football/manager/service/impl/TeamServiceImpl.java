package ua.yatsergray.football.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ua.yatsergray.football.manager.domain.dto.TeamDTO;
import ua.yatsergray.football.manager.domain.entity.Team;
import ua.yatsergray.football.manager.domain.request.TeamCreateUpdateRequest;
import ua.yatsergray.football.manager.exception.NoSuchTeamException;
import ua.yatsergray.football.manager.exception.TeamAlreadyExistsException;
import ua.yatsergray.football.manager.mapper.TeamMapper;
import ua.yatsergray.football.manager.repository.TeamRepository;
import ua.yatsergray.football.manager.repository.impl.TransferRepositoryImpl;
import ua.yatsergray.football.manager.service.TeamService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final TransferRepositoryImpl transferRepository;
    private final TeamMapper teamMapper;

    @Autowired
    public TeamServiceImpl(@Qualifier("teamRepositoryImpl") TeamRepository teamRepository, @Qualifier("transferRepositoryImpl") TransferRepositoryImpl transferRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.transferRepository = transferRepository;
        this.teamMapper = teamMapper;
    }

    @Override
    public TeamDTO addTeam(TeamCreateUpdateRequest teamCreateUpdateRequest) throws TeamAlreadyExistsException {
        if (teamRepository.existsByName(teamCreateUpdateRequest.getName())) {
            throw new TeamAlreadyExistsException(String.format("Team with name=\"%s\" already exists", teamCreateUpdateRequest.getName()));
        }

        Team team = Team.builder()
                .name(teamCreateUpdateRequest.getName())
                .commissionPercentage(teamCreateUpdateRequest.getCommissionPercentage())
                .bankAccountBalance(teamCreateUpdateRequest.getBankAccountBalance())
                .build();

        return teamMapper.mapToTeamDTO(teamRepository.save(team));
    }

    @Override
    public Optional<TeamDTO> getTeamById(UUID teamId) {
        return teamRepository.findById(teamId).map(teamMapper::mapToTeamDTO);
    }

    @Override
    public List<TeamDTO> getAllTeams() {
        return teamMapper.mapAllToTeamDTOList(teamRepository.findAll());
    }

    @Override
    public TeamDTO modifyTeamById(UUID teamId, TeamCreateUpdateRequest teamCreateUpdateRequest) throws NoSuchTeamException, TeamAlreadyExistsException {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchTeamException(String.format("Team with id=\"%s\" does not exist", teamId)));

        if (!teamCreateUpdateRequest.getName().equals(team.getName()) && teamRepository.existsByName(teamCreateUpdateRequest.getName())) {
            throw new TeamAlreadyExistsException(String.format("Team with name=\"%s\" already exists", teamCreateUpdateRequest.getName()));
        }

        team.setName(teamCreateUpdateRequest.getName());
        team.setCommissionPercentage(teamCreateUpdateRequest.getCommissionPercentage());
        team.setBankAccountBalance(teamCreateUpdateRequest.getBankAccountBalance());

        return teamMapper.mapToTeamDTO(teamRepository.save(team));
    }

    @Override
    public void removeTeamById(UUID teamId) throws NoSuchTeamException {
        if (!teamRepository.existsById(teamId)) {
            throw new NoSuchTeamException(String.format("Team with id=\"%s\" does not exist", teamId));
        }

        transferRepository.deleteAllAvailableToDeleteByTeamId(teamId);
        teamRepository.deleteById(teamId);
    }
}
