package ua.yatsergray.football.manager.domain.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TeamCreateUpdateRequest {

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Commission percentage is mandatory")
    @Min(value = 0, message = "Commission percentage must be at least 0")
    @Max(value = 10, message = "Commission percentage must not exceed 10")
    private Integer commissionPercentage;

    @NotNull(message = "Bank account balance is mandatory")
    @DecimalMin(value = "0.00", message = "Bank account balance must be at least 0")
    @Digits(integer = 17, fraction = 2, message = "Bank account balance must have up to 17 integer digits and 2 fractional digits")
    private BigDecimal bankAccountBalance;
}
