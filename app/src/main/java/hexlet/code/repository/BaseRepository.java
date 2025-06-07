package hexlet.code.repository;

import com.zaxxer.hikari.HikariDataSource;

public class BaseRepository {
    protected static HikariDataSource dataSource;

    public BaseRepository(HikariDataSource dataSource) {
        BaseRepository.dataSource = dataSource;
    }

    public static void setDataSource(HikariDataSource ds) {
        dataSource = ds;
    }
}
