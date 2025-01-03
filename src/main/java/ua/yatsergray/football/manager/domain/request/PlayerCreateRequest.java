package ua.yatsergray.football.manager.domain.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PlayerCreateRequest {

    @NotBlank(message = "First name is mandatory")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotNull(message = "Age is mandatory")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must not exceed 100")
    private Integer age;

    @NotNull(message = "Months of experience are mandatory")
    @Positive(message = "Months of experience must be greater than 0")
    private Integer monthsOfExperience;

    @NotNull(message = "Team id is mandatory")
    private UUID teamId;
}
