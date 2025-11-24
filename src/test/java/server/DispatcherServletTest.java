package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spring.ApplicationContext;

import spring.annotation.Controller;
import spring.annotation.GetMapping;
import spring.web.DispatcherServlet;

import static org.junit.jupiter.api.Assertions.*;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        ApplicationContext context = new ApplicationContext();
        context.registerBean("testController", new TestController());
        dispatcherServlet = new DispatcherServlet(context);
    }

    @Test
    void service_ReturnsOk_WhenHandlerExists() {
        HttpRequest request = new HttpRequest();
        request.setPath("/hello");

        HttpResponse response = dispatcherServlet.service(request);

        assertEquals(200, response.getStatus());
    }

    @Test
    void service_ReturnsNotFound_WhenHandlerMissing() {
        HttpRequest request = new HttpRequest();
        request.setPath("/nonexistent");

        HttpResponse response = dispatcherServlet.service(request);

        assertEquals(404, response.getStatus());
    }

    @Controller
    public static class TestController {
        @GetMapping("/hello")
        public String hello() {
            return "Hello from TestController";
        }
    }
}
