package org.utl.dsm.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.google.gson.Gson;
import org.utl.dsm.controller.ControllerLogin;
import org.utl.dsm.model.Usuario;

@Path("user")
public class LoginREST extends Application {

    @Path("login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(String datos) {
        Gson gson = new Gson();
        Usuario usuario = gson.fromJson(datos, Usuario.class);

        ControllerLogin controller = new ControllerLogin();
        try {
            // Verifica si el login es válido y obtiene el tipo de usuario
            String tipoUsuario = controller.verifyLogin(usuario.getUsername(), usuario.getPass());

            if (tipoUsuario != null) {
                // Construye la respuesta con el tipo de usuario
                String out = String.format("{\"mensaje\":\"Inicio de sesión exitoso\", \"tipo\":\"%s\"}", tipoUsuario);
                return Response.status(Response.Status.OK)
                        .entity(out)
                        .header("Access-Control-Allow-Origin", "*")  // CORS
                        .build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Credenciales inválidas")
                        .header("Access-Control-Allow-Origin", "*")  // CORS
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error durante el inicio de sesión: " + e.getMessage())
                    .header("Access-Control-Allow-Origin", "*")  // CORS
                    .build();
        }
    }
}
