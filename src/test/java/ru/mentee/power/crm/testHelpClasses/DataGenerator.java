package ru.mentee.power.crm.testHelpClasses;

import org.instancio.Instancio;
import static org.instancio.Select.field;

import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.entity.Company;
import ru.mentee.power.crm.spring.entity.Deal;
import ru.mentee.power.crm.spring.entity.Lead;


public class DataGenerator {

    public static Lead generateRandomLead (){
        Lead lead = Instancio.of(Lead.class)
                .ignore(field(Lead::getId))
                .ignore(field(Lead::getVersion))
                .ignore(field(Lead::getCreatedAt))
                .ignore(field(Lead::getCompany))
                .generate(field(Lead::getName), generate -> generate.string().minLength(2).maxLength(40))
                .generate(field(Lead::getEmail), generate -> generate.string().minLength(2).maxLength(40))
                .generate(field(Lead::getPhone), generate -> generate.string().minLength(2).maxLength(40))
                .generate(field(Lead::getStatus), generate -> generate.enumOf(LeadStatus.class))
                .create();
        lead.setCompany(generateRandomCompany());
        return lead;
    }

    public static Deal generateRandomDeal(Lead lead) {
        return Instancio.of(Deal.class)
                .generate(field(Deal::getAmount), generate -> generate.string().minLength(3).maxLength(20))
                .generate(field(Deal::getTitle), generate -> generate.string().minLength(3).maxLength(20))
                .create();
    }

    public static Company generateRandomCompany() {
        return Instancio.of(Company.class)
                .ignore(field(Company::getId))
                .ignore(field(Company::getLeads))
                .generate(field(Company::getName),generate -> generate.string().minLength(2).maxLength(40))
                .generate(field(Company::getIndustry), generate -> generate.string().minLength(2).maxLength(40))
                .create();
    }
}
