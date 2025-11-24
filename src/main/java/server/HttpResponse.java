package server;

public class HttpResponse {

    private final int status;
    private final String headers;
    private final String body;

    private HttpResponse(Builder builder) {
        this.status = builder.status;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public int getStatus() {
        return status;
    }

    public String toRawResponse() {
        String statusLine = "HTTP/1.1 " + status + "\r\n";
        return statusLine + headers + "\r\n" + body;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int status = 0;
        private String headers = "";
        private String body = "";

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder headers(String headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            if (status == 0) {
                throw new IllegalStateException("HTTP status must be set");
            }
            return new HttpResponse(this);
        }
    }
}

