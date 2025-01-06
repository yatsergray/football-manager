package ua.yatsergray.football.manager.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PlayerDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private Integer age;
    private Integer monthsOfExperience;

    @JsonProperty("transfers")
    @Builder.Default
    private List<TransferDTO> transferDTOList = new ArrayList<>();
}
