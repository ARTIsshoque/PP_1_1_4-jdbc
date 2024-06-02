package jm.task.core.jdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class Util {
    private static final Properties PROPERTIES = new Properties();
    private static final String PROPERTIES_FILE = "application.properties";
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String POOL_SIZE_KEY = "db.pool_size";
    private static final Integer DEFAULT_POOL_SIZE = 10;
    private static BlockingQueue<Connection> pool;
    private static List<Connection> sourceConnections;

    static {
        loadProperties();
        initConnectionPool();
    }

    private Util() {
    }

    private static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (InputStream inputStream = Util.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new MySQLException(e);
        }
    }


    private static void initConnectionPool() {
        String poolSize = getProperty(POOL_SIZE_KEY);
        int size = (poolSize == null) ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(size);
        sourceConnections = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Connection connection = open();
            Connection proxyConnection = (Connection) Proxy.newProxyInstance(
                    Util.class.getClassLoader(),
                    new Class[]{Connection.class},
                    (proxy, method, args) -> (method.getName().equals("close"))
                                            ? pool.add((Connection) proxy)
                                            : method.invoke(connection, args));
            pool.add(proxyConnection);
            sourceConnections.add(connection);
        }
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    getProperty(URL_KEY),
                    getProperty(USERNAME_KEY),
                    getProperty(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new MySQLException(e);
        }
    }

    public static Connection getConnection() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new MySQLException(e);
        }
    }

    public static void closeConnectionPool() {
        for (Connection sourceConnection : sourceConnections) {
            try {
                sourceConnection.close();
            } catch (SQLException e) {
                throw new MySQLException(e);
            }
        }
    }
}
