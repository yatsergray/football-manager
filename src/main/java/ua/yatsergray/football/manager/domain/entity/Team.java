package ua.yatsergray.football.manager.domain.entity;

import lombok.*;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Team {
    private UUID id;
    private String name;
    private Integer commissionPercentage;
    private BigDecimal bankAccountBalance;

    @Builder.Default
    private Set<Player> players = new LinkedHashSet<>();

    @Builder.Default
    private Set<Transfer> sellingTransfers = new LinkedHashSet<>();

    @Builder.Default
    private Set<Transfer> buyingTransfers = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team team)) return false;
        return Objects.equals(id, team.id) && Objects.equals(name, team.name) && Objects.equals(commissionPercentage, team.commissionPercentage) && Objects.equals(bankAccountBalance, team.bankAccountBalance) && Objects.equals(players, team.players) && Objects.equals(sellingTransfers, team.sellingTransfers) && Objects.equals(buyingTransfers, team.buyingTransfers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, commissionPercentage, bankAccountBalance, players, sellingTransfers, buyingTransfers);
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", commissionPercentage=" + commissionPercentage +
                ", bankAccountBalance=" + bankAccountBalance +
                '}';
    }
}
