package ua.yatsergray.football.manager.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "total_cost", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalCost;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "player_id",
            foreignKey = @ForeignKey(
                    name = "fk_transfer_player_id",
                    value = ConstraintMode.CONSTRAINT,
                    foreignKeyDefinition = "FOREIGN KEY (player_id) REFERENCES players (id) ON DELETE SET NULL"
            )
    )
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "selling_team_id",
            foreignKey = @ForeignKey(
                    name = "fk_transfer_selling_team_id",
                    value = ConstraintMode.CONSTRAINT,
                    foreignKeyDefinition = "FOREIGN KEY (selling_team_id) REFERENCES teams (id) ON DELETE SET NULL"
            )
    )
    private Team sellingTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "buying_team_id",
            foreignKey = @ForeignKey(
                    name = "fk_transfer_buying_team_id",
                    value = ConstraintMode.CONSTRAINT,
                    foreignKeyDefinition = "FOREIGN KEY (buying_team_id) REFERENCES teams (id) ON DELETE SET NULL"
            )
    )
    private Team buyingTeam;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Transfer transfer = (Transfer) o;
        return getId() != null && Objects.equals(getId(), transfer.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "totalCost = " + totalCost + ", " +
                "date = " + date + ")";
    }
}
