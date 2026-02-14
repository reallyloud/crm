package ru.mentee.power.crm.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.mentee.power.crm.domain.DealStatus;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.spring.entity.Company;
import ru.mentee.power.crm.spring.entity.Deal;
import ru.mentee.power.crm.spring.entity.Lead;
import ru.mentee.power.crm.spring.service.JpaDealService;
import ru.mentee.power.crm.spring.service.JpaLeadService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JpaDealController.class)
class JpaDealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JpaDealService dealService;

    @MockitoBean
    private JpaLeadService leadService;

    @Test
    void shouldListDeals() throws Exception {
        Deal deal = createTestDeal();
        when(dealService.getAllDeals()).thenReturn(List.of(deal));

        mockMvc.perform(get("/jpa/deals"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("deals"))
                .andExpect(view().name("jpa/deals/list"));

        verify(dealService).getAllDeals();
    }

    @Test
    void shouldShowKanbanView() throws Exception {
        Deal deal = createTestDeal();
        Map<DealStatus, List<Deal>> kanban = Map.of(DealStatus.NEW, List.of(deal));
        when(dealService.getDealsByStatusForKanban()).thenReturn(kanban);

        mockMvc.perform(get("/jpa/deals/kanban"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dealsByStatus"))
                .andExpect(view().name("jpa/deals/kanban"));

        verify(dealService).getDealsByStatusForKanban();
    }

    @Test
    void shouldShowConvertForm() throws Exception {
        UUID leadId = UUID.randomUUID();
        Lead lead = createTestLead(leadId);
        when(leadService.findById(leadId)).thenReturn(Optional.of(lead));

        mockMvc.perform(get("/jpa/deals/convert/{leadId}", leadId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("lead"))
                .andExpect(view().name("jpa/deals/convertLeadToDeal"));

        verify(leadService).findById(leadId);
    }

    @Test
    void shouldReturn404WhenConvertingNonExistentLead() throws Exception {
        UUID leadId = UUID.randomUUID();
        when(leadService.findById(leadId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/jpa/deals/convert/{leadId}", leadId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldConvertLeadToDealAndRedirect() throws Exception {
        UUID leadId = UUID.randomUUID();
        Deal deal = createTestDeal();
        when(dealService.convertLeadToDeal(eq(leadId), any(BigDecimal.class))).thenReturn(deal);

        mockMvc.perform(post("/jpa/deals/convert")
                        .param("leadId", leadId.toString())
                        .param("amount", "50000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jpa/deals"));

        verify(dealService).convertLeadToDeal(eq(leadId), eq(new BigDecimal("50000")));
    }

    @Test
    void shouldTransitionDealStatusAndRedirect() throws Exception {
        UUID dealId = UUID.randomUUID();
        Deal deal = createTestDeal();
        deal.setId(dealId);
        when(dealService.transitionDealStatus(dealId, DealStatus.QUALIFIED)).thenReturn(deal);

        mockMvc.perform(post("/jpa/deals/{id}/transition", dealId)
                        .param("newStatus", "QUALIFIED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/jpa/deals/kanban"));

        verify(dealService).transitionDealStatus(dealId, DealStatus.QUALIFIED);
    }

    private Deal createTestDeal() {
        Deal deal = new Deal("Test Deal", UUID.randomUUID(), BigDecimal.valueOf(50000), DealStatus.NEW);
        return deal;
    }

    private Lead createTestLead(UUID id) {
        Company company = new Company();
        company.setName("TestCompany");
        Lead lead = new Lead("Тест", "test@test.com", company, LeadStatus.NEW);
        lead.setId(id);
        lead.setPhone("89534895678");
        return lead;
    }
}
