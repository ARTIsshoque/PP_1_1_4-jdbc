package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import java.util.List;

public class UserServiceImpl implements UserService {
    public void createUsersTable() {
        UserDaoJDBCImpl.getInstance().createUsersTable();
        System.out.println("Создана таблица users.");
    }

    public void dropUsersTable() {
        UserDaoJDBCImpl.getInstance().dropUsersTable();
        System.out.println("Удалена таблица users");
    }

    public void saveUser(String name, String lastName, byte age) {
        UserDaoJDBCImpl.getInstance().saveUser(name, lastName, age);
        System.out.printf("User с именем - %s добавлен в базу данных.\n", name);
    }

    public void removeUserById(long id) {
        UserDaoJDBCImpl.getInstance().removeUserById(id);
        System.out.printf("User с id %d удален из базы данных.\n", id);
    }

    public List<User> getAllUsers() {
        return UserDaoJDBCImpl.getInstance().getAllUsers();
    }

    public void cleanUsersTable() {
        UserDaoJDBCImpl.getInstance().cleanUsersTable();
        System.out.println("Таблица users очищена.");
    }
}
