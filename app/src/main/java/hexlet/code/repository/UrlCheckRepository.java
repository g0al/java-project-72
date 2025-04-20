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

/*
public static void save(Url url) throws SQLException {
    var sql = "INSERT INTO cars (name) VALUES (?)";
    try (var conn = dataSource.getConnection();
         var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        preparedStatement.setString(1, url.getName());
        var createdAt = LocalDateTime.now();
        preparedStatement.setTimestamp(3, Timestamp.valueOf(createdAt));

        preparedStatement.executeUpdate();
        var generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            url.setId(generatedKeys.getLong(1));
            url.setCreatedAt(createdAt);
        } else {
            throw new SQLException("DB have not returned an id after saving an entity");
        }
    }
}

public static Optional<Url> find(Long id) throws SQLException {
    var sql = "SELECT * FROM urls WHERE id = ?";
    try (var conn = dataSource.getConnection();
         var stmt = conn.prepareStatement(sql)) {
        stmt.setLong(1, id);
        var resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            var name = resultSet.getString("name");
            var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

            var url = new Url(name);
            url.setId(id);
            url.setCreatedAt(createdAt);
            return Optional.of(url);
        }
        return Optional.empty();
    }
}

public static List<Url> getEntities() throws SQLException {
    var sql = "SELECT * FROM urls";
    try (var conn = dataSource.getConnection();
         var stmt = conn.prepareStatement(sql)) {
        var resultSet = stmt.executeQuery();
        var result = new ArrayList<Url>();
        while (resultSet.next()) {
            var id = resultSet.getLong("id");
            var name = resultSet.getString("name");
            var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

            var url = new Url(name);
            url.setId(id);
            url.setCreatedAt(createdAt);
            result.add(url);
        }
        return result;
    }

 */

