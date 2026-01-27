package ru.mentee.power.crm.spring;

import org.springframework.context.ConfigurableApplicationContext;

public class StartSpringServer {
    static void main() {
        Application springServer = new Application();
        ConfigurableApplicationContext context = springServer.start();


        System.out.println("старт");
        TestSql testSql = context.getBean(TestSql.class);
        testSql.start();


    }

}
