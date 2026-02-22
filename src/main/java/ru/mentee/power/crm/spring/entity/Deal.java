package ru.mentee.power.crm.spring.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.*;
import ru.mentee.power.crm.domain.DealStatus;

@Entity
@Table(name = "deals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Deal {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String title;

  @JoinColumn(name = "lead_id")
  private UUID leadId;

  @NotNull
  @Column(nullable = false)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  private DealStatus status;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "deal", cascade = CascadeType.ALL, orphanRemoval = true)
  List<DealProduct> dealProducts = new ArrayList<>();

  public void addDealProduct(DealProduct dealProduct) {
    dealProduct.setDeal(this);
    dealProducts.add(dealProduct);
  }

  public void removeDealProduct(DealProduct dealProduct) {
    dealProduct.setDeal(null);
    dealProducts.remove(dealProduct);
  }

  public Deal(String title, UUID leadId, BigDecimal amount, DealStatus status) {
    this.title = title;
    this.leadId = leadId;
    this.amount = amount;
    this.status = status;
  }

  @PrePersist
  protected void onCreate() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
  }

  public void transitionTo(DealStatus newStatus) {
    if (!status.canTransitionTo(newStatus)) {
      throw new IllegalArgumentException(
          "Невозможно перейти от статуса Deal \"" + status + "\" к статусу \"" + newStatus + "\"");
    }
    this.status = newStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Deal deal)) return false;
    return Objects.equals(id, deal.id)
        && Objects.equals(title, deal.title)
        && Objects.equals(leadId, deal.leadId)
        && Objects.equals(amount, deal.amount)
        && status == deal.status
        && Objects.equals(createdAt, deal.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, leadId, amount, status, createdAt);
  }
}
