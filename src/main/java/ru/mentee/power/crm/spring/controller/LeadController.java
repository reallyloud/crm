package ru.mentee.power.crm.spring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.repository.InMemoryLeadRepository;
import ru.mentee.power.crm.spring.service.LeadService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;
    private boolean isInitialized;

    @GetMapping("/leads")
    public String listLeads(@RequestParam(required = false) String search, @RequestParam(required = false) String status, Model model) {
        initLeadService();
        List<Lead> leads = leadService.findLeads(search, status);
        model.addAttribute("search", search != null ? search : "");
        model.addAttribute("leads", leads);

        return "leads/list";
    }

    @PostMapping("/leads/{id}")
    public String updateLead(@PathVariable String id, @Valid @ModelAttribute Lead lead, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("lead", lead);
            model.addAttribute("leadErrors", result);
            return "leads/form";
        }
        leadService.update(id, lead);
        return "redirect:/leads";
    }

    @PostMapping("/leads/{id}/delete")
    public String deleteLead(@PathVariable UUID id) {
        leadService.delete(id);
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
    public String createLead(@Valid @ModelAttribute Lead lead, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("lead", lead);
            model.addAttribute("errors", result);

            return "leads/createForm";
        }
        initLeadService();

        leadService.addLead(lead);

        return "redirect:/leads";
    }

    private void initLeadService() {
        if (isInitialized) {
            return;
        } else isInitialized = true;

        Lead lead1 = new Lead(UUID.randomUUID(), new Contact("", "", new Address("", "", ""))
                , "yandex", LeadStatus.QUALIFIED, "maximalen1999@gmail.com", "895348956", "Максим", LocalDateTime.now());
        Lead lead2 = new Lead(UUID.randomUUID(), new Contact("", "", new Address("", "", ""))
                , "google", LeadStatus.NEW, "oleg@gmail.com", "896431531", "Олег", LocalDateTime.now());
        Lead lead3 = new Lead(UUID.randomUUID(), new Contact("", "", new Address("", "", ""))
                , "lada", LeadStatus.CONTACTED, "gennadiy@gmail.com", "85489675865", "Геннадий", LocalDateTime.now());
        Lead lead4 = new Lead(UUID.randomUUID(), new Contact("", "", new Address("", "", ""))
                , "granta", LeadStatus.NEW, "auto@gmail.com", "895348956", "Сергей", LocalDateTime.now());
        Lead lead5 = new Lead(UUID.randomUUID(), new Contact("", "", new Address("", "", ""))
                , "hobbit", LeadStatus.NEW, "bilbobeggins@gmail.com", "844585676", "Михаил", LocalDateTime.now());
        Lead lead6 = new Lead(UUID.randomUUID(), new Contact("", "", new Address("", "", ""))
                , "yandex", LeadStatus.DONE, "fdsfdsg@gmail.com", "878654826", "Григорий", LocalDateTime.now());
        leadService.addLead(lead1); // Положили туда лидов
        leadService.addLead(lead2); // Положили туда лидов
        leadService.addLead(lead3); // Положили туда лидов
        leadService.addLead(lead4); // Положили туда лидов
        leadService.addLead(lead5); // Положили туда лидов
        leadService.addLead(lead6); // Положили туда лидов

    }

    public LeadService getService() {
        return this.leadService;
    }
}
