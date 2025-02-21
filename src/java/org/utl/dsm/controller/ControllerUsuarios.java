package org.utl.dsm.controller;

import org.utl.dsm.cqrs.UsuarioCQRS;
import org.utl.dsm.dao.UsuarioDAO;
import org.utl.dsm.model.Usuario;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerUsuarios {
    private final UsuarioDAO usuarioDAO;
    private final UsuarioCQRS usuarioCQRS;

    public ControllerUsuarios() {
        this.usuarioDAO = new UsuarioDAO();
        this.usuarioCQRS = new UsuarioCQRS(usuarioDAO);
    }

public List<Usuario> getAllUsuarios() {
    try {
        return usuarioDAO.listUsuarios();
    } catch (SQLException e) {
        e.printStackTrace();
          return null; // Cambiar null por un ArrayList vacío
    }
}


public int insertUsuario(Usuario usuario) {
    try {
        return usuarioCQRS.insertUsuario(usuario);  // Usa el CQRS para insertar
    } catch (SQLException e) {
        e.printStackTrace();
        return -1;
    }
}

}
