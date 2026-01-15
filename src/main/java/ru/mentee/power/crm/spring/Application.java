package ru.mentee.power.crm.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
    private ConfigurableApplicationContext context;
    public ConfigurableApplicationContext start() {
        context = SpringApplication.run(Application.class);
        return context;
    }

    public void stop() {
        if(context != null) {
            context.close();
        }
    }
}
