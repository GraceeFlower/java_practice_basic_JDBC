package com.thoughtworks;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;

public class JDBCUtil {

    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try {
            Properties pro = new Properties();
            ClassLoader classLoader = JDBCUtil.class.getClassLoader();
            URL pathURL = classLoader.getResource("jdbc.properties");
            String path = pathURL.getPath();
            pro.load(new FileReader(path));
            URL = pro.getProperty("URL");
            USER = pro.getProperty("USER");
            PASSWORD = pro.getProperty("PASSWORD");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection connectToDB() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void releaseSource(Connection conn, Statement pre) {
        doubleClose(conn, pre);
    }

    public static void releaseSource(Connection conn, Statement pre, ResultSet res) {
        doubleClose(conn, pre);
        if (null != res) {
            try {
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void doubleClose(Connection conn, Statement pre) {
        if (null != pre) {
            try {
                pre.close();
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
