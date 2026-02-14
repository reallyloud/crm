package ru.mentee.power.crm.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.domain.DealStatus;
import ru.mentee.power.crm.spring.entity.Deal;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.repository.JpaDealRepository;
import ru.mentee.power.crm.spring.repository.JpaLeadRepository;
import ru.mentee.power.crm.testHelpClasses.DataGenerator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest(classes = ru.mentee.power.crm.spring.Application.class)
@Transactional
class JpaDealServiceFullTest {

    @Autowired
    private JpaDealService dealService;

    @Autowired
    private JpaDealRepository dealRepository;

    @Autowired
    private JpaLeadRepository leadRepository;

    private Lead savedLead;
    private Deal savedDeal;

    @BeforeEach
    void setUp() {
        dealRepository.deleteAll();
        leadRepository.deleteAll();

        savedLead = DataGenerator.generateRandomLead();
        savedLead = leadRepository.save(savedLead);

        savedDeal = new Deal("Test Deal", savedLead.getId(), BigDecimal.valueOf(50000), DealStatus.NEW);
        savedDeal = dealRepository.save(savedDeal);
    }

    @Test
    void getAllDeals_shouldReturnAllDeals() {
        Lead lead2 = DataGenerator.generateRandomLead();
        lead2 = leadRepository.save(lead2);

        Deal deal2 = new Deal("Deal 2", lead2.getId(), BigDecimal.valueOf(30000), DealStatus.QUALIFIED);
        dealRepository.save(deal2);

        List<Deal> deals = dealService.getAllDeals();

        assertThat(deals).hasSize(2);
    }

    @Test
    void findById_shouldReturnDealWhenExists() {
        Optional<Deal> found = dealService.findById(savedDeal.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Test Deal");
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        Optional<Deal> found = dealService.findById(UUID.randomUUID());

        assertThat(found).isEmpty();
    }

    @Test
    void getDealsByStatusForKanban_shouldGroupByStatus() {
        Lead lead2 = DataGenerator.generateRandomLead();
        lead2 = leadRepository.save(lead2);

        Deal deal2 = new Deal("Deal Qualified", lead2.getId(), BigDecimal.valueOf(30000), DealStatus.QUALIFIED);
        dealRepository.save(deal2);

        Map<DealStatus, List<Deal>> kanban = dealService.getDealsByStatusForKanban();

        assertThat(kanban).containsKey(DealStatus.NEW);
        assertThat(kanban).containsKey(DealStatus.QUALIFIED);
        assertThat(kanban.get(DealStatus.NEW)).hasSize(1);
        assertThat(kanban.get(DealStatus.QUALIFIED)).hasSize(1);
    }

    @Test
    void transitionDealStatus_shouldTransitionValidStatus() {
        Deal transitioned = dealService.transitionDealStatus(savedDeal.getId(), DealStatus.QUALIFIED);

        assertThat(transitioned.getStatus()).isEqualTo(DealStatus.QUALIFIED);
        assertThat(dealRepository.findById(savedDeal.getId()).get().getStatus())
                .isEqualTo(DealStatus.QUALIFIED);
    }

    @Test
    void transitionDealStatus_shouldThrowOnInvalidTransition() {
        assertThatThrownBy(() -> dealService.transitionDealStatus(savedDeal.getId(), DealStatus.WON))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void transitionDealStatus_shouldThrowWhenDealNotFound() {
        UUID nonExistentId = UUID.randomUUID();

        assertThatThrownBy(() -> dealService.transitionDealStatus(nonExistentId, DealStatus.QUALIFIED))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Deal not found");
    }

    @Test
    void convertLeadToDeal_shouldCreateDeal() {
        Lead lead2 = DataGenerator.generateRandomLead();
        lead2 = leadRepository.save(lead2);

        Deal deal = dealService.convertLeadToDeal(lead2.getId(), BigDecimal.valueOf(75000));

        assertThat(deal.getId()).isNotNull();
        assertThat(deal.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(75000));
        assertThat(deal.getStatus()).isEqualTo(DealStatus.NEW);
    }

    @Test
    void convertLeadToDeal_shouldThrowWhenLeadNotFound() {
        UUID nonExistentLeadId = UUID.randomUUID();

        assertThatThrownBy(() -> dealService.convertLeadToDeal(nonExistentLeadId, BigDecimal.valueOf(100)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void convertLeadToDeal_shouldThrowWhenAmountIsNull() {
        assertThatThrownBy(() -> dealService.convertLeadToDeal(savedLead.getId(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
