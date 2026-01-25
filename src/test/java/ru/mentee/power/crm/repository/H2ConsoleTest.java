package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.spring.Application;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class H2ConsoleTest {

    @LocalServerPort
    private int port;

    @Test
    void h2ConsoleShouldBeAccessible() throws Exception {
        // Given
        URL url = new URL("http://localhost:" + port + "/h2-console");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        // When
        int responseCode = connection.getResponseCode();

        // Then
        assertThat(responseCode).isEqualTo(200);
        connection.disconnect();
    }
}
