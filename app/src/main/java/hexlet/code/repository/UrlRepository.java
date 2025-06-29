package hexlet.code.repository;

import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.model.Url;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {

    public UrlRepository(HikariDataSource dataSource) {
        super(dataSource);
    }

    public static void save(Url url) throws SQLException {
        var sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, url.getName());
            var createdAt = Timestamp.valueOf(LocalDateTime.now());
            preparedStatement.setTimestamp(2, createdAt);

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
                url.setCreatedAt(createdAt.toLocalDateTime());
            } else {
                throw new SQLException("DB did not return an ID after saving an entity");
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public static Optional<Url> findByName(String name) throws SQLException {
        var sql = "SELECT id, name, created_at FROM urls WHERE name = ?";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, name);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToUrl(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<Url> findById(Long id) throws SQLException {
        var sql = "SELECT id, name, created_at FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToUrl(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    public static List<Url> getEntities() throws SQLException {
        var sql = "SELECT id, name, created_at FROM urls";
        List<Url> urls = new ArrayList<>();
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                urls.add(mapResultSetToUrl(resultSet));
            }
        }
        return urls;
    }

    private static Url mapResultSetToUrl(ResultSet resultSet) throws SQLException {
        Url url = new Url(resultSet.getString("name"));
        url.setId(resultSet.getLong("id"));
        url.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        return url;
    }
}
