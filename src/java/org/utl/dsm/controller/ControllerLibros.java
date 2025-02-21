package org.utl.dsm.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.APPService.LibroExternoAppService;
import org.utl.dsm.cqrs.LibrosCQRS;
import org.utl.dsm.dao.LibrosDAO;
import org.utl.dsm.model.Libros;
import org.utl.dsm.model.LibroViewModel;

public class ControllerLibros {

    // Instancias de LibrosDAO y LibrosCQRS
    private final LibrosDAO librosDAO;
    private final LibrosCQRS librosCQRS;
    
    // Constructor
    public ControllerLibros() {
        this.librosDAO = new LibrosDAO();
        this.librosCQRS = new LibrosCQRS(librosDAO);  // Se pasa el DAO al CQRS
    }

    // Método para obtener todos los libros
    public List<Libros> getAllLibros() {
        try {
            return librosDAO.listLibros();  // Llama al método del DAO
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<LibroViewModel> getAllPublico() {
        try {
            List<Libros> libros = librosDAO.listLibros();  // Llama al método del DAO
            List<LibroViewModel> libroViewModels = new ArrayList<>();

            for (Libros libro : libros) {
                libroViewModels.add(new LibroViewModel(
                    libro.getIdLibro(),
                    libro.getNombreLibro(),
                    libro.getAutor(),
                    libro.getGenero(),
                    libro.getEstatus(),
                    libro.getArchivoBase64()));
            }

            return libroViewModels;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
public List<LibroViewModel> getAllPrivado() throws Exception {
    List<LibroViewModel> lstLibrosViewModel = new ArrayList<>();
    LibroExternoAppService librosExternos = new LibroExternoAppService();
    
    // Obtener libros de la API externa y agregar a la lista principal
    List<LibroViewModel> apiUniversidadAndre = librosExternos.apiUniversidadAndre();
    lstLibrosViewModel.addAll(apiUniversidadAndre);

    // Crear instancia de LibrosDAO y obtener los libros internos
    LibrosDAO librosDAO = new LibrosDAO();
    List<Libros> librosInternos = librosDAO.listLibros();
    
    // Agregar los libros internos a la lista principal
    for (Libros libro : librosInternos) {
        lstLibrosViewModel.add(new LibroViewModel(
            libro.getIdLibro(),
            libro.getNombreLibro(),
            libro.getAutor(),
            libro.getGenero(),
            libro.getEstatus(),
            libro.getArchivoBase64()));
    }

    return lstLibrosViewModel;
}

        
   
    // Método para obtener un libro por ID
    public Libros getLibroById(int idLibro) {
        return librosDAO.getLibroById(idLibro);  // Llama al método del DAO
    }

    // Método para insertar un libro
    public int insertLibro(Libros libro) {
        try {
            return librosCQRS.insertLibro(libro);  // Usa el CQRS para insertar
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Método para actualizar un libro
    public boolean updateLibro(Libros libro) {
        try {
            return librosCQRS.updateLibro(libro);  // Usa el CQRS para actualizar
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
