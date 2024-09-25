/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.info.Info;
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
import travelm.Viajes;
import travelm.ViajesJpaController;

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

@Tag(name = "viajes",
        description = "Datos de los gastos de travelmate")
@Server(url = "/travelm/datos") // para el Try it

@Path("viajes")
public class ServiceRESTViajes {

    @Context
    private UriInfo context;
    private static final String PERSISTENCE_UNIT = "travelmPU";

    @Operation(
            summary = "Datos de todos los viajes",
            description = "Obtiene los datos de todos los viajes"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acci&oacute;n realizada con &eacute;xito",
            content = {
                @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Viajes.class))
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
        List<Viajes> lista;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            ViajesJpaController dao = new ViajesJpaController(emf);
            lista = dao.findViajesEntities();
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
            summary = "Obtiene un viaje con un ID",
            description = "Obtiene viaje por ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acci&oacute;n realizada con &eacute;xito",
            content = {
                @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Viajes.class)
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
        Viajes usu;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            ViajesJpaController dao = new ViajesJpaController(emf);
            usu = dao.findViajes(Integer.valueOf(id));
            if (usu == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe viaje con ID " + id);
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
            summary = "Crea un viaje nuevo",
            description = ""
    )
    @ApiResponse(responseCode = "201",
            description = "Viaje creado")
    @ApiResponse(responseCode = "302",
            description = " Viaje ya existe")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petici&oacute;n")

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(
            @Parameter(required = true,
                    description = "Datos en json del viaje a a&ntilde;adir") Viajes via) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            ViajesJpaController dao = new ViajesJpaController(emf);
            Viajes viajeEncontrado = null;
            if ((via.getId() != 0) && (via.getId() != null)) {
                viajeEncontrado = dao.findViajes(via.getId());
            }
            if (viajeEncontrado != null) {
                statusResul = Response.Status.FOUND;
                mensaje.put("mensaje", "Ya existe viaje con ID " + via.getId());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                via.setFechaFin(via.getFechaFin());
                dao.create(via);
                statusResul = Response.Status.CREATED;
                mensaje.put("mensaje", "Viaje " + via.getNombre() + " grabado");
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición: " + ex.getLocalizedMessage());
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
            summary = "Actualiza un viaje existente",
            description = ""
    )
    @ApiResponse(responseCode = "200",
            description = "Viaje actualizado")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petici&oacute;n")

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(
            @Parameter(required = true,
                    description = "Datos en json del viaje a actualizar") Viajes viaje) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            ViajesJpaController dao = new ViajesJpaController(emf);
            Viajes viajeEncontrado = dao.findViajes(viaje.getId());
            if (viajeEncontrado == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe viaje con ID " + viaje.getId());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                // Actualizar campos del lugar encontrado

                viajeEncontrado.setNombre(viaje.getNombre());
                viajeEncontrado.setFechaInicio(viaje.getFechaInicio());
                viajeEncontrado.setFechaFin(viaje.getFechaFin());
                viajeEncontrado.setKilometrosRealizados(viaje.getKilometrosRealizados());
                viajeEncontrado.setImagen(viaje.getImagen());
//                  viajeEncontrado.setUsuariosCollection(viaje.getUsuariosCollection());

                // Grabar los cambios
                dao.edit(viajeEncontrado);
                statusResul = Response.Status.OK;
                mensaje.put("mensaje", "Viaje con ID " + viaje.getId() + " actualizado");
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
            summary = "Elimina un viaje con ID",
            description = ""
    )
    @ApiResponse(responseCode = "200",
            description = "Viaje eliminado")
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
            ViajesJpaController dao = new ViajesJpaController(emf);
            Viajes viajeEncontrado = dao.findViajes(id);
            if (viajeEncontrado == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe viaje con ID " + id);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                dao.destroy(id);
                statusResul = Response.Status.OK;
                mensaje.put("mensaje", "viaje con ID " + id + " eliminado.");
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
}
