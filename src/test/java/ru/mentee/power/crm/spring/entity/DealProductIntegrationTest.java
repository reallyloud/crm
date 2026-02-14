package ru.mentee.power.crm.spring.entity;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.mentee.power.crm.spring.repository.JpaDealRepository;
import ru.mentee.power.crm.spring.repository.JpaProductRepository;
import ru.mentee.power.crm.testHelpClasses.DataGenerator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class DealProductIntegrationTest {

    Logger log = LoggerFactory.getLogger(DealProductIntegrationTest.class);

    @Autowired
    private JpaDealRepository dealRepository;

    @Autowired
    private JpaProductRepository productRepository;
    
    @Autowired
    private EntityManager entityManager;

    @Test
    void testSaveDealWithProducts() {

        //Given
        Deal deal = getDeal();

        //When
        dealRepository.save(deal);
        Optional<Deal> result = dealRepository.findById(deal.getId());

        //Then
        assertThat(result).isPresent();
        List<DealProduct> dealProducts = result.get().getDealProducts();

        assertThat(dealProducts).hasSize(2);
        DealProduct dealProduct1 = dealProducts.get(0);
        DealProduct dealProduct2 = dealProducts.get(1);
        assertThat(dealProduct1.getQuantity()).isEqualTo(Integer.valueOf(2));
        assertThat(dealProduct1.getUnitPrice()).isEqualTo(BigDecimal.valueOf(81000));
        assertThat(dealProduct2.getQuantity()).isEqualTo(Integer.valueOf(1));
        assertThat(dealProduct2.getUnitPrice()).isEqualTo(BigDecimal.valueOf(25000));
    }

    @Test
    void testEntityGraphSolvesNPlusOne() {
        Deal deal = DataGenerator.generateRandomDeal(DataGenerator.generateRandomLead());
        DealProduct dealProduct1 = DataGenerator.generateRandomDealProduct();
        DealProduct dealProduct2 = DataGenerator.generateRandomDealProduct();
        DealProduct dealProduct3 = DataGenerator.generateRandomDealProduct();

        deal.addDealProduct(dealProduct1);
        deal.addDealProduct(dealProduct2);
        deal.addDealProduct(dealProduct3);

        dealRepository.save(deal);
        entityManager.flush();
        entityManager.clear();

        log.info("Должна быть проблема N+1 (много SELECT):");
        Optional<Deal> result1 = dealRepository.findById(deal.getId());
        assertThat(result1).isPresent();
        Deal deal1 = result1.get();

        UUID[] products1 = new UUID[3];
        UUID[] products2 = new UUID[3];

        int i = 0;
        for (DealProduct dealProduct: deal1.getDealProducts()) {
            products1[i] = dealProduct.getId();
            i++;
        }


        entityManager.clear();

        log.info("НЕ должна быть проблема N+1 (не много SELECT):");
        Optional<Deal> result2 = dealRepository.findDealWithProducts(deal.getId());
        assertThat(result2).isPresent();
        Deal deal2 = result2.get();
        i = 0;
        for (DealProduct dealProduct: deal2.getDealProducts()) {
            products2[i] = dealProduct.getId();
            i++;
        }

        assertThat(products1).isEqualTo(products2);

    }


    private static Deal getDeal() {
        Deal deal = new Deal();
        deal.setAmount(BigDecimal.valueOf(150000));

        Product product1 = new Product();
        product1.setName("Ноутбук Dell");
        product1.setSku("LAPTOP-001");
        product1.setPrice(BigDecimal.valueOf(90000));

        Product product2 = new Product();
        product2.setName("Монитор LG");
        product2.setSku("MONITOR-001");
        product2.setPrice(BigDecimal.valueOf(25000));

        DealProduct dealProduct1 = new DealProduct();
        dealProduct1.setProduct(product1);
        dealProduct1.setQuantity(2);
        dealProduct1.setUnitPrice(BigDecimal.valueOf(81000));

        DealProduct dealProduct2 = new DealProduct();
        dealProduct2.setProduct(product2);
        dealProduct2.setQuantity(1);
        dealProduct2.setUnitPrice(BigDecimal.valueOf(25000));

        deal.addDealProduct(dealProduct1);
        deal.addDealProduct(dealProduct2);
        return deal;
    }
}