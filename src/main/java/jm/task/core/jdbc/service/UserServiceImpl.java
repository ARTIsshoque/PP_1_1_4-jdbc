package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserServiceImpl implements UserService {
    public final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public void createUsersTable() {
        UserDaoJDBCImpl.getInstance().createUsersTable();
        logger.info("Создана таблица users.");
    }

    public void dropUsersTable() {
        UserDaoJDBCImpl.getInstance().dropUsersTable();
        logger.info("Удалена таблица users");
    }

    public void saveUser(String name, String lastName, byte age) {
        UserDaoJDBCImpl.getInstance().saveUser(name, lastName, age);
        logger.info("User с именем - {} добавлен в базу данных.", name);
    }

    public void removeUserById(long id) {
        UserDaoJDBCImpl.getInstance().removeUserById(id);
        logger.info("User с id {} удален из базы данных.", id);
    }

    public List<User> getAllUsers() {
        return UserDaoJDBCImpl.getInstance().getAllUsers();
    }

    public void cleanUsersTable() {
        UserDaoJDBCImpl.getInstance().cleanUsersTable();
        logger.info("Таблица users очищена.");
    }
}
