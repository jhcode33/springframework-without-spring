package server;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {

    public HttpRequest parse(BufferedReader in) throws Exception {
        HttpRequest request = new HttpRequest();

        // Request Line
        String requestLine = in.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            return request;
        }

        parseRequestLine(requestLine, request);

        // Headers
        Map<String, String> headers = parseHeaders(in);
        headers.forEach(request::addHeader);

        // Body
        parseBody(in, request);

        return request;
    }

    private void parseRequestLine(String requestLine, HttpRequest request) {
        String[] parts = requestLine.split(" ");
        request.setMethod(parts[0]);
        request.setPath(parts[1]);
        request.setProtocol(parts[2]);
    }

    private Map<String, String> parseHeaders(BufferedReader in) throws Exception {
        Map<String, String> headers = new HashMap<>();

        String line = in.readLine();
        while (line != null && !line.isEmpty()) {

            int idx = line.indexOf(":");
            if (idx > 0) {
                String key = line.substring(0, idx).trim();
                String value = line.substring(idx + 1).trim();
                headers.put(key, value);
            }

            line = in.readLine();
        }

        return headers;
    }

    private void parseBody(BufferedReader in, HttpRequest request) throws Exception {
        if (!request.getHeaders().containsKey("Content-Length")) {
            return;
        }

        int contentLength = Integer.parseInt(request.getHeaders().get("Content-Length"));
        char[] buf = new char[contentLength];

        int read = in.read(buf, 0, contentLength);
        if (read > 0) {
            request.setBody(new String(buf, 0, read));
        }
    }
}
