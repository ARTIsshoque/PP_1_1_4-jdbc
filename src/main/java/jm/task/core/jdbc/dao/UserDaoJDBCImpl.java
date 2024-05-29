package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.MySQLException;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final UserDaoJDBCImpl INSTANCE = new UserDaoJDBCImpl();
    private static final String CREATE_TBL_SQL = """
            CREATE TABLE IF NOT EXISTS `users` (
                id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                name VARCHAR(255) NOT NULL,
                last_name VARCHAR(255) NOT NULL,
                age TINYINT NOT NULL);
            """;
    private static final String DROP_TBL_SQL = "DROP TABLE IF EXISTS `users`;";
    private static final String ADD_USER_SQL = """
            INSERT INTO users (name, last_name, age)
            VALUES (?, ?, ?);
            """;
    private static final String CLEAR_TBL_SQL = "TRUNCATE TABLE `users`";
    private static final String RM_USER_SQL = "DELETE FROM `users` WHERE `id` = ?;";
    private static final String GET_ALL_SQL = "SELECT id, name, last_name, age FROM `users`;";


    private UserDaoJDBCImpl() {
    }

    public static UserDaoJDBCImpl getInstance() {
        return INSTANCE;
    }

    public void createUsersTable() {
        try (Connection conn = Util.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(CREATE_TBL_SQL);
        } catch (SQLException | MySQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Connection conn = Util.getConnection();
        Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(DROP_TBL_SQL);
        } catch (SQLException | MySQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection conn = Util.getConnection();
        PreparedStatement prepStmt = conn.prepareStatement(ADD_USER_SQL)) {
            prepStmt.setString(1, name);
            prepStmt.setString(2, lastName);
            prepStmt.setByte(3, age);
            prepStmt.executeUpdate();
        } catch (SQLException | MySQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (Connection conn = Util.getConnection();
        PreparedStatement prepStmt = conn.prepareStatement(RM_USER_SQL)) {
            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();
        } catch (SQLException | MySQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = Util.getConnection();
        PreparedStatement prepStmt = conn.prepareStatement(GET_ALL_SQL)) {
            ResultSet resultSet = prepStmt.executeQuery();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("last_name"),
                        resultSet.getByte("age")
                );
                user.setId(resultSet.getLong("id"));
                users.add(user);
            }
            return users;
        } catch (SQLException | MySQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cleanUsersTable() {
        try (Connection conn = Util.getConnection();
        Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(CLEAR_TBL_SQL);
        } catch (SQLException | MySQLException e) {
            e.printStackTrace();
        }
    }
}
