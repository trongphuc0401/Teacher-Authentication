package vn.edu.likelion.TeacherAuthen.data;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

/**
 * Connection -
 *
 * @param
 * @return
 * @throws
 */
public class Connect {
    private String url;
    private String user;
    private String pass;
    private Connection conn = null;

    public Connect() {
        url = "jdbc:oracle:thin:@localhost:1521:orcl";
        user = "system";
        pass = "trongphuc098";
    }

    public Connection openConnect() throws SQLException {
        conn = DriverManager.getConnection(url, user, pass);
        return conn;
    }

    public void closeConnect() throws SQLException {
        if (conn != null) conn.close();
    }

    public Connection getConnect() {
        return conn;
    }


}
