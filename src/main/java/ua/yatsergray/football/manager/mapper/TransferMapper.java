package ua.yatsergray.football.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.yatsergray.football.manager.domain.dto.TransferDTO;
import ua.yatsergray.football.manager.domain.entity.Transfer;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransferMapper {

    @Mapping(source = "player.firstName", target = "playerFirstName")
    @Mapping(source = "player.lastName", target = "playerLastName")
    @Mapping(source = "sellingTeam.name", target = "sellingTeamName")
    @Mapping(source = "buyingTeam.name", target = "buyingTeamName")
    TransferDTO mapToTransferDTO(Transfer transfer);

    List<TransferDTO> mapAllToTransferDTOList(List<Transfer> transfers);
}
