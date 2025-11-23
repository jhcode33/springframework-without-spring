package server;

public class HttpResponse {

    private String statusLine;
    private String headers;
    private String body;

    public HttpResponse(String statusLine, String headers, String body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public String toRawResponse() {
        return statusLine + headers + "\r\n" + body;
    }
}
