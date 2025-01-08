package ua.yatsergray.football.manager.domain.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Transfer {
    private UUID id;
    private BigDecimal totalCost;
    private LocalDate date;
    private Player player;
    private Team sellingTeam;
    private Team buyingTeam;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transfer transfer)) return false;
        return Objects.equals(id, transfer.id) && Objects.equals(totalCost, transfer.totalCost) && Objects.equals(date, transfer.date) && Objects.equals(player, transfer.player) && Objects.equals(sellingTeam, transfer.sellingTeam) && Objects.equals(buyingTeam, transfer.buyingTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalCost, date, player, sellingTeam, buyingTeam);
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", totalCost=" + totalCost +
                ", date=" + date +
                '}';
    }
}
