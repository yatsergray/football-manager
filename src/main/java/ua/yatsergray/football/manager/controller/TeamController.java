package ua.yatsergray.football.manager.controller;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.yatsergray.football.manager.domain.dto.TeamDTO;
import ua.yatsergray.football.manager.domain.request.TeamCreateUpdateRequest;
import ua.yatsergray.football.manager.service.impl.TeamServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {
    private final TeamServiceImpl teamService;

    @Autowired
    public TeamController(TeamServiceImpl teamService) {
        this.teamService = teamService;
    }

    @SneakyThrows
    @PostMapping
    public ResponseEntity<TeamDTO> createTeam(@Valid @RequestBody TeamCreateUpdateRequest teamCreateUpdateRequest) {
        return ResponseEntity.ok(teamService.addTeam(teamCreateUpdateRequest));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDTO> readTeamById(@PathVariable("teamId") UUID teamId) {
        return teamService.getTeamById(teamId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TeamDTO>> readAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @SneakyThrows
    @PutMapping("/{teamId}")
    public ResponseEntity<TeamDTO> updateTeamById(@PathVariable("teamId") UUID teamId, @Valid @RequestBody TeamCreateUpdateRequest teamCreateUpdateRequest) {
        return ResponseEntity.ok(teamService.modifyTeamById(teamId, teamCreateUpdateRequest));
    }

    @SneakyThrows
    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeamById(@PathVariable("teamId") UUID teamId) {
        teamService.removeTeamById(teamId);

        return ResponseEntity.ok().build();
    }
}
