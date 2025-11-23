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
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (Socket socket = clientSocket;
             BufferedReader in = new BufferedReader(new InputStreamReader(
                     socket.getInputStream(),
                     ServerConfig.DEFAULT_CHARSET
             ));
             OutputStream out = socket.getOutputStream()) {

            String line = in.readLine();
            if (line == null || line.isEmpty()) {
                return;
            }

            OutputView.printRequestLine(line);

            while (!line.isEmpty()) {
                line = in.readLine();
                OutputView.printHeaderLine(line);
            }

            byte[] bodyBytes = ServerConfig.DEFAULT_BODY.getBytes(ServerConfig.DEFAULT_CHARSET);

            String response = ServerConfig.HTTP_STATUS_200 +
                    ServerConfig.HEADER_CONTENT_TYPE +
                    ServerConfig.HEADER_CONTENT_LENGTH + bodyBytes.length + ServerConfig.CRLF +
                    ServerConfig.HEADER_CONNECTION_CLOSE +
                    ServerConfig.CRLF +
                    ServerConfig.DEFAULT_BODY;

            out.write(response.getBytes(ServerConfig.DEFAULT_CHARSET));
            out.flush();

        } catch (Exception e) {
            OutputView.printClientError(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        HttpServer server = new HttpServer();
        server.start();
    }
}
