package ru.mentee.power.crm;

import java.io.File;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.startup.Tomcat;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.servlet.LeadListServlet;
import ru.mentee.power.crm.spring.repository.InMemoryLeadRepository;
import ru.mentee.power.crm.spring.repository.LeadRepository;
import ru.mentee.power.crm.spring.service.LeadService;

public class ServletServer {
  private Tomcat tomcat = new Tomcat();

  private void startServer() throws Exception {
    LeadRepository<Lead> repository = new InMemoryLeadRepository(); // Создали репозиторий лидов
    LeadService leadService = new LeadService(repository); // Обернули его в бизнес логику лидов
    leadService.addLead(
        "maximalen1999@gmail.com", "yandex", LeadStatus.QUALIFIED); // Положили туда лидов
    leadService.addLead("oleg@gmail.com", "google", LeadStatus.NEW);
    leadService.addLead("gennadiy@gmail.com", "lada", LeadStatus.NEW);
    leadService.addLead("auto@gmail.com", "granta", LeadStatus.NEW);
    leadService.addLead("bilbobeggins@gmail.com", "hobbit", LeadStatus.NEW);

    tomcat = new Tomcat(); // создали томкат для управления сервлетами
    tomcat.setPort(8080); // задали порт
    Context context = tomcat.addContext("", new File("").getAbsolutePath());
    // Создаем контекст на путь, заданный первым параметром. Во втором папка на диске, где хранится
    // корневая директория проекта

    context
        .getServletContext()
        .setAttribute("leadService", leadService); // Добавляем корневой контекст приложения
    Tomcat.addServlet(context, "LeadListServlet", new LeadListServlet()); //
    context.addServletMappingDecoded("/leads", "LeadListServlet");
    tomcat.getConnector();

    tomcat.start();
    System.out.println("Tomcat started on port " + tomcat.getServer().getPort());
    System.out.println("Open http://localhost:8080/leads in browser");
  }

  public void start() throws Exception {
    startServer();
  }

  public void stop() throws LifecycleException {
    if (tomcat != null && tomcat.getServer() != null) {
      Server server = tomcat.getServer();
      try {
        // Останавливаем Server (это остановит все сервисы и коннекторы)
        server.stop();
        // Уничтожаем Server
        server.destroy();
      } catch (LifecycleException e) {
        // Если сервер уже остановлен, игнорируем исключение
        if (!e.getMessage().contains("already")) {
          throw e;
        }
      } finally {
        // Освобождаем ресурсы Tomcat
        tomcat.destroy();
      }
    }
  }

  public Tomcat getTomcat() {
    return this.tomcat;
  }

  public void startAndAwait() throws Exception {
    startServer();
    tomcat.getServer().await();
  }
}
