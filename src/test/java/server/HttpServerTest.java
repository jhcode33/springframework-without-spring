package server;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpServerTest {

    private static Thread serverThread;

    @BeforeAll
    static void setUp() {
        serverThread = new Thread(() -> {
            try {
                new HttpServer().start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        serverThread.setDaemon(true);
        serverThread.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }
    }

    @AfterAll
    static void tearDown() {
        serverThread.interrupt();
    }

    @Test
    void GET_요청시_200_OK_만_확인() throws Exception {
        try (Socket socket = new Socket("localhost", ServerConfig.DEFAULT_PORT);
             OutputStream out = socket.getOutputStream();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String request = "GET / HTTP/1.1\r\n" +
                            "Host: localhost\r\n" +
                            "\r\n";

            out.write(request.getBytes(ServerConfig.DEFAULT_CHARSET));
            out.flush();

            String statusLine = in.readLine();

            assertEquals("HTTP/1.1 200 OK", statusLine);
        }
    }

}
