package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.OutputView;
import spring.web.DispatcherServlet;

/**
 * 간단한 HTTP 서버
 * - 멀티스레드 처리
 * - DispatcherServlet과 연동하여 동적 요청 처리
 */
public class HttpServer {

    private final HttpRequestParser parser = new HttpRequestParser();
    private final DispatcherServlet dispatcherServlet;
    private final ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2
    );

    /**
     * DispatcherServlet과 연동하는 생성자
     * @param dispatcherServlet 요청 처리를 위임할 DispatcherServlet
     */
    public HttpServer(DispatcherServlet dispatcherServlet) {
        this.dispatcherServlet = dispatcherServlet;
    }

    /**
     * 서버 기동
     * @throws Exception
     */
    public void start() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(ServerConfig.DEFAULT_PORT)) {
            OutputView.printServerStart(ServerConfig.DEFAULT_PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(() -> handleClient(clientSocket));
            }
        }
    }

    /**
     * 클라이언트 요청 처리
     */
    private void handleClient(Socket clientSocket) {
        try (Socket socket = clientSocket;
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), ServerConfig.DEFAULT_CHARSET)
             );
             OutputStream out = socket.getOutputStream()) {

            HttpRequest request = parser.parse(in);

            HttpResponse response = dispatcherServlet.service(request);

            sendResponse(out, response);

        } catch (Exception e) {
            OutputView.printClientError(e.getMessage());
        }
    }

    /**
     * HTTP 응답 전송
     */
    private void sendResponse(OutputStream out, HttpResponse response) throws Exception {
        String raw = response.toRawResponse();
        out.write(raw.getBytes(ServerConfig.DEFAULT_CHARSET));
        out.flush();
    }
}
