package ru.mentee.power.crm.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.repository.InMemoryLeadRepository;
import ru.mentee.power.crm.spring.service.LeadService;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;
    private boolean isInitialized;

    @GetMapping("/leads")
    public String showLeads(@RequestParam(required = false) LeadStatus status, Model model) {
        initLeadService();
        List<Lead> leadList = leadService.findAll();
        if (status != null) {
            leadList = leadService.findByStatus(status);
        }

        // Кладём атрибут leads в jte:
        model.addAttribute("currentFilter", status);
        model.addAttribute("leads", leadList);
        // Возвращаем адрес этого jte файла, в который мы кладем leadList:
        return "leads/list";
    }

    @PostMapping("/leads/{id}")
    public String updateLead(@PathVariable String id, @ModelAttribute Lead lead) {
        leadService.update(id,lead);
        return "redirect:/leads";
    }


    @GetMapping("/leads/new")
    public String showCreateForm(Model model) {
        model.addAttribute("lead", new Lead(null, "", "", LeadStatus.NEW));
        return "leads/create"; // JTE шаблон leads/create.jte
    }

    @GetMapping("/leads/{id}/edit")
    public String showEditForm(@PathVariable UUID id, Model model) {
        if (leadService.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found");
        }
        Lead lead = leadService.findById(id).get();
        model.addAttribute("lead", lead);
        return "spring/edit";
    }


    @PostMapping("/leads")
    public String createLead(@ModelAttribute Lead lead) {
        initLeadService();

        leadService.addLead(lead.email(), lead.company(), lead.status());

        return "redirect:/leads";
    }

    private void initLeadService() {
        if (isInitialized) {
            return;
        } else
            isInitialized = true;

        leadService.addLead("maximalen1999@gmail.com", "yandex", LeadStatus.QUALIFIED); // Положили туда лидов
        leadService.addLead("oleg@gmail.com", "google", LeadStatus.NEW);
        leadService.addLead("gennadiy@gmail.com", "lada", LeadStatus.CONTACTED);
        leadService.addLead("auto@gmail.com", "granta", LeadStatus.NEW);
        leadService.addLead("bilbobeggins@gmail.com", "hobbit", LeadStatus.NEW);
        leadService.addLead("fdsfdsg@gmail.com", "yandex", LeadStatus.DONE);
    }

    public LeadService getService() {
        return this.leadService;
    }
}
