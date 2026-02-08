package ru.mentee.power.crm.spring.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ru.mentee.power.crm.spring.entity.Deal;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.repository.JpaDealRepository;
import ru.mentee.power.crm.spring.repository.JpaLeadRepository;
import ru.mentee.power.crm.spring.utility.PropagationInvoker;
import ru.mentee.power.crm.testHelpClasses.DataGenerator;

@ActiveProfiles("test")
@SpringBootTest(classes = ru.mentee.power.crm.spring.Application.class)
@Transactional
class JpaDealServiceIntegrationTest {
    @Autowired
    JpaDealRepository dealRepository;

    @Autowired
    JpaLeadRepository leadRepository;

    @Autowired
    JpaDealService dealService;

    @Autowired
    JpaLeadService leadService;

    @Autowired
    PropagationInvoker invoke;

    Lead lead;
    Lead lead1;
    Lead lead2;
    Lead lead3;
    Lead lead4;

    @BeforeEach
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void setUp() {
        leadRepository.deleteAll();
        this.lead = DataGenerator.generateRandomLead();
        this.lead1 = DataGenerator.generateRandomLead();
        this.lead2 = DataGenerator.generateRandomLead();
        this.lead3 = DataGenerator.generateRandomLead();
        this.lead4 = DataGenerator.generateRandomLead();

        this.lead = leadRepository.save(this.lead);
        this.lead1 = leadRepository.save(this.lead1);
        this.lead2 = leadRepository.save(this.lead2);
        this.lead3 = leadRepository.save(this.lead3);
        this.lead4 = leadRepository.save(this.lead4);

    }


    @Test
    void convertLeadToDeal_shouldRollbackOnConstraintViolation() {
        //Given

        Deal deal = new Deal();
        try {
            deal = dealService.convertLeadToDeal(lead.getId(), null);
        } catch (NullPointerException | IllegalArgumentException _) {

        }
        //When,Then
        assertThatThrownBy(() ->
                dealService.convertLeadToDeal(lead.getId(), null)
        );

        assertThat(deal.getId()).isNull();
    }

    @Test
    void convertLeadToDeal_shouldNotRollbackOnConstraintViolation() {
        //Given
        // Объект lead уже сохранен в методе setUp()

        //When
        Deal deal = dealService.convertLeadToDeal(lead.getId(), BigDecimal.valueOf(675));

        //Then

        assertThat(dealRepository.existsById(deal.getId())).isTrue();

    }

    @Test
    void demonstrateSelfInvocationProblem() {
        //Given
        List<UUID> ids = leadRepository.findAllIds();
        assertThat(ids).isNotEmpty();

        //When
        try {
            leadService.processLeadsSelfInvoke(ids);
        } catch (IllegalArgumentException e) {
            System.out.println("catched exception");
        }

        Lead firstLead = leadRepository.findById(ids.getFirst()).get();
        Lead secondLead = leadRepository.findById(ids.get(1)).get();
        Lead thirdLead = leadRepository.findById(ids.get(2)).get();

        //Then
        assertThat(firstLead.getName()).isEqualTo("PROCESSED");

        //Последующие лиды не меняют имя, хотя ошибку выкинуло только на втором,
        //то есть дальнейшие транзакции не проходят.
        assertThat(secondLead.getName()).isNotEqualTo("PROCESSED");
        assertThat(thirdLead.getName()).isNotEqualTo("PROCESSED");
    }

    void fixingSelfInvocationProblem() {
        //Given
        List<UUID> ids = leadRepository.findAllIds();

        //When
        try {
            leadService.processLeads(ids);
        } catch (IllegalArgumentException e) {
            System.out.println("catched exception");
        }

        Lead firstLead = leadRepository.findById(ids.getFirst()).get();
        Lead secondLead = leadRepository.findById(ids.get(1)).get();
        Lead thirdLead = leadRepository.findById(ids.get(2)).get();

        //Then
        assertThat(firstLead.getName()).isEqualTo("PROCESSED");

        //Последующие лиды не меняют имя, хотя ошибку выкинуло только на втором,
        //то есть дальнейшие транзакции не проходят.
        assertThat(secondLead.getName()).isNotEqualTo("PROCESSED");
        assertThat(thirdLead.getName()).isEqualTo("PROCESSED");
    }
}