package error;

import server.HttpResponse;

public interface ExceptionResolver {

    HttpResponse resolveException(Exception ex);
}

