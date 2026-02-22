package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DealTest {

  @Test
  void shouldCreateDeal_withNewStatus() {
    UUID leadId = UUID.randomUUID();
    BigDecimal amount = new BigDecimal("100000.00");

    Deal deal = new Deal(leadId, amount);

    assertThat(deal.getId()).isNotNull();
    assertThat(deal.getLeadId()).isEqualTo(leadId);
    assertThat(deal.getAmount()).isEqualTo(amount);
    assertThat(deal.getStatus()).isEqualTo(DealStatus.NEW);
    assertThat(deal.getCreatedAt()).isNotNull();
  }

  @Test
  void shouldTransitionToValidStatus() {
    Deal deal = new Deal(UUID.randomUUID(), BigDecimal.valueOf(8493689));
    deal.transitionTo(DealStatus.QUALIFIED);
    assertThat(deal.getStatus()).isEqualTo(DealStatus.QUALIFIED);
    // TODO: создайте Deal, вызовите transitionTo(QUALIFIED), проверьте что статус изменился
  }

  @Test
  void shouldThrowException_whenTransitionInvalid() {
    // TODO: создайте Deal в статусе WON, попробуйте transitionTo(NEW)
    Deal deal =
        new Deal(
            UUID.randomUUID(),
            UUID.randomUUID(),
            BigDecimal.valueOf(589487),
            DealStatus.WON,
            LocalDateTime.now());
    assertThatThrownBy(() -> deal.transitionTo(DealStatus.NEW))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(("Невозможно перейти от статуса Deal \"WON\" к статусу \"NEW\""));

    // assertThatThrownBy(() -> deal.transitionTo(DealStatus.NEW))
    //   .isInstanceOf(IllegalStateException.class)
    //   .hasMessageContaining("Cannot transition from WON to NEW");
  }
}
