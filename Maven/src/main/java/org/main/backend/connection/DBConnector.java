package org.main.backend.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс для подключения к БД
 */
public class DBConnector {

    private static Connection connection;

    /**
     * Метод, предназначенный для получения соединения с БД, указанной в настройках приложения
     * @return соединение с БД
     * @throws SQLException
     */
    public static Connection getConnection() {
        ApplicationProperties properties = ApplicationProperties.getInstance();
        try {
            if(connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(properties.getValue("database_url"),
                                                        properties.getValue("database_user"),
                                                        properties.getValue("database_password"));
            }
        } catch(SQLException e) {
                System.out.println("Ошибка соединения с БД: " + e);
            }
        return connection;
    }

}
