package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UrlCheckRepository {

    private static List<UrlCheck> entities = new ArrayList<UrlCheck>();

    public static void save(UrlCheck urlCheck) throws IOException {
        urlCheck.setId((long) entities.size() + 1);
        urlCheck.setCreatedAt(LocalDateTime.now());
        entities.add(urlCheck);
    }

    public static List<UrlCheck> find(Long id) {
        var urlChecks = entities.stream()
                .filter(entity -> entity.getUrlId() == id)
                .toList();
        return urlChecks;
    }

    public static List<UrlCheck> getEntities() {
        return entities;
    }

    public static void removeAll() {
        entities = new ArrayList<UrlCheck>();
    }
}
