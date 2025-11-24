package app;

import server.HttpServer;
import spring.ApplicationContext;
import spring.web.DispatcherServlet;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ApplicationContext();
        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        HttpServer server = new HttpServer(dispatcherServlet);
        server.start();
    }
}
