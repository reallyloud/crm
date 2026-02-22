package ru.mentee.power.crm.testHelpClasses;

import static org.instancio.Select.field;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import org.instancio.Instancio;
import ru.mentee.power.crm.domain.DealStatus;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.entity.*;

public class DataGenerator {

  public static Lead generateRandomLead() {
    Lead lead =
        Instancio.of(Lead.class)
            .ignore(field(Lead::getId))
            .ignore(field(Lead::getVersion))
            .ignore(field(Lead::getCreatedAt))
            .ignore(field(Lead::getCompany))
            .generate(
                field(Lead::getName), generate -> generate.string().minLength(2).maxLength(40))
            .generate(
                field(Lead::getEmail), generate -> generate.string().minLength(2).maxLength(40))
            .generate(
                field(Lead::getPhone), generate -> generate.string().minLength(2).maxLength(40))
            .generate(field(Lead::getStatus), generate -> generate.enumOf(LeadStatus.class))
            .create();
    lead.setCompany(generateRandomCompany());
    return lead;
  }

  public static Deal generateRandomDeal(Lead lead) {
    return Instancio.of(Deal.class)
        .ignore(field(Deal::getId))
        .ignore(field(Deal::getDealProducts))
        .ignore(field(Deal::getLeadId))
        .generate(field(Deal::getStatus), generate -> generate.enumOf(DealStatus.class))
        .set(field(Deal::getAmount), generateRandomBigDecimal())
        .ignore(field(Deal::getCreatedAt))
        .generate(field(Deal::getTitle), generate -> generate.string().minLength(3).maxLength(20))
        .create();
  }

  public static Company generateRandomCompany() {
    return Instancio.of(Company.class)
        .ignore(field(Company::getId))
        .ignore(field(Company::getLeads))
        .generate(field(Company::getName), generate -> generate.string().minLength(2).maxLength(40))
        .generate(
            field(Company::getIndustry), generate -> generate.string().minLength(2).maxLength(40))
        .create();
  }

  public static Product generateRandomProduct() {
    return Instancio.of(Product.class)
        .ignore(field(Product::getId))
        .ignore(field(Product::getDealProducts))
        .generate(field(Product::getSku), generate -> generate.string().minLength(2).maxLength(40))
        .generate(field(Product::getName), generate -> generate.string().minLength(2).maxLength(40))
        .set(field(Product::getPrice), generateRandomBigDecimal())
        .set(field(Product::getActive), true)
        .create();
  }

  public static DealProduct generateRandomDealProduct() {
    return Instancio.of(DealProduct.class)
        .ignore(field(DealProduct::getId))
        .ignore(field(DealProduct::getDeal))
        .ignore(field(DealProduct::getProduct))
        .generate(field(DealProduct::getQuantity), gen -> gen.ints().range(1, 30))
        .set(field(DealProduct::getUnitPrice), generateRandomBigDecimal())
        .create();
  }

  public static BigDecimal generateRandomBigDecimal() {
    Random random = new Random();
    return BigDecimal.valueOf(random.nextInt(1, 999999)).setScale(2, RoundingMode.HALF_UP);
  }
}
