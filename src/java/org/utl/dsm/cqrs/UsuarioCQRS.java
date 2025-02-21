package org.utl.dsm.cqrs;

import org.utl.dsm.model.Usuario;
import org.utl.dsm.dao.UsuarioDAO;
import java.sql.SQLException;

public class UsuarioCQRS {

    private UsuarioDAO usuarioDAO;

    // Constructor para inicializar el DAO
    public UsuarioCQRS(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    // Método para validar el usuario antes de insertarlo
    public void validarUsuario(Usuario usuario) throws IllegalArgumentException {
        if (usuario.getUsername() == null || usuario.getUsername().length() < 3) {
            throw new IllegalArgumentException("El nombre de usuario es requerido y debe tener al menos 3 caracteres.");
        }

        if (usuario.getPass() == null || usuario.getPass().length() < 6) {
            throw new IllegalArgumentException("La contraseña es requerida y debe tener al menos 6 caracteres.");
        }

        if (!usuario.getTipo().matches("[123]")) {
            throw new IllegalArgumentException("El tipo de usuario debe ser '1', '2' o '3'.");
        }
    }

    // Método para insertar un usuario
    public int insertUsuario(Usuario usuario) throws SQLException {
        validarUsuario(usuario);
        return usuarioDAO.insertUsuario(usuario.getUsername(), usuario.getPass(), 
                                         usuario.getEstatus(), usuario.getTipo());
    }
}