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
import travelm.LugaresJpaController;

/**
 *
 * @author jospl
 *
 */
@OpenAPIDefinition(
        info = @Info(
                description = "APIREST sobre la BD travelmate en MySQL Server",
                version = "1.0.0",
                title = "Swagger TRAVELMATE"
        )
)

@Tag(name = "lugares",
        description = "Datos de los gastos de travelmate")
@Server(url = "/travelm/datos") // para el Try it

@Path("lugares")
public class ServiceRESTLugares {

    @Context
    private UriInfo context;
    private static final String PERSISTENCE_UNIT = "travelmPU";

    @Operation(
            summary = "Datos de todos los lugares",
            description = "Obtiene los datos de todos los lugares"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acci&oacute;n realizada con &eacute;xito",
            content = {
                @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Lugares.class))
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
        List<Lugares> lista;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LugaresJpaController dao = new LugaresJpaController(emf);
            lista = dao.findLugaresEntities();
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

    @Operation(
            summary = "Obtiene un lugar con un ID",
            description = "Obtiene lugar por ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acci&oacute;n realizada con &eacute;xito",
            content = {
                @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Lugares.class)
                )
            }
    )
    @ApiResponse(responseCode = "204",
            description = "No hay datos que mostrar")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petici&oacute;n")

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@PathParam("id") String id) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        Lugares lug;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LugaresJpaController dao = new LugaresJpaController(emf);
            lug = dao.findLugares(Integer.valueOf(id));
            if (lug == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe lugar con ID " + id);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                statusResul = Response.Status.OK;
                response = Response
                        .status(statusResul)
                        .entity(lug)
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

    @Operation(
            summary = "Crea un lugar nuevo",
            description = ""
    )
    @ApiResponse(responseCode = "201",
            description = "Lugar creado")
    @ApiResponse(responseCode = "302",
            description = " Lugar ya existe")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petici&oacute;n")

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(
            @Parameter(required = true,
                    description = "Datos en json del lugar a a&ntilde;adir") Lugares lug) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LugaresJpaController dao = new LugaresJpaController(emf);
            Lugares lugarEncontrado = null;
            if ((lug.getId() != 0) && (lug.getId() != null)) {
                lugarEncontrado = dao.findLugares(lug.getId());
            }
            if (lugarEncontrado != null) {
                statusResul = Response.Status.FOUND;
                mensaje.put("mensaje", "Ya existe lugar con ID " + lug.getId());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                dao.create(lug);
                statusResul = Response.Status.CREATED;
                mensaje.put("mensaje", "Lugar " + lug.getNombre() + " grabado");
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

    @Operation(
            summary = "Actualiza un lugar existente",
            description = ""
    )
    @ApiResponse(responseCode = "200",
            description = "Lugar actualizado")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petici&oacute;n")

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(
            @Parameter(required = true,
                    description = "Datos en json del lugar a actualizar") Lugares lugar) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            LugaresJpaController dao = new LugaresJpaController(emf);
            Lugares lugarFound = dao.findLugares(lugar.getId());
            if (lugarFound == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe lugar con ID " + lugar.getId());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                // Actualizar campos del lugar encontrado

                lugarFound.setNombre(lugar.getNombre());
                lugarFound.setLatitud(lugar.getLatitud());
                lugarFound.setLongitud(lugar.getLongitud());
                lugarFound.setViajeId(lugar.getViajeId());

                // Grabar los cambios
                dao.edit(lugarFound);
                statusResul = Response.Status.OK;
                mensaje.put("mensaje", "Gasto con ID " + lugar.getId() + " actualizado");
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

    @Operation(
            summary = "Elimina un lugar con ID",
            description = ""
    )
    @ApiResponse(responseCode = "200",
            description = "Lugar eliminado")
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
            LugaresJpaController dao = new LugaresJpaController(emf);
            Lugares lugarFound = dao.findLugares(id);
            if (lugarFound == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe lugar con ID " + id);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                dao.destroy(id);
                statusResul = Response.Status.OK;
                mensaje.put("mensaje", "Lugar con ID " + id + " eliminado.");
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
