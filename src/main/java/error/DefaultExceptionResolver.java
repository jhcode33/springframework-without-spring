package error;

import server.HttpResponse;
import server.HttpStatus;
import server.ServerConfig;

public class DefaultExceptionResolver implements ExceptionResolver {

    @Override
    public HttpResponse resolveException(Exception ex) {
        StringBuilder body = new StringBuilder();
        body.append("<html><head><meta charset='UTF-8'><title>Error</title></head><body>");
        body.append("<h1>500 Internal Server Error</h1>");
        body.append("<p>").append(ex.getMessage()).append("</p>");
        body.append("</body></html>");

        return HttpResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .headers(ServerConfig.HEADER_CONTENT_TYPE)
                .body(body.toString())
                .build();
    }
}
