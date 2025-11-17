package io;

public class OutputView {

    public static final String SERVER_START = "HTTP 서버가 포트 %d 에서 시작되었습니다.";
    public static final String REQUEST_LINE = "요청 라인: %s";
    public static final String HEADER_LINE = "헤더: %s";
    public static final String CLIENT_ERROR = "클라이언트 처리 중 오류 발생: %s";

    public static void printServerStart(int port) {
        System.out.printf(SERVER_START + "%n", port);
    }

    public static void printRequestLine(String line) {
        System.out.printf(REQUEST_LINE + "%n", line);
    }

    public static void printHeaderLine(String line) {
        System.out.printf(HEADER_LINE + "%n", line);
    }

    public static void printClientError(String message) {
        System.err.printf(CLIENT_ERROR + "%n", message);
    }
}
