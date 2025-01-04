package ua.yatsergray.football.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.yatsergray.football.manager.domain.dto.PlayerDTO;
import ua.yatsergray.football.manager.domain.entity.Player;

import java.util.List;

@Mapper(componentModel = "spring", uses = TransferMapper.class)
public interface PlayerMapper {

    @Mapping(source = "transfers", target = "transferDTOList")
    PlayerDTO mapToPlayerDTO(Player player);

    List<PlayerDTO> mapAllToPlayerDTOList(List<Player> players);
}
