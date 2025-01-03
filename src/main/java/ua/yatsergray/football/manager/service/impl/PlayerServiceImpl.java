package ua.yatsergray.football.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.yatsergray.football.manager.domain.dto.PlayerDTO;
import ua.yatsergray.football.manager.domain.entity.Player;
import ua.yatsergray.football.manager.domain.entity.Team;
import ua.yatsergray.football.manager.domain.request.PlayerCreateRequest;
import ua.yatsergray.football.manager.domain.request.PlayerUpdateRequest;
import ua.yatsergray.football.manager.exception.NoSuchPlayerException;
import ua.yatsergray.football.manager.exception.NoSuchTeamException;
import ua.yatsergray.football.manager.mapper.PlayerMapper;
import ua.yatsergray.football.manager.repository.PlayerRepository;
import ua.yatsergray.football.manager.repository.TeamRepository;
import ua.yatsergray.football.manager.service.PlayerService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerMapper playerMapper;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public PlayerServiceImpl(PlayerMapper playerMapper, PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerMapper = playerMapper;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public PlayerDTO addPlayer(PlayerCreateRequest playerCreateRequest) throws NoSuchTeamException {
        Team team = teamRepository.findById(playerCreateRequest.getTeamId())
                .orElseThrow(() -> new NoSuchTeamException(String.format("Team with id=\"%s\" does not exist", playerCreateRequest.getTeamId())));

        Player player = Player.builder()
                .firstName(playerCreateRequest.getFirstName())
                .lastName(playerCreateRequest.getLastName())
                .age(playerCreateRequest.getAge())
                .monthsOfExperience(playerCreateRequest.getMonthsOfExperience())
                .team(team)
                .build();

        return playerMapper.mapToPlayerDTO(playerRepository.save(player));
    }

    @Override
    public Optional<PlayerDTO> getPlayerById(UUID playerId) {
        return playerRepository.findById(playerId).map(playerMapper::mapToPlayerDTO);
    }

    @Override
    public List<PlayerDTO> getAllPlayers() {
        return playerMapper.mapAllToPlayerDTOList(playerRepository.findAll());
    }

    @Override
    public PlayerDTO modifyPlayerById(UUID playerId, PlayerUpdateRequest playerUpdateRequest) throws NoSuchPlayerException {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NoSuchPlayerException(String.format("Player with id=\"%s\" does not exist", playerId)));

        player.setFirstName(playerUpdateRequest.getFirstName());
        player.setLastName(playerUpdateRequest.getLastName());
        player.setAge(playerUpdateRequest.getAge());
        player.setMonthsOfExperience(playerUpdateRequest.getMonthsOfExperience());

        return playerMapper.mapToPlayerDTO(playerRepository.save(player));
    }

    @Override
    public void removePlayerById(UUID playerId) throws NoSuchPlayerException {
        if (!playerRepository.existsById(playerId)) {
            throw new NoSuchPlayerException(String.format("Player with id=\"%s\" does not exist", playerId));
        }

        playerRepository.deleteById(playerId);
    }
}
