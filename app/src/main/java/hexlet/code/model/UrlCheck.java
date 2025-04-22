package hexlet.code.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UrlCheck {

    private Long id;

    private int statusCode;
    private String title;
    private String h1;

    private String description;

    private Long urlId;

    private LocalDateTime createdAt;

    public UrlCheck(Long urlId, int statusCode, String title, String h1, String description) {
        this.urlId = urlId;
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
    }

    public UrlCheck(Long urlId, int statusCode, String title, String h1, String description, LocalDateTime createdAt) {
        this.urlId = urlId;
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.createdAt = createdAt;
    }

    public UrlCheck(Long id) {
        this.id = id;
    }
}
