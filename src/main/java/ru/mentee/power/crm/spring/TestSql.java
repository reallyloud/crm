package ru.mentee.power.crm.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.repository.JpaLeadRepository;

@Component
public class TestSql {

    @Autowired
    JpaLeadRepository repository;

    public void start() {
        Lead lead = new Lead();
        lead.setName("Олег 2");
        lead.setEmail("test2@example.com");
        lead.setCompany("ACME");
        lead.setStatus(LeadStatus.NEW);
        lead.setPhone("+1234567890");

        repository.save(lead);
        System.out.println("Сейвнуло лид");
    }
}
