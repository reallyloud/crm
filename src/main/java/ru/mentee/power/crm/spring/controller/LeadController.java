package ru.mentee.power.crm.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.service.LeadService;

import java.util.List;

@Controller
public class LeadController {

    private final LeadService leadService;


    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @GetMapping("/leads")
    public String showLeads(Model model) {
        leadService.addLead("maximalen1999@gmail.com", "yandex", LeadStatus.QUALIFIED); // Положили туда лидов
        leadService.addLead("oleg@gmail.com", "google", LeadStatus.NEW);
        leadService.addLead("gennadiy@gmail.com", "lada", LeadStatus.NEW);
        leadService.addLead("auto@gmail.com", "granta", LeadStatus.NEW);
        leadService.addLead("bilbobeggins@gmail.com", "hobbit", LeadStatus.NEW);
        List<Lead> leadList = leadService.findAll();

        model.addAttribute("leads",leadList);
        return "leads/list";
    }
}
