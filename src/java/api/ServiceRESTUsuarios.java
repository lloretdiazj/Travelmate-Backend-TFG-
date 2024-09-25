/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.List;
import travelm.Gastos;
import travelm.Lugares;
import travelm.Usuarios;
import travelm.UsuariosJpaController;

/**
 *
 * @author jospl
 */
@OpenAPIDefinition(
        info = @Info(
                description = "APIREST sobre la BD travelmate en MySQL Server",
                version = "1.0.0",
                title = "Swagger TRAVELMATE"
        )
)

@Tag(name = "usuarios",
        description = "Datos de los gastos de travelmate")
@Server(url = "/travelm/datos") // para el Try it

@Path("usuarios")
public class ServiceRESTUsuarios {

    @Context
    private UriInfo context;
    private static final String PERSISTENCE_UNIT = "travelmPU";

    @Operation(
            summary = "Datos de todos los usuarios",
            description = "Obtiene los datos de todos los usuarios"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acci&oacute;n realizada con &eacute;xito",
            content = {
                @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Usuarios.class))
                )
            }
    )
    @ApiResponse(responseCode = "204",
            description = "No hay datos que mostrar")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petici&oacute;n")

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        List<Usuarios> lista;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            UsuariosJpaController dao = new UsuariosJpaController(emf);
            lista = dao.findUsuariosEntities();
            if (lista == null) {
                statusResul = Response.Status.NO_CONTENT;
                response = Response
                        .status(statusResul)
                        .build();
            } else {
                statusResul = Response.Status.OK;
                response = Response
                        .status(statusResul)
                        .entity(lista)
                        .build();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición " + ex.getLocalizedMessage());
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;

    }

    @Operation(
            summary = "Obtiene un usuario con un ID",
            description = "Obtiene usuario por ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acci&oacute;n realizada con &eacute;xito",
            content = {
                @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Usuarios.class)
                )
            }
    )
    @ApiResponse(responseCode = "204",
            description = "No hay datos que mostrar")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petici&oacute;n")

    @GET
    @Path("id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@PathParam("id") String id) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        Usuarios usu;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            UsuariosJpaController dao = new UsuariosJpaController(emf);
            usu = dao.findUsuarios(Integer.valueOf(id));
            if (usu == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe usuario con ID " + id);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                statusResul = Response.Status.OK;
                response = Response
                        .status(statusResul)
                        .entity(usu)
                        .build();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición de usuario" + ex.getLocalizedMessage());
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;
    }

    @Operation(
            summary = "Obtiene un usuario con un email",
            description = "Obtiene usuario por email"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acci&oacute;n realizada con &eacute;xito",
            content = {
                @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Usuarios.class)
                )
            }
    )
    @ApiResponse(responseCode = "204",
            description = "No hay datos que mostrar")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petici&oacute;n")

    @GET
    @Path("email/{mail}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOneByMail(@PathParam("mail") String mail) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;

        Usuarios usu = null;

        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            UsuariosJpaController dao = new UsuariosJpaController(emf);
            List<Usuarios> lista = dao.findUsuariosEntities();
            for (Usuarios u : lista) {
                if (mail.equals(u.getEmail())) {
                    usu = u;
                    break;
                }
            }

            if (usu == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe usuario con correo " + mail);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                statusResul = Response.Status.OK;
                response = Response
                        .status(statusResul)
                        .entity(usu)
                        .build();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición de usuario" + ex.getLocalizedMessage());
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;
    }

    @Operation(
            summary = "Crea un usuario nuevo",
            description = ""
    )
    @ApiResponse(responseCode = "201",
            description = "Usuario creado")
    @ApiResponse(responseCode = "302",
            description = " Usuario ya existe")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petici&oacute;n")

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(
            @Parameter(
                    required = true,
                    description = "Datos en json del usuario a a&ntilde;adir") Usuarios usu) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            UsuariosJpaController dao = new UsuariosJpaController(emf);
            Usuarios usuarioEncontrado = null;
            if ((usu.getId() != 0) && (usu.getId() != null)) {
                usuarioEncontrado = dao.findUsuarios(usu.getId());
            }
            if (usuarioEncontrado != null) {
                statusResul = Response.Status.FOUND;
                mensaje.put("mensaje", "Ya existe usuario con ID " + usu.getId());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                dao.create(usu);
                statusResul = Response.Status.CREATED;
                mensaje.put("mensaje", "Usuario " + usu.getNombre() + " grabado");
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición " + ex.getLocalizedMessage());
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;
    }

    @Operation(
            summary = "Actualiza un usuario existente",
            description = ""
    )
    @ApiResponse(responseCode = "200",
            description = "Usuario actualizado")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petici&oacute;n")

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(
            @Parameter(
                    required = true,
                    description = "Datos en json del usuario a actualizar") Usuarios usuario) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            UsuariosJpaController dao = new UsuariosJpaController(emf);
            Usuarios usuarioFound = dao.findUsuarios(usuario.getId());
            if (usuarioFound == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe usuario con ID " + usuario.getId());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                // Actualizar campos del lugar encontrado

                usuarioFound.setNombre(usuario.getNombre());
                usuarioFound.setContrasena(usuario.getContrasena());
                usuarioFound.setEmail(usuario.getEmail());
                usuarioFound.setImagen(usuario.getImagen());

                // Grabar los cambios
                dao.edit(usuarioFound);
                statusResul = Response.Status.OK;
                mensaje.put("mensaje", "Usuario con ID " + usuario.getId() + " actualizado");
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición ERROR: " + ex.getMessage());
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;

    }

    @Operation(
            summary = "Elimina un usuario con ID",
            description = ""
    )
    @ApiResponse(responseCode = "200",
            description = "Usuario eliminado")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petici&oacute;n")

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            UsuariosJpaController dao = new UsuariosJpaController(emf);
            Usuarios usuarioFound = dao.findUsuarios(id);
            if (usuarioFound == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe usuario con ID " + id);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                dao.destroy(id);
                statusResul = Response.Status.OK;
                mensaje.put("mensaje", "usuario con ID " + id + " eliminado.");
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición");
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;
    }

}
