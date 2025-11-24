package spring.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ViewResolver {

    private final String viewPathPrefix;
    private final String viewPathSuffix;

    public ViewResolver(String prefix, String suffix) {
        this.viewPathPrefix = prefix;
        this.viewPathSuffix = suffix;
    }

    public String resolveView(String viewName, Model model) {
        String filePath = resolveFilePath(viewName);
        String content = readResourceContent(filePath, model);

        if (filePath.endsWith(".md")) {
            return wrapMarkdown(content);
        }
        return content;
    }

    private String resolveFilePath(String viewName) {
        String filePath = viewPathPrefix + viewName;
        if (!filePath.endsWith(".html") && !filePath.endsWith(".md")) {
            filePath += viewPathSuffix;
        }
        return filePath;
    }

    /**
     * classpath 리소스를 읽어 문자열로 반환
     * @param resourcePath 리소스 경로
     * @param model 치환할 데이터
     * @return 파일 내용
     */
    private String readResourceContent(String resourcePath, Model model) {
        InputStream is = getResourceAsStream(resourcePath);
        if (is == null) {
            return "<p>파일을 찾을 수 없습니다: " + resourcePath + "</p>";
        }
        return readFromStream(is, model);
    }

    /**
     * InputStream을 읽어 문자열로 변환하고 placeholder 치환
     * @param is InputStream
     * @param model 치환할 데이터
     * @return 파일 내용
     */
    private String readFromStream(InputStream is, Model model) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(replacePlaceholders(line, model)).append("\n");
            }
        } catch (IOException e) {
            content.append("<p>파일을 읽는 중 오류 발생</p>");
        }
        return content.toString();
    }

    /**
     * classpath에서 InputStream 반환
     * @param resourcePath 리소스 경로
     * @return InputStream 또는 null
     */
    private InputStream getResourceAsStream(String resourcePath) {
        return getClass().getClassLoader().getResourceAsStream(resourcePath);
    }

    private String replacePlaceholders(String line, Model model) {
        for (Map.Entry<String, Object> entry : model.getAttributes().entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            line = line.replace(placeholder, entry.getValue().toString());
        }
        return line;
    }

    private String wrapMarkdown(String content) {
        return "<html><body><pre>" + content + "</pre></body></html>";
    }
}
