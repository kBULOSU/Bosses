package br.com.yiatzz.bosses.repository;

import br.com.yiatzz.bosses.database.MysqlDatabase;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class BossRankingRepository {

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS `boss_ranking` (`user_name` VARCHAR(16) NOT NULL, " +
            "`kills` MEDIUMINT NOT NULL, PRIMARY KEY(user_name));";

    private static final String INSERT_OR_UPDATE_QUERY = "INSERT INTO `boss_ranking` (`user_name`, `kills`) VALUES (?,1) " +
            "ON DUPLICATE KEY UPDATE `kills`= kills + 1;";

    private static final String SELECT_RANKING_QUERY = "SELECT * FROM `boss_ranking` ORDER BY `kills` DESC LIMIT 10;";

    private final MysqlDatabase database;

    public void createTable() {
        Connection connection = database.getConnection();
        try {
            if (connection == null || connection.isClosed()) {
                return;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE_QUERY)) {
                preparedStatement.execute();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertOrUpdate(String userName) {
        Connection connection = database.getConnection();
        try {
            if (connection == null || connection.isClosed()) {
                return;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_OR_UPDATE_QUERY)) {
                preparedStatement.setString(1, userName);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Integer> fetch() {
        Map<String, Integer> out = new HashMap<>();

        Connection connection = database.getConnection();
        try {
            if (connection == null || connection.isClosed()) {
                return out;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_RANKING_QUERY);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    out.put(
                            resultSet.getString("user_name"),
                            resultSet.getInt("kills")
                    );
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return out;
    }

}
