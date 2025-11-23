package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import io.OutputView;

public class HttpServer {

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
                     new InputStreamReader(socket.getInputStream(), ServerConfig.DEFAULT_CHARSET));
             OutputStream out = socket.getOutputStream()) {

            HttpRequest request = receiveRequest(in);

            HttpResponse response = createResponse();

            sendResponse(out, response);

        } catch (Exception e) {
            OutputView.printClientError(e.getMessage());
        }
    }

    private HttpRequest receiveRequest(BufferedReader in) throws Exception {
        HttpRequest request = new HttpRequest();

        String requestLine = in.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            return request;
        }

        OutputView.printRequestLine(requestLine);

        String[] parts = requestLine.split(" ");
        request.setMethod(parts[0]);
        request.setPath(parts[1]);
        request.setVersion(parts[2]);

        String line = in.readLine();
        while (line != null && !line.isEmpty()) {

            OutputView.printHeaderLine(line);

            int idx = line.indexOf(ServerConfig.HEADER_SEPARATOR);
            if (idx != -1) {
                String key = line.substring(0, idx).trim();
                String value = line.substring(idx + ServerConfig.HEADER_SEPARATOR.length()).trim();
                request.addHeader(key, value);
            }

            line = in.readLine();
        }

        return request;
    }

    private HttpResponse createResponse() throws Exception {
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
