package org.utl.dsm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.bd.ConexionMysql;
import org.utl.dsm.controller.ControllerLibros;
import org.utl.dsm.model.Libros;

public class LibrosDAO {

    //private ControllerLibros controllerLibros;

    // Constructor
    public LibrosDAO() {
       // this.controllerLibros = new ControllerLibros();
    }
    
public int insertLibro(String nombreLibro, String autor, String genero, String estatus, String archivoBase64) throws SQLException {
    try (ConexionMysql conexion = new ConexionMysql()) {
        Connection conn = conexion.open();  // Abre la conexión

        String query = "INSERT INTO libros (nombreLibro, autor, genero, estatus, archivoBase64) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setString(1, nombreLibro);
        stmt.setString(2, autor);
        stmt.setString(3, genero);
        stmt.setString(4, estatus);
        stmt.setString(5, archivoBase64);
        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        int idLibro = -1;
        if (rs.next()) {
            idLibro = rs.getInt(1);  // Retorna el ID generado
        }

        rs.close();
        stmt.close();
        return idLibro;
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Error al insertar el libro: " + e.getMessage());
    }
}


    // Método para listar libros
    public List<Libros> listLibros() throws SQLException {
        List<Libros> libros = new ArrayList<>();

        try (ConexionMysql conexion = new ConexionMysql()) {
            Connection conn = conexion.open();  // Abre la conexión

            String query = "SELECT idLibro, nombreLibro, autor, genero, estatus,archivoBase64 FROM libros";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Libros libro = new Libros();
                libro.setIdLibro(rs.getInt("idLibro"));
                libro.setNombreLibro(rs.getString("nombreLibro"));
                libro.setAutor(rs.getString("autor"));
                libro.setGenero(rs.getString("genero"));
                libro.setEstatus(rs.getString("estatus"));
                libro.setArchivoBase64(rs.getString("archivoBase64"));
                libros.add(libro);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al listar los libros: " + e.getMessage());
        }

        return libros;
    }
    
     // Método para actualizar un libro existente
    public boolean updateLibro(int idLibro, String nombreLibro, String autor, String genero, String estatus, String archivoBase64) throws SQLException {
        boolean actualizado = false;

        // Utiliza la conexión de ConexionMysql
        try (ConexionMysql conexion = new ConexionMysql()) {
            Connection conn = conexion.open();  // Abre la conexión

            // Crear la consulta SQL para actualizar el libro
            String query = "UPDATE libros SET nombreLibro = ?, autor = ?, genero = ?, estatus = ?, archivoBase64 = ? WHERE idLibro = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nombreLibro);
            stmt.setString(2, autor);
            stmt.setString(3, genero);
            stmt.setString(4, estatus);
            stmt.setString(5, archivoBase64);
            stmt.setInt(6, idLibro);

            // Ejecutar la actualización
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                actualizado = true;
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el libro: " + e.getMessage());
        }

        return actualizado;
    }
public Libros getLibroById(int idLibro) {
        Libros libro = null; // Cambiar a un solo objeto
        String query = "SELECT idLibro, nombreLibro, autor, genero, estatus FROM libros WHERE idLibro = ?";

        try (ConexionMysql connMysql = new ConexionMysql();  
             Connection conn = connMysql.open();  
             PreparedStatement pstm = conn.prepareStatement(query)) {

            pstm.setInt(1, idLibro);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                libro = new Libros();
                libro.setIdLibro(rs.getInt("idLibro"));
                libro.setNombreLibro(rs.getString("nombreLibro"));
                libro.setAutor(rs.getString("autor"));
                libro.setGenero(rs.getString("genero"));
                libro.setEstatus(rs.getString("estatus"));
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar libro por ID: " + e.getMessage());
        }
        return libro; // Devolver un único objeto Libros
    }

}
