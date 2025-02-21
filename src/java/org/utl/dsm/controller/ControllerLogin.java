package org.utl.dsm.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.utl.dsm.bd.ConexionMysql;
import org.utl.dsm.dao.UsuarioDAO;

public class ControllerLogin {
    
    

public String verifyLogin(String username, String password) throws Exception {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        return usuarioDAO.verifyLogin(username, password);
    }
}
