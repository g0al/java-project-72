package hexlet.code.repository;

import hexlet.code.model.Url;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository {

    private static List<Url> entities = new ArrayList<Url>();

    public static void save(Url url) {
        url.setId((long) entities.size() + 1);
        url.setCreatedAt(LocalDateTime.now());
        entities.add(url);
    }

    public static Optional<Url> find(Long id) {
        var maybeUrl = entities.stream()
                .filter(entity -> entity.getId() == id)
                .findAny();
        return maybeUrl;
    }

    public static Optional<Url> search(String name) {
        return entities.stream()
                .filter(entity -> entity.getName().equals(name))
                .findAny();
    }

    public static List<Url> getEntities() {
        return entities;
    }

    public static void removeAll() {
        entities = new ArrayList<Url>();
    }
}
/*
public static void save(Car car) throws SQLException {
    var sql = "INSERT INTO cars (make, model, created_at) VALUES (?, ?, ?)";
    try (var conn = dataSource.getConnection();
         var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        preparedStatement.setString(1, car.getMake());
        preparedStatement.setString(2, car.getModel());
        var createdAt = LocalDateTime.now();
        preparedStatement.setTimestamp(3, Timestamp.valueOf(createdAt));

        preparedStatement.executeUpdate();
        var generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            car.setId(generatedKeys.getLong(1));
            car.setCreatedAt(createdAt);

        } else {
            throw new SQLException("DB have not returned an id after saving an entity");
        }
    }
}

public static Optional<Car> find(Long id) throws SQLException {
    var sql = "SELECT * FROM cars WHERE id = ?";
    try (var conn = dataSource.getConnection();
         var stmt = conn.prepareStatement(sql)) {
        stmt.setLong(1, id);
        var resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            var make = resultSet.getString("make");
            var model = resultSet.getString("model");
            var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

            var car = new Car(make, model);
            car.setId(id);
            car.setCreatedAt(createdAt);
            return Optional.of(car);
        }
        return Optional.empty();
    }
}

public static List<Car> getEntities() throws SQLException {
    var sql = "SELECT * FROM cars";
    try (var conn = dataSource.getConnection();
         var stmt = conn.prepareStatement(sql)) {
        var resultSet = stmt.executeQuery();
        var result = new ArrayList<Car>();
        while (resultSet.next()) {
            var id = resultSet.getLong("id");
            var make = resultSet.getString("make");
            var model = resultSet.getString("model");
            var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

            var car = new Car(make, model);
            car.setId(id);
            car.setCreatedAt(createdAt);
            result.add(car);
        }
        return result;
    }

 */
