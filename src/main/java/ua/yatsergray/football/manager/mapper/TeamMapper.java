package ua.yatsergray.football.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.yatsergray.football.manager.domain.dto.TeamDTO;
import ua.yatsergray.football.manager.domain.entity.Team;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PlayerMapper.class, TransferMapper.class})
public interface TeamMapper {

    @Mapping(source = "players", target = "playerDTOList")
    @Mapping(source = "sellingTransfers", target = "sellingTransferDTOList")
    @Mapping(source = "buyingTransfers", target = "buyingTransferDTOList")
    TeamDTO mapToTeamDTO(Team team);

    List<TeamDTO> mapAllToTeamDTOList(List<Team> teams);
}
