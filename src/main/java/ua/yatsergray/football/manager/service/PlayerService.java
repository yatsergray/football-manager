package ua.yatsergray.football.manager.service;

import ua.yatsergray.football.manager.domain.dto.PlayerDTO;
import ua.yatsergray.football.manager.domain.request.PlayerCreateRequest;
import ua.yatsergray.football.manager.domain.request.PlayerUpdateRequest;
import ua.yatsergray.football.manager.exception.NoSuchPlayerException;
import ua.yatsergray.football.manager.exception.NoSuchTeamException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerService {

    PlayerDTO addPlayer(PlayerCreateRequest playerCreateRequest) throws NoSuchTeamException;

    Optional<PlayerDTO> getPlayerById(UUID playerId);

    List<PlayerDTO> getAllPlayers();

    PlayerDTO modifyPlayerById(UUID playerId, PlayerUpdateRequest playerUpdateRequest) throws NoSuchPlayerException;

    void removePlayerById(UUID playerId) throws NoSuchPlayerException;
}
