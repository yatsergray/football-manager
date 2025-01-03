package ua.yatsergray.football.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ua.yatsergray.football.manager.domain.dto.TeamDTO;
import ua.yatsergray.football.manager.domain.entity.Team;

import java.util.List;

@Mapper(componentModel = "spring", uses = PlayerMapper.class)
public interface TeamMapper {

    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    @Mapping(source = "players", target = "playerDTOList")
    TeamDTO mapToTeamDTO(Team team);

    List<TeamDTO> mapAllToTeamDTOList(List<Team> teams);
}
