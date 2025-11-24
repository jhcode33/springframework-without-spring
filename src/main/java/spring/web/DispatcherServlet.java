package spring.web;

import error.DefaultExceptionResolver;
import error.ExceptionResolver;
import server.HttpRequest;
import server.HttpResponse;
import server.HttpStatus;
import server.ServerConfig;
import spring.ApplicationContext;

public class DispatcherServlet {

    private final HandlerMapping handlerMapping;
    private final HandlerAdapter handlerAdapter = new HandlerAdapter();
    private final ExceptionResolver exceptionResolver = new DefaultExceptionResolver();
    private final ViewResolver viewResolver = new ViewResolver(ServerConfig.DEFAULT_VIEW_PATH, ServerConfig.DEFAULT_VIEW_SUFFIX);

    public DispatcherServlet(ApplicationContext applicationContext) {
        this.handlerMapping = new HandlerMapping(applicationContext.getAllBeans());
    }

    public HttpResponse service(HttpRequest request) {
        try {
            String path = request.getPath();
            HandlerMethod handlerMethod = handlerMapping.getHandler(path);

            if (handlerMethod == null) {
                return HttpResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .headers(ServerConfig.HEADER_CONTENT_TYPE)
                        .body(ServerConfig.DEFAULT_BODY)
                        .build();
            }

            Object result = handlerAdapter.handle(handlerMethod);

            if (result instanceof String) {
                String html = viewResolver.resolveView((String) result, new Model());
                return HttpResponse.builder()
                        .status(HttpStatus.OK)
                        .headers(ServerConfig.HEADER_CONTENT_TYPE)
                        .body(html)
                        .build();

            } else if (result instanceof ModelAndView) {
                ModelAndView mv = (ModelAndView) result;
                String html = viewResolver.resolveView(mv.getViewName(), mv.getModel());
                return HttpResponse.builder()
                        .status(HttpStatus.OK)
                        .headers(ServerConfig.HEADER_CONTENT_TYPE)
                        .body(html)
                        .build();
            }

            return HttpResponse.builder()
                    .status(HttpStatus.OK)
                    .headers(ServerConfig.HEADER_CONTENT_TYPE)
                    .body(result.toString())
                    .build();

        } catch (Exception e) {
            return exceptionResolver.resolveException(e);
        }
    }
}
