package ua.yatsergray.football.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.yatsergray.football.manager.domain.dto.PlayerDTO;
import ua.yatsergray.football.manager.domain.entity.Player;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    PlayerDTO mapToPlayerDTO(Player player);

    List<PlayerDTO> mapAllToPlayerDTOList(List<Player> players);
}
