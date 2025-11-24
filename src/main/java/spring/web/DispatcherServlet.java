package spring.web;

import server.HttpStatus;
import server.HttpRequest;
import server.HttpResponse;
import server.ServerConfig;
import spring.ApplicationContext;

public class DispatcherServlet {

    private final HandlerMapping handlerMapping;
    private final HandlerAdapter handlerAdapter = new HandlerAdapter();

    public DispatcherServlet(ApplicationContext applicationContext) {
        this.handlerMapping = new HandlerMapping(applicationContext.getAllBeans());
    }

    public HttpResponse service(HttpRequest request) {

        String path = request.getPath();

        HandlerMethod handlerMethod = handlerMapping.getHandler(path);

        // 핸들러가 없는 경우 → 404 응답
        if (handlerMethod == null) {
            return HttpResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .headers(ServerConfig.HEADER_CONTENT_TYPE)
                    .body(ServerConfig.DEFAULT_BODY)
                    .build();
        }

        // 핸들러 실행
        Object result = handlerAdapter.handle(handlerMethod);

        // 정상 응답
        return HttpResponse.builder()
                .status(HttpStatus.OK)
                .headers(ServerConfig.HEADER_CONTENT_TYPE)
                .body(ServerConfig.DEFAULT_BODY)
                .build();
    }
}
