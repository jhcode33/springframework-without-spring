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
        String filePath = viewPathPrefix + viewName + viewPathSuffix;
        StringBuilder html = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (Map.Entry<String, Object> entry : model.getAttributes().entrySet()) {
                    String placeholder = "${" + entry.getKey() + "}";
                    line = line.replace(placeholder, entry.getValue().toString());
                }
                html.append(line).append("\n");
            }
        } catch (IOException e) {
            html.append("<p>View 파일을 읽을 수 없습니다: ").append(filePath).append("</p>");
        }

        return html.toString();
    }
}

