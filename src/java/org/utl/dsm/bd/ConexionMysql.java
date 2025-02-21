package org.utl.dsm.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMysql implements AutoCloseable {

    private Connection conn;

    public Connection open() {
        String user = "root";
        String password = "Asakalaluya1$";
        String url = "jdbc:mysql://127.0.0.1:3306/ProyectoAS";
        String parametros = "?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url + parametros, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return conn;
    }

    @Override
    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
