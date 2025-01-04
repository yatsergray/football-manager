package ua.yatsergray.football.manager.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TransferCreateRequest {

    @NotNull(message = "Buying team id is mandatory")
    private UUID buyingTeamId;
}
