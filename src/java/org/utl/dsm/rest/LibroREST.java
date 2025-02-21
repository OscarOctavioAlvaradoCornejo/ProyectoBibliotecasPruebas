package org.utl.dsm.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.List;
import org.utl.dsm.controller.ControllerLibros;
import org.utl.dsm.model.LibroViewModel;
import org.utl.dsm.model.Libros;

@Path("libro")
public class LibroREST extends Application {

    // Método para insertar un libro
@Path("insert")
@POST
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public Response insert(String datos) {
    Gson gson = new Gson();
    Libros libro = gson.fromJson(datos, Libros.class);

    ControllerLibros controller = new ControllerLibros();
    try {
        int idLibro = controller.insertLibro(libro);

        String out = String.format("{\"idLibro\":\"%d\", \"nombreLibro\":\"%s\"}", idLibro, libro.getNombreLibro());
        return Response.status(Response.Status.CREATED)
                .entity(out)
                .header("Access-Control-Allow-Origin", "*") // Permite CORS
                .build();
    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error al insertar libro: " + e.getMessage())
                .header("Access-Control-Allow-Origin", "*") // Permite CORS
                .build();
    }
}


 @OPTIONS
@Path("{path : .*}")
public Response handlePreflight() {
    return Response.ok()
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
            .header("Access-Control-Allow-Credentials", "true") 
            .build();
}

// Listar todos los libros
@Path("listPublico")
@GET
@Produces(MediaType.APPLICATION_JSON)
public Response listPublico() {
    ControllerLibros controller = new ControllerLibros();  // Crea una instancia del controlador
    List<LibroViewModel> libros = controller.getAllPublico(); // Llama al método para obtener libros
    Gson gson = new Gson();
    String json = gson.toJson(libros); // Convierte la lista a JSON
    return Response.ok(json)
            .header("Access-Control-Allow-Origin", "*") // Permite CORS
            .build();
}

@GET
@Path("getPrivado")
@Produces(MediaType.APPLICATION_JSON)
public Response getAllPrivado() {
    String out;
    
    try {
        // Llamamos al método correcto del controlador
        ControllerLibros controllerLibros = new ControllerLibros();
        List<LibroViewModel> librosPrivados = controllerLibros.getAllPrivado();

        // Imprime en el servidor para verificar el contenido
        System.out.println("Libros obtenidos: " + librosPrivados.size());
        for (LibroViewModel libro : librosPrivados) {
            System.out.println(libro);
        }

        // Convertimos la lista a formato JSON para la respuesta
        Gson gson = new Gson();
        out = gson.toJson(librosPrivados);
    } catch (Exception ex) {
        ex.printStackTrace();
        out = "{\"error\": \"" + ex.getMessage() + "\"}";
    }
    
    // Devolvemos la respuesta con código HTTP 200 OK
    return Response.status(Response.Status.OK).entity(out).build();
}


// Listar todos los libros
//@Path("list")
//@GET
//@Produces(MediaType.APPLICATION_JSON)
//public Response list() {
//    ControllerLibros controller = new ControllerLibros();  // Crea una instancia del controlador
//    List<Libros> libros = controller.getAllLibros(); // Llama al método para obtener libros
//    Gson gson = new Gson();
//    String json = gson.toJson(libros); // Convierte la lista a JSON
//    return Response.ok(json)
//            .header("Access-Control-Allow-Origin", "*") // Permite CORS
//            .build();
//}


    // Obtener un libro por ID
    @Path("getById")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
    @Produces(MediaType.APPLICATION_JSON)  
    public Response getById(@FormParam("id_libro") int idLibro) {
        ControllerLibros controller = new ControllerLibros();
        Libros libro = controller.getLibroById(idLibro); // Cambiado a un solo objeto
        if (libro != null) {
            Gson gson = new Gson();
            String json = gson.toJson(libro);
            return Response.ok(json).build();
        } else {
            String errorJson = "{\"error\": \"Libro no encontrado\"}";
            return Response.status(Response.Status.NOT_FOUND).entity(errorJson).build();
        }
    }

    // Actualizar un libro existente usando POST
    @Path("update")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(String datos) {
        Gson gson = new Gson();
        Libros libro = gson.fromJson(datos, Libros.class);

        ControllerLibros controller = new ControllerLibros();
        try {
            boolean actualizado = controller.updateLibro(libro);

            if (actualizado) {
                String out = String.format("{\"idLibro\":\"%d\", \"mensaje\":\"Libro actualizado correctamente\"}", libro.getIdLibro());
                return Response.status(Response.Status.OK)
                        .entity(out)
                        .header("Access-Control-Allow-Origin", "*")  // CORS
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Libro no encontrado")
                        .header("Access-Control-Allow-Origin", "*")  // CORS
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar el libro: " + e.getMessage())
                    .header("Access-Control-Allow-Origin", "*")  // CORS
                    .build();
        }
    }
}
