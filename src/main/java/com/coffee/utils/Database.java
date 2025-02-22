package com.coffee.utils;

//import com.coffee.GUI.DatabaseGUI;

import com.coffee.utils.Settings;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

//import static com.coffee.GUI.DatabaseGUI.status;

public class Database {
    public static Connection getConnection() throws IOException {
        do {
            try {
                FileInputStream fileInputStream = new FileInputStream(Objects.requireNonNull(Resource.getAbsolutePath(Settings.DATABASE_FILE)));
                Properties properties = new Properties();
                properties.load(fileInputStream);
                String dbPort = properties.getProperty("db.port");
                String dbDatabase = properties.getProperty("db.database");
                String dbUsername = properties.getProperty("db.username");
                String dbPassword = properties.getProperty("db.password");
                fileInputStream.close();
                if (dbDatabase == null)
                    throw new RuntimeException();
                String dbUrl = String.format("jdbc:mysql://localhost:%s/%s", dbPort, dbDatabase);
                return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            } catch (RuntimeException | SQLException e) {
                initializeDatabase();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } while (true);
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void initializeDatabase() throws IOException {
        if (SwingUtilities.isEventDispatchThread()) {
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến cơ sở dữ liệu.\nVui lòng khởi động lại chương trình", "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        JOptionPane.showMessageDialog(null, "Không thể kết nối đến cơ sở dữ liệu.\nVui lòng cấu hình lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        Resource.copyResource(Settings.DATABASE_FILE, Settings.DATABASE_FILE);
        FileInputStream fileInputStream = new FileInputStream(Objects.requireNonNull(Resource.getAbsolutePath(Settings.DATABASE_FILE)));
        Properties properties = new Properties();
        properties.load(fileInputStream);
//        new DatabaseGUI(properties);
//        do {
//            properties.load(fileInputStream);
//        } while (!status);
//        status = false;
        fileInputStream.close();
    }
}
