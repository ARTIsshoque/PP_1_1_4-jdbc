package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;


public class Main {

    public static void main(String[] args) {
        UserService service = new UserServiceImpl();
        service.createUsersTable();
        service.saveUser("Харитон", "Елизов", (byte) 22);
        service.saveUser("Петр", "Поедов", (byte) 33);
        service.saveUser("Диана", "Очкина", (byte) 26);
        service.saveUser("Светлана", "Ачинская", (byte) 44);
        for (User user : service.getAllUsers()) {
            System.out.println(user);
        }
        service.cleanUsersTable();
        service.dropUsersTable();

        Util.closeConnectionPool();
    }
}
