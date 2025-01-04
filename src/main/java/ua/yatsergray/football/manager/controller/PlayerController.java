package ua.yatsergray.football.manager.controller;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.yatsergray.football.manager.domain.dto.PlayerDTO;
import ua.yatsergray.football.manager.domain.dto.TransferDTO;
import ua.yatsergray.football.manager.domain.request.PlayerCreateRequest;
import ua.yatsergray.football.manager.domain.request.PlayerUpdateRequest;
import ua.yatsergray.football.manager.domain.request.TransferCreateRequest;
import ua.yatsergray.football.manager.service.impl.PlayerServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {
    private final PlayerServiceImpl playerService;

    @Autowired
    public PlayerController(PlayerServiceImpl playerService) {
        this.playerService = playerService;
    }

    @SneakyThrows
    @PostMapping
    public ResponseEntity<PlayerDTO> createPlayer(@Valid @RequestBody PlayerCreateRequest playerCreateRequest) {
        return ResponseEntity.ok(playerService.addPlayer(playerCreateRequest));
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerDTO> readPlayerById(@PathVariable("playerId") UUID playerId) {
        return playerService.getPlayerById(playerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> readAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @SneakyThrows
    @PutMapping("/{playerId}")
    public ResponseEntity<PlayerDTO> updatePlayerById(@PathVariable("playerId") UUID playerId, @Valid @RequestBody PlayerUpdateRequest playerUpdateRequest) {
        return ResponseEntity.ok(playerService.modifyPlayerById(playerId, playerUpdateRequest));
    }

    @SneakyThrows
    @DeleteMapping("/{playerId}")
    public ResponseEntity<Void> deletePlayerById(@PathVariable("playerId") UUID playerId) {
        playerService.removePlayerById(playerId);

        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/{playerId}/transfer")
    public ResponseEntity<TransferDTO> transferPlayerById(@PathVariable("playerId") UUID playerId, @Valid @RequestBody TransferCreateRequest transferCreateRequest) {
        return ResponseEntity.ok(playerService.transferPlayerById(playerId, transferCreateRequest.getBuyingTeamId()));
    }
}
