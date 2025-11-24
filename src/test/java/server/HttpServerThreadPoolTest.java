package server;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpServerThreadPoolTest {

//    private static Thread serverThread;
//
//    @BeforeAll
//    static void startServer() {
//        serverThread = new Thread(() -> {
//            try {
//                new HttpServer().start();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        serverThread.setDaemon(true);
//        serverThread.start();
//
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException ignored) {}
//    }
//
//    @Test
//    void threadPool_동시요청_다른Thread_사용_확인() throws Exception {
//        int concurrentClients = 5;
//        CountDownLatch latch = new CountDownLatch(concurrentClients);
//        Set<String> threadNames = Collections.synchronizedSet(new HashSet<>());
//
//        for (int i = 0; i < concurrentClients; i++) {
//            new Thread(() -> {
//                try (Socket socket = new Socket("localhost", ServerConfig.DEFAULT_PORT);
//                     OutputStream out = socket.getOutputStream()) {
//
//                    PrintWriter writer = new PrintWriter(out, true);
//                    writer.print("GET /test HTTP/1.1\r\nHost: localhost\r\n\r\n");
//                    writer.flush();
//
//                    threadNames.add(Thread.currentThread().getName());
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    latch.countDown();
//                }
//            }).start();
//        }
//
//        latch.await();
//
//        assertTrue(threadNames.size() >= 1, "동시 요청 처리 Thread가 하나 이상이어야 합니다.");
//    }
}
