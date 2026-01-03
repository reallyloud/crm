package ru.mentee.power.crm.web;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HelloCrmServer {

    private final HttpServer server;

    public HelloCrmServer(int port) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(port), 0); // создание сервера
    }

    public void start() {
        server.createContext("/hello", new HelloHandler());
        server.start();
        System.out.println("Server started on http://localhost:" + server.getAddress().getPort());
    }

    public void stop() {
        server.stop(0);
    }

    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod(); // логгируем тип запроса
            String path = exchange.getRequestURI().getPath(); // логгируем путь запроса
            System.out.println("Received " + method + " request for " + path);

            String htmlResponse = generateHtmlContent(); // сделали html страницу
            byte[] responseBytes = htmlResponse.getBytes(StandardCharsets.UTF_8); // записали в байты
            exchange.sendResponseHeaders(200, responseBytes.length); // установили код ответа и его размер
            // устанавливаем в заголовке Content-Type тип html и кодировку utf-8
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");


            try (OutputStream os = exchange.getResponseBody()) { // получаем OutputStream из HttpExcange
                os.write(responseBytes); // Записываем байты HTML в поток
            }
            // OutputStream будет автоматически закрыт здесь.
        }

        private String generateHtmlContent() {
            StringBuilder htmlBuilder = new StringBuilder();


            htmlBuilder.append("<!DOCTYPE html>\n");
            htmlBuilder.append("<html>\n");
            htmlBuilder.append("<head>\n");
            htmlBuilder.append("    <meta charset=\"UTF-8\">\n"); // Указываем кодировку
            htmlBuilder.append("    <title>Hello CRM!</title>\n"); // Заголовок вкладки
            htmlBuilder.append("</head>\n");
            htmlBuilder.append("<body>\n");
            htmlBuilder.append("    <h1>Hello CRM!</h1>\n"); // Основной заголовок на странице
            htmlBuilder.append("</body>\n");
            htmlBuilder.append("</html>");

            return htmlBuilder.toString();
        }
    }


}


