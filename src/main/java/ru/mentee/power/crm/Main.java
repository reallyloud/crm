package ru.mentee.power.crm;

import jakarta.servlet.ServletContext;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.InMemoryLeadRepository;
import ru.mentee.power.crm.repository.LeadRepository;
import ru.mentee.power.crm.service.LeadService;
import ru.mentee.power.crm.servlet.LeadListServlet;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        LeadRepository<Lead> repository = new InMemoryLeadRepository(); // Создали репозиторий лидов
        LeadService leadService = new LeadService(repository); // Обернули его в бизнес логику лидов

        leadService.addLead("maximalen1999@gmail.com", "yandex", LeadStatus.QUALIFIED); // Положили туда лидов
        leadService.addLead("oleg@gmail.com", "google", LeadStatus.NEW);
        leadService.addLead("gennadiy@gmail.com", "lada", LeadStatus.NEW);
        leadService.addLead("auto@gmail.com", "granta", LeadStatus.NEW);
        leadService.addLead("bilbobeggins@gmail.com", "hobbit", LeadStatus.NEW);

        Tomcat tomcat = new Tomcat(); // создали томкат для управления сервлетами
        tomcat.setPort(8080);  // задали порт
        Context context = tomcat.addContext("", new File("").getAbsolutePath());
        // Создаем контекст на путь, заданный первым параметром. Во втором папка на диске, где хранится корневая директория проекта

        context.getServletContext().setAttribute("leadService", leadService); //Добавляем корневой контекст приложения
        tomcat.addServlet(context, "LeadListServlet", new LeadListServlet()); //
        context.addServletMappingDecoded("/leads", "LeadListServlet");
        tomcat.getConnector();
        System.out.println(context.getServletContext().getAttribute("leadService"));


        tomcat.start();
        System.out.println("Tomcat started on port 8080");
        System.out.println("Open http://localhost:8080/leads in browser");
        tomcat.getServer().await();
    }
}