package org.utl.dsm.rest;

import org.utl.dsm.controller.ControllerUsuarios;
import org.utl.dsm.model.Usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.List;

@Path("usuarios")
public class UsuarioREST extends Application{
    private final ControllerUsuarios usuarioController;

    public UsuarioREST() {
        this.usuarioController = new ControllerUsuarios();
    }

@Path("agregarUsuarios")
@POST
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public Response insert(String datos) {
    Gson gson = new Gson();
    Usuario usuario = gson.fromJson(datos, Usuario.class);  // Convierte el JSON a un objeto Usuario

    ControllerUsuarios controller = new ControllerUsuarios();  // Crea una instancia del controlador
    try {
        int idUsuario = controller.insertUsuario(usuario);  // Llama al método para insertar el usuario

        String out = String.format("{\"idUsuario\":\"%d\", \"username\":\"%s\"}", idUsuario, usuario.getUsername());
        return Response.status(Response.Status.CREATED)  // Respuesta 201 - Creado
                .entity(out)  // Cuerpo de la respuesta
                .header("Access-Control-Allow-Origin", "*") // Permite CORS
                .build();
    } catch (IllegalArgumentException e) {
        return Response.status(Response.Status.BAD_REQUEST)  // Respuesta 400 - Petición incorrecta
                .entity("{\"message\": \"" + e.getMessage() + "\"}")  // Mensaje de error
                .header("Access-Control-Allow-Origin", "*") // Permite CORS
                .build();
    } catch (Exception e) {
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)  // Respuesta 500 - Error interno
                .entity("{\"message\": \"Error al insertar usuario: " + e.getMessage() + "\"}")  // Mensaje de error
                .header("Access-Control-Allow-Origin", "*") // Permite CORS
                .build();
    }
}


@GET
@Path("getAllUsuarios")
@Produces(MediaType.APPLICATION_JSON)
public Response getAllUsuarios() {
    ControllerUsuarios controller = new ControllerUsuarios();
    List<Usuario> usuarios = controller.getAllUsuarios(); 
    Gson gson = new Gson();
    String json = gson.toJson(usuarios); // Convertir lista a JSON
    return Response.ok(json)
            .header("Access-Control-Allow-Origin", "*") // Permite CORS
            .build();
}

}
