package Controller;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class SamleMenu {
@GetMapping
    public String showChapters(Model model) {
        List<Chapter> chapters = List.of(
            new Chapter(1, "1章"), new Chapter(2, "2章"),
            new Chapter(3, "3章"), new Chapter(4, "4章"),
            new Chapter(5, "5章"), new Chapter(6, "6章")
        );
        model.addAttribute("chapters", chapters);
        return "jstqb"; // jstqb.html に対応
    }
}

class Chapter {
    private final int id;
    private final String name;

    public Chapter(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}
