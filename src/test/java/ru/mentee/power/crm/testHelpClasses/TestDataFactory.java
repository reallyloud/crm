package ru.mentee.power.crm.testHelpClasses;

import org.instancio.Instancio;
import org.instancio.Model;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.entity.Lead;

import java.time.LocalDateTime;

import static org.instancio.Select.field;


public class TestDataFactory {

    public static Model<Lead> generateRandomLead (){
        return Instancio.of(Lead.class)
                .ignore(field(Lead::getId))
                .ignore(field(Lead::getCreatedAt))
                .generate(field(Lead::getName), generate -> generate.string().minLength(2).maxLength(40))
                .generate(field(Lead::getCompany), generate -> generate.string().minLength(2).maxLength(40))
                .generate(field(Lead::getEmail), generate -> generate.string().minLength(2).maxLength(40))
                .generate(field(Lead::getPhone), generate -> generate.string().minLength(2).maxLength(40))
                .generate(field(Lead::getStatus), generate -> generate.enumOf(LeadStatus.class))
                .toModel();
    }

}
