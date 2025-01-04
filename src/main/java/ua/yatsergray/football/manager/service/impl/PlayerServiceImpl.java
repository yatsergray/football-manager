package ua.yatsergray.football.manager.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.yatsergray.football.manager.domain.dto.PlayerDTO;
import ua.yatsergray.football.manager.domain.dto.TransferDTO;
import ua.yatsergray.football.manager.domain.entity.Player;
import ua.yatsergray.football.manager.domain.entity.Team;
import ua.yatsergray.football.manager.domain.entity.Transfer;
import ua.yatsergray.football.manager.domain.request.PlayerCreateRequest;
import ua.yatsergray.football.manager.domain.request.PlayerUpdateRequest;
import ua.yatsergray.football.manager.exception.InsufficientTeamBankAccountBalanceException;
import ua.yatsergray.football.manager.exception.NoSuchPlayerException;
import ua.yatsergray.football.manager.exception.NoSuchTeamException;
import ua.yatsergray.football.manager.exception.PlayerConflictException;
import ua.yatsergray.football.manager.mapper.PlayerMapper;
import ua.yatsergray.football.manager.mapper.TransferMapper;
import ua.yatsergray.football.manager.repository.PlayerRepository;
import ua.yatsergray.football.manager.repository.TeamRepository;
import ua.yatsergray.football.manager.repository.TransferRepository;
import ua.yatsergray.football.manager.service.PlayerService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final TransferRepository transferRepository;
    private final PlayerMapper playerMapper;
    private final TransferMapper transferMapper;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository, TransferRepository transferRepository, PlayerMapper playerMapper, TransferMapper transferMapper) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.transferRepository = transferRepository;
        this.playerMapper = playerMapper;
        this.transferMapper = transferMapper;
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

        transferRepository.deleteAll(transferRepository.findAvailableToDeleteByPlayerId(playerId));
        playerRepository.deleteById(playerId);
    }

    @Override
    public TransferDTO transferPlayerById(UUID playerId, UUID buyingTeamId) throws NoSuchPlayerException, PlayerConflictException, InsufficientTeamBankAccountBalanceException {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NoSuchPlayerException(String.format("Player with id=\"%s\" does not exist", playerId)));
        Team sellingTeam = player.getTeam();
        Team buyingTeam = teamRepository.findById(buyingTeamId)
                .orElseThrow(() -> new NoSuchPlayerException(String.format("Team with id=\"%s\" does not exist", buyingTeamId)));

        if (sellingTeam.equals(buyingTeam)) {
            throw new PlayerConflictException(String.format("Player with id=\"%s\" already belongs to Team with id=\"%s\"", playerId, buyingTeamId));
        }

        BigDecimal totalTransferCost = calculateTotalTransferCost(player.getMonthsOfExperience(), player.getAge(), buyingTeam.getCommissionPercentage());

        if (buyingTeam.getBankAccountBalance().compareTo(totalTransferCost) < 0) {
            throw new InsufficientTeamBankAccountBalanceException(String.format("Team with id=\"%s\" does not have enough funds to buy Player with id=\"%s\"", buyingTeamId, playerId));
        }

        buyingTeam.setBankAccountBalance(buyingTeam.getBankAccountBalance().subtract(totalTransferCost));
        sellingTeam.setBankAccountBalance(sellingTeam.getBankAccountBalance().add(totalTransferCost));

        player.setTeam(buyingTeam);

        teamRepository.save(buyingTeam);
        teamRepository.save(sellingTeam);

        Transfer transfer = Transfer.builder()
                .totalCost(totalTransferCost)
                .date(LocalDate.now())
                .player(player)
                .sellingTeam(sellingTeam)
                .buyingTeam(buyingTeam)
                .build();

        return transferMapper.mapToTransferDTO(transferRepository.save(transfer));
    }

    private BigDecimal calculateTotalTransferCost(Integer playerMonthsOfExperience, Integer playerAge, Integer buyingTeamCommissionPercentage) {
        BigDecimal baseTransferCost = new BigDecimal(playerMonthsOfExperience)
                .multiply(new BigDecimal(100000))
                .divide(new BigDecimal(playerAge), RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal buyingTeamCommission = baseTransferCost
                .multiply(new BigDecimal(buyingTeamCommissionPercentage))
                .divide(new BigDecimal(100), RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        return baseTransferCost
                .add(buyingTeamCommission)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
