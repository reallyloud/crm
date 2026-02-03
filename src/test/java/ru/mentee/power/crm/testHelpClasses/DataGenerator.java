package ru.mentee.power.crm.testHelpClasses;

import org.instancio.Instancio;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.entity.Deal;
import ru.mentee.power.crm.spring.entity.Lead;

import static org.instancio.Select.field;


public class DataGenerator {

    public static Lead generateRandomLead (){
        return Instancio.of(Lead.class)
                .ignore(field(Lead::getId))
                .ignore(field(Lead::getCreatedAt))
                .generate(field(Lead::getName), generate -> generate.string().minLength(2).maxLength(40))
                .generate(field(Lead::getCompany), generate -> generate.string().minLength(2).maxLength(40))
                .generate(field(Lead::getEmail), generate -> generate.string().minLength(2).maxLength(40))
                .generate(field(Lead::getPhone), generate -> generate.string().minLength(2).maxLength(40))
                .generate(field(Lead::getStatus), generate -> generate.enumOf(LeadStatus.class))
                .create();
    }

    public static Deal generateRandomDeal(Lead lead) {
        return Instancio.of(Deal.class)
                .generate(field(Deal::getAmount), generate -> generate.string().minLength(3).maxLength(20))
                .generate(field(Deal::getTitle), generate -> generate.string().minLength(3).maxLength(20))
                .create();
    }

}
