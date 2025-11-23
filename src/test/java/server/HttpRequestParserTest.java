package server;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HttpRequestParserTest {

    @Test
    void parse_GET_요청_정상_테스트() throws Exception {
        String rawRequest =
                "GET /hello HTTP/1.1\r\n" +
                        "Host: localhost\r\n" +
                        "User-Agent: JUnitTest\r\n" +
                        "\r\n";

        BufferedReader reader = new BufferedReader(new StringReader(rawRequest));
        HttpRequestParser parser = new HttpRequestParser();

        HttpRequest request = parser.parse(reader);

        assertEquals("GET", request.getMethod());
        assertEquals("/hello", request.getPath());
        assertEquals("HTTP/1.1", request.getProtocol());

        assertEquals("localhost", request.getHeaders().get("Host"));
        assertEquals("JUnitTest", request.getHeaders().get("User-Agent"));

        assertNull(request.getBody()); // Body 없는 GET 요청
    }

    @Test
    void parse_POST_요청_with_Body() throws Exception {
        String body = "{\"name\":\"chatgpt\"}";
        String rawRequest =
                "POST /api/data HTTP/1.1\r\n" +
                        "Host: localhost\r\n" +
                        "Content-Length: " + body.length() + "\r\n" +
                        "Content-Type: application/json\r\n" +
                        "\r\n" +
                        body;

        BufferedReader reader = new BufferedReader(new StringReader(rawRequest));
        HttpRequestParser parser = new HttpRequestParser();

        HttpRequest request = parser.parse(reader);

        assertEquals("POST", request.getMethod());
        assertEquals("/api/data", request.getPath());
        assertEquals("HTTP/1.1", request.getProtocol());

        assertEquals("localhost", request.getHeaders().get("Host"));
        assertEquals(String.valueOf(body.length()), request.getHeaders().get("Content-Length"));
        assertEquals("application/json", request.getHeaders().get("Content-Type"));

        assertEquals(body, request.getBody());
    }

    @Test
    void parse_빈_요청_테스트() throws Exception {
        String rawRequest = "\r\n";
        BufferedReader reader = new BufferedReader(new StringReader(rawRequest));
        HttpRequestParser parser = new HttpRequestParser();

        HttpRequest request = parser.parse(reader);

        assertNull(request.getMethod());
        assertNull(request.getPath());
        assertNull(request.getProtocol());
        assertEquals(0, request.getHeaders().size());
        assertNull(request.getBody());
    }
}
