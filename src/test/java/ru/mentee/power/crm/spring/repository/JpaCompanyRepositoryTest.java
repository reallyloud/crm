package ru.mentee.power.crm.spring.repository;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.spring.entity.Company;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.testHelpClasses.DataGenerator;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CompanyRepositoryTest {

  @Autowired private JpaCompanyRepository companyRepository;

  @Autowired private JpaLeadRepository leadRepository;

  @Autowired private EntityManager entityManager;

  @Test
  void shouldSaveCompanyWithLeads() {
    // Given
    Company company = DataGenerator.generateRandomCompany();

    Lead lead1 = DataGenerator.generateRandomLead();
    Lead lead2 = DataGenerator.generateRandomLead();

    company.addLead(lead1);
    company.addLead(lead2);

    // When
    Company saved = companyRepository.save(company);

    // Then
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getLeads()).hasSize(2);

    // Проверяем, что в БД создались записи
    Company found = companyRepository.findById(saved.getId()).orElseThrow();
    assertThat(found.getLeads()).hasSize(2);
  }

  @Test
  void shouldAvoidN1WithEntityGraph() {
    // Given — создаём компанию с 5 лидами
    Company company = DataGenerator.generateRandomCompany();
    for (int i = 0; i < 5; i++) {
      company.addLead(DataGenerator.generateRandomLead());
    }
    Company saved = companyRepository.save(company);

    // Сбрасываем изменения в БД и очищаем Persistence Context для чистоты эксперимента
    entityManager.flush();
    entityManager.clear();

    // When — используем метод с @EntityGraph
    Company found = companyRepository.findByIdWithLeads(saved.getId()).get();
    // Then — проверяем, что leads загружены
    assertThat(found.getLeads()).hasSize(5);

    // Проверьте SQL логи: должен быть 1 запрос с LEFT JOIN,
    // а не 1 SELECT для Company + 5 SELECT для каждого Lead
  }
}
