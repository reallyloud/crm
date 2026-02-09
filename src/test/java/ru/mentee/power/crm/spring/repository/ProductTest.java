package ru.mentee.power.crm.spring.repository;

import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.spring.entity.Product;
import ru.mentee.power.crm.testHelpClasses.DataGenerator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class JpaProductRepositoryTest {

    @Autowired
    private JpaProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveAndFindProduct_whenValidData() {
        // Given
        Product product = new Product();
        product.setName("Консультация по архитектуре");
        product.setSku("CONSULT-ARCH-001");
        product.setPrice(new BigDecimal("50000.00"));
        product.setActive(true);

        // When
        Product saved = productRepository.save(product);

        // Then
        assertThat(saved.getId()).isNotNull();
        Optional<Product> found = productRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getSku()).isEqualTo("CONSULT-ARCH-001");
    }

    @Test
    void findBySkuTest() {
        //Given
        Product product = DataGenerator.generateRandomProduct();

        //When
        Product saved = productRepository.save(product);

        //Then
        assertThat(saved.getId()).isNotNull();
        Optional<Product> found = productRepository.findBySku(product.getSku());
        assertThat(found).isPresent();
        assertThat(found.get().getSku()).isEqualTo(product.getSku());

    }

    @Test
    void findByActiveTrueTest() {
        //Given
        Product product1 = DataGenerator.generateRandomProduct();
        Product product2 = DataGenerator.generateRandomProduct();
        Product product3 = DataGenerator.generateRandomProduct();
        product3.setActive(false);
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        //When
        List<Product> found = productRepository.findByActiveTrue();

        assertThat(found).hasSize(2);
    }

    @Test
    void uniqueConstraintSkuTest() {
        //Given
        Product product1 = DataGenerator.generateRandomProduct();
        Product product2 = DataGenerator.generateRandomProduct();
        product1.setSku("TEST-001");
        product2.setSku("TEST-001");
        productRepository.save(product1);

        //When
        assertThatThrownBy(() -> {
                    productRepository.save(product2);
                    entityManager.flush();
                }
        ).isInstanceOf(ConstraintViolationException.class);

    }


    // TODO: реализуйте тест для unique constraint на SKU
    // Given продукт с SKU "TEST-001" сохранён
    // When пытаемся сохранить второй продукт с тем же SKU
    // Then выбрасывается DataIntegrityViolationException
}
