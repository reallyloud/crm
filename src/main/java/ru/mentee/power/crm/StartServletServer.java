package ru.mentee.power.crm;

public class StartServletServer {
  static void main() throws Exception {
    ServletServer server = new ServletServer();
    server.startAndAwait();
  }
}
