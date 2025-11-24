package app.controller;

import spring.annotation.Controller;
import spring.annotation.GetMapping;

@Controller
public class MarkdownController {

    @GetMapping("/")
    public String showMarkdown() {
        return "project-overview.md";
    }
}
