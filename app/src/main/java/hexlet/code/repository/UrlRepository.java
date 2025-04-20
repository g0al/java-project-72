package hexlet.code.repository;

import com.zaxxer.hikari.HikariConfig;
import hexlet.code.model.Url;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static hexlet.code.repository.BaseRepository.dataSource;

public class UrlRepository {

    private static List<Url> entities = new ArrayList<Url>();

    public static String getDbType() {
        String db = System.getenv().getOrDefault("JDBC_DATABASE_URL", "Hikari");
        var hikariConfig = new HikariConfig();
        if (!db.startsWith("jdbc:postgresql")) {
            hikariConfig.setJdbcUrl("jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
            return "Hikari";
        }
        return "Postgres";
    }

    public static void save(Url url) throws SQLException {
        String dbType = getDbType();
        if (dbType.equals("Postgres")) {
            var sql = "INSERT INTO urls (name) VALUES (?)";
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
        } else {
            url.setId((long) entities.size() + 1);
            url.setCreatedAt(LocalDateTime.now());
            entities.add(url);
        }
    }

    public static Optional<Url> find(Long id) throws SQLException {
        String dbType = getDbType();
        if (dbType.equals("Postgres")) {
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
        } else {
            return entities.stream()
                    .filter(entity -> entity.getId() == id)
                    .findAny();
        }
    }


    public static Optional<Url> search(String name) {
        return entities.stream()
                .filter(entity -> entity.getName().equals(name))
                .findAny();
    }

    public static List<Url> getEntities() throws SQLException {
        String dbType = getDbType();
        if (dbType.equals("Postgres")) {
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
        } else {
            return entities;
        }
    }

    public static void removeAll() {
        entities = new ArrayList<>();
    }
}
