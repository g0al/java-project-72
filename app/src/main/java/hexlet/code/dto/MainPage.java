package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class MainPage {
    private Boolean visited;
    private String currentUser;
    private String db;

    public Boolean isVisited() {
        return visited;
    }
}
