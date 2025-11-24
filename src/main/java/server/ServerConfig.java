package server;

public final class ServerConfig {

    private ServerConfig() {}

    // Server Settings
    public static final int DEFAULT_PORT = 8080;
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String DEFAULT_VIEW_PATH = "views/";
    public static final String DEFAULT_VIEW_SUFFIX = ".html";

    // HTTP Headers
    public static final String HEADER_CONTENT_TYPE = "Content-Type: text/html; charset=UTF-8\r\n";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length: ";
    public static final String HEADER_CONNECTION_CLOSE = "Connection: close\r\n";

    // Common
    public static final String CRLF = "\r\n";

    // Default Body
    public static final String DEFAULT_BODY =
            "<html><body><h1>Hello, SimpleHttpServer</h1></body></html>";
}
