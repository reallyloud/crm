package ru.mentee.power.crm;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.servlet.LeadListServlet;
import ru.mentee.power.crm.spring.Application;
import ru.mentee.power.crm.spring.repository.InMemoryLeadRepository;
import ru.mentee.power.crm.spring.repository.LeadRepository;
import ru.mentee.power.crm.spring.service.LeadService;
import ru.mentee.power.crm.testHelpClasses.PerformanceTestApplication;

import java.io.File;
import java.net.http.*;
import java.net.URI;

import static org.assertj.core.api.Assertions.*;

/**
 * Интеграционный тест сравнения Servlet и Spring Boot стеков.
 * Запускает оба сервера, выполняет HTTP запросы, сравнивает результаты.
 */
class StackComparisonTest {

    private static final int SERVLET_PORT = 8080;
    private static final int SPRING_PORT = 8081;

    private HttpClient httpClient;

    @BeforeEach
    void setUp() throws LifecycleException {
        httpClient = HttpClient.newHttpClient();
    }


    @Test
    @DisplayName("Оба стека должны возвращать лидов в HTML таблице")
    void shouldReturnLeadsFromBothStacks() throws Exception {

        // Given: HTTP запросы к обоим стекам и открываем серверы
        HttpRequest servletRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + SERVLET_PORT + "/leads"))
                .GET()
                .build();

        HttpRequest springRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + SPRING_PORT + "/leads"))
                .GET()
                .build();

        // When: выполняем запросы
        HttpResponse<String> servletResponse = httpClient.send(
                servletRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> springResponse = httpClient.send(
                springRequest, HttpResponse.BodyHandlers.ofString());

        // Then: оба возвращают 200 OK и содержат таблицу
        assertThat(servletResponse.statusCode()).isEqualTo(200);
        assertThat(springResponse.statusCode()).isEqualTo(200);

        assertThat(servletResponse.body()).contains("<table");
        assertThat(springResponse.body()).contains("<table");


        assertThat(countTableRows(servletResponse.body(), "<tr>"))
                .isEqualTo(countTableRows(springResponse.body(), "<tr>"));
    }

    public static int countTableRows(String text, String substring) {
        if (text == null || text.isEmpty() || substring == null || substring.isEmpty()) {
            return 0;
        }
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length(); // Учитываем непересекающиеся вхождения
        }
        return count;
    }

    @Test
    @DisplayName("Измерение времени старта обоих стеков")
    void shouldMeasureStartupTime() throws LifecycleException {
        // Servlet startup time (уже запущен вручную)
        long servletStartupMs = measureServletStartup();

        // Spring Boot startup time (уже запущен вручную)
        long springStartupMs = measureSpringBootStartup();

        // Вывод результатов
        System.out.println("=== Сравнение времени старта ===");
        System.out.printf("Servlet стек: %d ms%n", servletStartupMs);
        System.out.printf("Spring Boot: %d ms%n", springStartupMs);
        System.out.printf("Разница: Spring %s на %d ms%n",
                springStartupMs > servletStartupMs ? "медленнее" : "быстрее",
                Math.abs(springStartupMs - servletStartupMs));

        // Просто фиксируем что оба стартуют за разумное время
        assertThat(servletStartupMs).isLessThan(10_000);
        assertThat(springStartupMs).isLessThan(15_000);
    }

    private long measureServletStartup() throws LifecycleException {
        long startTime = System.nanoTime();
        Tomcat tomcat = new Tomcat();
        tomcat.getServer().setPort(8084);
        tomcat.getServer().start();
        long startupTime = (System.nanoTime() - startTime) / 1_000_000L;
        tomcat.getServer().stop();

        return startupTime;
    }

    private long measureSpringBootStartup() {
        long startTime = System.nanoTime();

        ConfigurableApplicationContext context =
                new SpringApplicationBuilder(PerformanceTestApplication.class)
                        .run("--server.port=8083");

        long startupTime = (System.nanoTime() - startTime) / 1_000_000L;
        context.close();

        return startupTime;
    }
}



