package spring.web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        String content = readFileContent(filePath, model);

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

    private String readFileContent(String filePath, Model model) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(replacePlaceholders(line, model)).append("\n");
            }
        } catch (IOException e) {
            content.append("<p>파일을 읽을 수 없습니다: ").append(filePath).append("</p>");
        }
        return content.toString();
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
