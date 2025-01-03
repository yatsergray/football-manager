package ua.yatsergray.football.manager.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TeamDTO {
    private UUID id;
    private String name;
    private Integer commissionPercentage;
    private BigDecimal bankAccountBalance;

    @JsonProperty("players")
    @Builder.Default
    private List<PlayerDTO> playerDTOList = new ArrayList<>();
}
