package org.utl.dsm.cqrs;

import org.utl.dsm.model.Libros;
import org.utl.dsm.dao.LibrosDAO;
import java.sql.SQLException;

public class LibrosCQRS {

    private LibrosDAO librosDAO;

    // Constructor para inicializar el DAO
    public LibrosCQRS(LibrosDAO librosDAO) {
        this.librosDAO = librosDAO;
    }

    // Método para validar el libro antes de insertarlo o actualizarlo
    public void validarLibro(Libros libro) throws IllegalArgumentException {
        if (libro.getNombreLibro() == null || libro.getNombreLibro().length() < 5 || libro.getNombreLibro().length() > 100) {
            throw new IllegalArgumentException("El nombre del libro es requerido y debe tener entre 5 y 100 caracteres.");
        }

        if (libro.getGenero() == null || libro.getGenero().length() < 5 || libro.getGenero().length() > 30) {
            throw new IllegalArgumentException("La categoría es requerida y debe tener entre 5 y 30 caracteres.");
        }
    }

    // Método para insertar un libro
    public int insertLibro(Libros libro) throws SQLException {
        validarLibro(libro);
        return librosDAO.insertLibro(libro.getNombreLibro(), libro.getAutor(), 
                                     libro.getGenero(), libro.getEstatus(), 
                                     libro.getArchivoBase64());
    }

    // Método para actualizar un libro
    public boolean updateLibro(Libros libro) throws SQLException {
        validarLibro(libro);
        return librosDAO.updateLibro(libro.getIdLibro(), libro.getNombreLibro(), 
                                     libro.getAutor(), libro.getGenero(), 
                                     libro.getEstatus(), libro.getArchivoBase64());
    }
}
