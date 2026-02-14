package ru.mentee.power.crm.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import ru.mentee.power.crm.domain.DealStatus;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.service.JpaDealService;
import ru.mentee.power.crm.spring.service.JpaLeadService;

import java.math.BigDecimal;
import java.util.UUID;

@Controller
@RequestMapping("/jpa/deals")
@RequiredArgsConstructor
public class JpaDealController {

    private final JpaDealService dealService;
    private final JpaLeadService leadService;

    @GetMapping
    public String listDeals(Model model) {
        model.addAttribute("deals", dealService.getAllDeals());
        return "jpa/deals/list";
    }

    @GetMapping("/kanban")
    public String kanbanView(Model model) {
        model.addAttribute("dealsByStatus", dealService.getDealsByStatusForKanban());
        return "jpa/deals/kanban";
    }

    @GetMapping("/convert/{leadId}")
    public String showConvertForm(@PathVariable UUID leadId, Model model) {
        Lead lead = leadService.findById(leadId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found"));
        model.addAttribute("lead", lead);
        return "jpa/deals/convertLeadToDeal";
    }

    @PostMapping("/convert")
    public String convertLeadToDeal(@RequestParam UUID leadId, @RequestParam BigDecimal amount) {
        dealService.convertLeadToDeal(leadId, amount);
        return "redirect:/jpa/deals";
    }

    @PostMapping("/{id}/transition")
    public String transitionStatus(@PathVariable UUID id, @RequestParam DealStatus newStatus) {
        dealService.transitionDealStatus(id, newStatus);
        return "redirect:/jpa/deals/kanban";
    }
}
