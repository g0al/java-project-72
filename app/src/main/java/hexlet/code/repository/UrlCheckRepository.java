package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlCheckRepository extends BaseRepository {

    public static void save(UrlCheck check) throws SQLException {
        var sql = "INSERT INTO url_checks (url_id, status_code, title, h1, description, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        LocalDateTime createdAt = LocalDateTime.now();
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, check.getUrlId());
            preparedStatement.setLong(2, check.getStatusCode());
            preparedStatement.setString(3, check.getTitle());
            preparedStatement.setString(4, check.getH1());
            preparedStatement.setString(5, check.getDescription());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(createdAt));

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                check.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB did not return an ID after saving an entity");
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public static List<UrlCheck> getChecksForUrl(Long urlId) throws SQLException {
        var sql = "SELECT id, url_id, status_code, title, h1, description, created_at FROM url_checks WHERE url_id = ?";
        List<UrlCheck> checks = new ArrayList<>();
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setLong(1, urlId);

            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UrlCheck check = new UrlCheck(
                            resultSet.getLong("url_id"),
                            resultSet.getInt("status_code"),
                            resultSet.getString("title"),
                            resultSet.getString("h1"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("created_at").toLocalDateTime()
                    );
                    check.setId(resultSet.getLong("id"));
                    checks.add(check);
                }
            }
        }
        return checks;
    }

    public static Map<Long, UrlCheck> getLastChecks() throws SQLException {
        String sql = """
                SELECT uc.id, uc.url_id, uc.status_code, uc.title, uc.h1, uc.description, uc.created_at
                FROM url_checks uc
                INNER JOIN (
                    SELECT url_id, MAX(created_at) AS last_check
                    FROM url_checks
                    GROUP BY url_id
                ) grouped_uc
                ON uc.url_id = grouped_uc.url_id AND uc.created_at = grouped_uc.last_check
                """;

        Map<Long, UrlCheck> lastChecks = new HashMap<>();

        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                UrlCheck check = new UrlCheck(
                        resultSet.getLong("url_id"),
                        resultSet.getInt("status_code"),
                        resultSet.getString("title"),
                        resultSet.getString("h1"),
                        resultSet.getString("description"),
                        resultSet.getTimestamp("created_at").toLocalDateTime()
                );
                check.setId(resultSet.getLong("id"));
                check.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());

                lastChecks.put(check.getUrlId(), check);
            }
            return lastChecks;
        }
    }
}
