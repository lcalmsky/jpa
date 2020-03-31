package io.lcalmsky.jpa.jdbc.domain.model.dao;

import io.lcalmsky.jpa.jdbc.domain.model.dto.Player;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class PlayerDaoImpl implements PlayerDao {
    private static final String URL = "db 주소";
    private static final String USER = "db 계정 정보(아이디)";
    private static final String PASSWORD = "db 계정 정보(비밀번호)";

    @Override
    public Player findPlayerById(Long id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        final String sql = "SELECT NAME, GOALS, ASSISTS, DRIBBLE_TOTAL_COUNT, DRIBBLE_SUCCESS_COUNT FROM PLAYER WHERE ID = ?";
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
if (resultSet.next()) {
    Player player = new Player();
    player.setId(id);
    player.setName(resultSet.getString("NAME"));
    player.setGoals(resultSet.getInt("GOALS"));
    player.setAssists(resultSet.getInt("ASSISTS"));
    player.setDribbleTotalCount(resultSet.getInt("DRIBBLE_TOTAL_COUNT"));
    player.setDribbleSuccessCount(resultSet.getInt("DRIBBLE_SUCCESS_COUNT"));
    return player;
}
        } catch (SQLException e) {
            log.error("failed to find player from database", e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                log.error("failed to close resources", e);
            }
        }
        return null;
    }

    @Override
    public void savePlayer(Player player) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        final String sql = "insert into player values (?, ?, ?)";

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, player.getName());
            preparedStatement.setInt(2, player.getGoals());
            preparedStatement.setInt(3, player.getAssists());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("failed to find player from database", e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                log.error("failed to close resources", e);
            }
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
