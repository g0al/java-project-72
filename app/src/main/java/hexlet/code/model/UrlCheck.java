package hexlet.code.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UrlCheck {
    private long id;
    private int statusCode;
    private String title;
    private String h1;
    private String description;
    private long urlId;
    private LocalDateTime createdAt;

    public UrlCheck(Long id) {
        this.urlId = id;
    }

    public static int compare(UrlCheck p1, UrlCheck p2) {
        if (p1.getId() > p2.getId()) {
            return 1;
        }
        return -1;
    }
    /*
    Свойство id — идентификатор, должно генерироваться автоматически базой данных.
    Свойство createdAt отражает дату и время добавления проверки на сайт.
    Свойство statusCode - числовое.
    Свойства title, h1, description текстовые, последнее должно уметь хранить большие объёмы текста.
    Свойство urlId - идентификатор сущности Url, проверка для которого была выполнена.
    Сущность Url связана с текущей сущностью UrlCheck связью один-ко-многим
     */
}
