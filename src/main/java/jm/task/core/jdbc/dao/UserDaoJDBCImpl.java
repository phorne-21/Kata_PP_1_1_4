package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {
    private final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS User "
            + "(id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), lastName VARCHAR(255), age INT)";
    private final String DELETE_USERS_TABLE = "DROP TABLE IF EXISTS User";
    private final String SAVE_USER = "INSERT INTO User (name, lastName, age) VALUES (?, ?, ?)";
    private final String DELETE_USER_BY_ID = "DELETE FROM User WHERE id = ?";
    private final String READ_ALL_USERS = "SELECT * FROM User";
    private final String DELETE_ALL_USERS = "DELETE FROM User";

    private static final Logger logger = Logger.getLogger("UserDaoJDBCImpl");

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(CREATE_USERS_TABLE)) {
            preparedStatement.executeUpdate();
            logger.info("Created table of users");
        } catch (SQLException e) {
            logger.severe("Failed to create table of users with the following statement: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(DELETE_USERS_TABLE)) {
            preparedStatement.executeUpdate();
            logger.info("Table of users was successfully dropped");
        } catch (SQLException e) {
            logger.severe("Failed to drop table of users with the following statement: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(SAVE_USER)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            int rowsNum = preparedStatement.executeUpdate();
            logger.info("User " + name + " was successfully saved "/*with id " + id*/);
        } catch (SQLException e) {
            logger.severe("Failed to save user with the following statement: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(DELETE_USER_BY_ID)) {
            preparedStatement.setLong(1, id);
            logger.info("Removed user with id " + id + " from table of users");
        } catch (SQLException e) {
            logger.severe("Failed to remove user with id " + id + " from the table of users.");
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (Statement statement = Util.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(READ_ALL_USERS);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
            }
            logger.info("Retrieved " + userList.size() + " users");
        } catch (SQLException e) {
            logger.severe("Failed to retrieve users with the following statement: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(DELETE_ALL_USERS)) {
            int num = preparedStatement.executeUpdate();
            logger.info("Deleted " + num + " users. Table cleaned.");
        } catch (SQLException e) {
            logger.severe("Failed to clean table of users with the following statement: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}