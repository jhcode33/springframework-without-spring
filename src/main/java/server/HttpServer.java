package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import io.OutputView;

public class HttpServer {

    private final HttpRequestParser parser = new HttpRequestParser();

    public void start() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(ServerConfig.DEFAULT_PORT)) {

            OutputView.printServerStart(ServerConfig.DEFAULT_PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                handleClient(socket);
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (Socket socket = clientSocket;
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), ServerConfig.DEFAULT_CHARSET)
             );
             OutputStream out = socket.getOutputStream()) {

            HttpRequest request = parser.parse(in);

            HttpResponse response = createResponse(request);

            sendResponse(out, response);

        } catch (Exception e) {
            OutputView.printClientError(e.getMessage());
        }
    }

    private HttpResponse createResponse(HttpRequest request) throws Exception {
        byte[] bodyBytes = ServerConfig.DEFAULT_BODY.getBytes(ServerConfig.DEFAULT_CHARSET);

        String headers =
                ServerConfig.HEADER_CONTENT_TYPE +
                        ServerConfig.HEADER_CONTENT_LENGTH + bodyBytes.length + ServerConfig.CRLF +
                        ServerConfig.HEADER_CONNECTION_CLOSE;

        return new HttpResponse(
                ServerConfig.HTTP_STATUS_200,
                headers,
                ServerConfig.DEFAULT_BODY
        );
    }

    private void sendResponse(OutputStream out, HttpResponse response) throws Exception {
        String raw = response.toRawResponse();
        out.write(raw.getBytes(ServerConfig.DEFAULT_CHARSET));
        out.flush();
    }

    public static void main(String[] args) throws Exception {
        new HttpServer().start();
    }
}
