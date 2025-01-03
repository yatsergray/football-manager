package ua.yatsergray.football.manager.domain.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PlayerDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private Integer age;
    private Integer monthsOfExperience;
}
