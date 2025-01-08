package ua.yatsergray.football.manager.domain.entity;

import lombok.*;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Player {
    private UUID id;
    private String firstName;
    private String lastName;
    private Integer age;
    private Integer monthsOfExperience;
    private Team team;

    @Builder.Default
    private Set<Transfer> transfers = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return Objects.equals(id, player.id) && Objects.equals(firstName, player.firstName) && Objects.equals(lastName, player.lastName) && Objects.equals(age, player.age) && Objects.equals(monthsOfExperience, player.monthsOfExperience) && Objects.equals(team, player.team) && Objects.equals(transfers, player.transfers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, age, monthsOfExperience, team, transfers);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", monthsOfExperience=" + monthsOfExperience +
                '}';
    }
}
