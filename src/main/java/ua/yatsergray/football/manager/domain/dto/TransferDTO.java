package ua.yatsergray.football.manager.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TransferDTO {
    private BigDecimal totalCost;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate date;
    private String playerFirstName;
    private String playerLastName;
    private String sellingTeamName;
    private String buyingTeamName;
}
