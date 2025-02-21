package org.utl.dsm.dao;

import org.utl.dsm.bd.ConexionMysql;
import org.utl.dsm.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public int insertUsuario(String username, String pass, int estatus, String tipo) throws SQLException {
        try ( ConexionMysql conexion = new ConexionMysql()) {
            Connection conn = conexion.open();

            String query = "INSERT INTO usuarios (username, pass, estatus, tipo) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, username);
            stmt.setString(2, pass);
            stmt.setInt(3, estatus);
            stmt.setString(4, tipo);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int idUsuario = -1;
            if (rs.next()) {
                idUsuario = rs.getInt(1);
            }

            rs.close();
            stmt.close();
            return idUsuario;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al insertar el usuario: " + e.getMessage());
        }
    }

    public List<Usuario> listUsuarios() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();

        try ( ConexionMysql conexion = new ConexionMysql()) {
            Connection conn = conexion.open();  // Abre la conexión

            String query = "SELECT idUsuario, username, pass, estatus, tipo FROM usuarios";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPass(rs.getString("pass"));
                usuario.setEstatus(rs.getInt("estatus"));
                usuario.setTipo(rs.getString("tipo"));
                usuarios.add(usuario);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al listar los usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    public String verifyLogin(String username, String password) throws SQLException {
        String tipoUsuario = null;

        try ( ConexionMysql conexion = new ConexionMysql()) {
            Connection conn = conexion.open();
            String query = "SELECT tipo FROM usuarios WHERE username = ? AND pass = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tipoUsuario = rs.getString("tipo"); // Obtiene el tipo de usuario
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al verificar las credenciales: " + e.getMessage());
        }

        return tipoUsuario;
    }

}
