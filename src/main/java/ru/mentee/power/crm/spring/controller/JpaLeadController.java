package ru.mentee.power.crm.spring.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.entity.Company;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.service.JpaLeadService;

@Controller
@RequestMapping("/jpa/leads")
@RequiredArgsConstructor
public class JpaLeadController {

  private final JpaLeadService leadService;

  @GetMapping
  public String listLeads(
      @RequestParam(required = false) String search,
      @RequestParam(required = false) String status,
      Model model) {
    List<Lead> leads = leadService.findLeads(search, status);
    model.addAttribute("search", search != null ? search : "");
    model.addAttribute("status", status);
    model.addAttribute("leads", leads);
    return "jpa/leads/list";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("lead", new LeadForm("", "", "", "", LeadStatus.NEW));
    return "jpa/leads/create";
  }

  @PostMapping
  public String createLead(
      @Valid @ModelAttribute("lead") LeadForm form, BindingResult result, Model model) {
    if (result.hasErrors()) {
      model.addAttribute("lead", form);
      model.addAttribute("errors", result);
      return "jpa/leads/createForm";
    }

    Company company = new Company();
    company.setName(form.companyName());
    Lead lead = new Lead(form.name(), form.email(), company, form.status());
    lead.setPhone(form.phone());
    leadService.save(lead);
    return "redirect:/jpa/leads";
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable UUID id, Model model) {
    Lead lead =
        leadService
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found"));
    LeadForm form =
        new LeadForm(
            lead.getName(),
            lead.getEmail(),
            lead.getPhone(),
            lead.getCompany() != null ? lead.getCompany().getName() : "",
            lead.getStatus());
    model.addAttribute("lead", form);
    model.addAttribute("leadId", id);
    return "jpa/leads/edit";
  }

  @PostMapping("/{id}")
  public String updateLead(
      @PathVariable UUID id,
      @Valid @ModelAttribute("lead") LeadForm form,
      BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("lead", form);
      model.addAttribute("leadErrors", result);
      model.addAttribute("leadId", id);
      return "jpa/leads/edit";
    }
    leadService.update(
        id, form.name(), form.email(), form.phone(), form.companyName(), form.status());
    return "redirect:/jpa/leads";
  }

  @PostMapping("/{id}/delete")
  public String deleteLead(@PathVariable UUID id) {
    leadService.delete(id);
    return "redirect:/jpa/leads";
  }
}
