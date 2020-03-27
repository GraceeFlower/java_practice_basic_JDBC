package com.thoughtworks;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

public class JDBCUtil {

    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static String DRIVER;

    static {
        try {
            Properties pro = new Properties();
            ClassLoader classLoader = JDBCUtil.class.getClassLoader();
            URL pathURL = classLoader.getResource("jdbc.properties");
            String path = Objects.requireNonNull(pathURL).getPath();
            pro.load(new FileReader(path));
            URL = pro.getProperty("URL");
            USER = pro.getProperty("USER");
            PASSWORD = pro.getProperty("PASSWORD");
            DRIVER = pro.getProperty("DRIVER");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection connectToDB() throws SQLException {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void releaseSource(Statement stmt, Connection conn) {
        doubleClose(stmt, conn);
    }

    public static void releaseSource(ResultSet res, Statement stmt, Connection conn) {
        if (null != res) {
            try {
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        doubleClose(stmt, conn);
    }

    private static void doubleClose(Statement stmt, Connection conn) {
        if (null != stmt) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
