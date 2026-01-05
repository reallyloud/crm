package ru.mentee.power.crm.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeadListServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletContext servletContext;

    @Mock
    private LeadService leadService;

    private LeadListServlet servlet;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws IOException {
        servlet = spy(new LeadListServlet());
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        doReturn(servletContext).when(servlet).getServletContext();
        when(servletContext.getAttribute("leadService")).thenReturn(leadService);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void shouldReturnHtmlTable_whenDoGetCalled() throws Exception {
        // Given: создаем тестовые данные
        Lead lead1 = createTestLead("test1@example.com", "Company1", LeadStatus.NEW);
        Lead lead2 = createTestLead("test2@example.com", "Company2", LeadStatus.CONTACTED);
        List<Lead> leads = List.of(lead1, lead2);

        when(leadService.findAll()).thenReturn(leads);

        // When: вызываем doGet
        servlet.doGet(request, response);

        // Then: проверяем что leadService.findAll() был вызван
        verify(leadService, times(1)).findAll();

        // Then: проверяем что был сгенерирован HTML с таблицей
        String htmlContent = stringWriter.toString();
        assertThat(htmlContent).contains("<!DOCTYPE html>");
        assertThat(htmlContent).contains("<html>");
        assertThat(htmlContent).contains("<table border='1'>");
        assertThat(htmlContent).contains("<th>Email</th>");
        assertThat(htmlContent).contains("<th>Company</th>");
        assertThat(htmlContent).contains("<th>Status</th>");
        assertThat(htmlContent).contains("test1@example.com");
        assertThat(htmlContent).contains("Company1");
        assertThat(htmlContent).contains("NEW");
        assertThat(htmlContent).contains("test2@example.com");
        assertThat(htmlContent).contains("Company2");
        assertThat(htmlContent).contains("CONTACTED");
    }

    @Test
    void shouldSetContentTypeToHtml_whenDoGetCalled() throws Exception {
        // Given
        when(leadService.findAll()).thenReturn(List.of());

        // When: вызываем doGet
        servlet.doGet(request, response);

        // Then: проверяем что setContentType был вызван с правильным параметром
        verify(response, times(1)).setContentType("text/html; charset=UTF-8");
    }

    private Lead createTestLead(String email, String company, LeadStatus status) {
        Address address = new Address("Moscow", "Lubyanka", "zip");
        Contact contact = new Contact(email, "899436964", address);
        return new Lead(UUID.randomUUID(), contact, company, status);
    }
}

