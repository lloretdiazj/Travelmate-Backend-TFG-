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
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import travelm.Gastos;
import travelm.GastosJpaController;

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

@Tag(name = "gastos",
        description = "Datos de los gastos de travelmate")
@Server(url = "/travelm/datos") // para el Try it

@Path("gastos")
public class ServiceRESTGastos {

    @Context
    private UriInfo context;
    private static final String PERSISTENCE_UNIT = "travelmPU";

    @Operation(
            summary = "Todos los gastos de la BD",
            description = "Obtiene los datos de todos los gastos de la BD"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acci&oacute;n realizada con &eacute;xito",
            content = {
                @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Gastos.class))
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
        Status statusResul;
        List<Gastos> lista;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            GastosJpaController dao = new GastosJpaController(emf);
            lista = dao.findGastosEntities();
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
            mensaje.put("mensaje", "Error al procesar la petición gastos " + ex.getLocalizedMessage());
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
            summary = "Obtiene un gasto con un ID",
            description = "Obtiene gasto por ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Acci&oacute;n realizada con &eacute;xito",
            content = {
                @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Gastos.class)
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
        Status statusResul;
        Gastos gas;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            GastosJpaController dao = new GastosJpaController(emf);
            gas = dao.findGastos(Integer.valueOf(id));
            if (gas == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe gasto con ID " + id);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                statusResul = Response.Status.OK;
                response = Response
                        .status(statusResul)
                        .entity(gas)
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
            summary = "Crea un gasto nuevo",
            description = ""
    )
    @ApiResponse(responseCode = "201",
            description = "Gasto creado")
    @ApiResponse(responseCode = "302",
            description = " Gasto ya existe")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petici&oacute;n")

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(
            @Parameter(required = true,
                    description = "Datos en json del gastp a a&ntilde;adir") Gastos gas) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Status statusResul;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            GastosJpaController dao = new GastosJpaController(emf);
            Gastos gastoEncontrado = null;
            if ((gas.getId() != 0) && (gas.getId() != null)) {
                gastoEncontrado = dao.findGastos(gas.getId());
            }
            if (gastoEncontrado != null) {
                statusResul = Response.Status.FOUND;
                mensaje.put("mensaje", "Ya existe gasto con ID " + gas.getId());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                dao.create(gas);
                statusResul = Response.Status.CREATED;
                mensaje.put("mensaje", "Gasto " + gas.getDescripcion() + " grabado");
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
            summary = "Actualiza un gasto existente",
            description = ""
    )
    @ApiResponse(responseCode = "200",
            description = "Gasto actualizado")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petici&oacute;n")

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(
            @Parameter(required = true,
                    description = "Datos en json del gastp a actualizar") Gastos gasto) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Status statusResul;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            GastosJpaController dao = new GastosJpaController(emf);
            Gastos gastoFound = dao.findGastos(gasto.getId());
            if (gastoFound == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe gasto con ID " + gasto.getId());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                // Actualizar campos del libro encontrado

                gastoFound.setDescripcion(gasto.getDescripcion());

                gastoFound.setViajeId(gasto.getViajeId());
                gastoFound.setDescripcion(gasto.getDescripcion());
                gastoFound.setGasto(gasto.getGasto());

                // Grabar los cambios
                dao.edit(gastoFound);
                statusResul = Response.Status.OK;
                mensaje.put("mensaje", "Gasto con ID " + gasto.getId() + " actualizado");
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
            summary = "Elimina un gasto con ID",
            description = ""
    )
    @ApiResponse(responseCode = "200",
            description = "Gasto eliminado")
    @ApiResponse(responseCode = "400",
            description = "Error al procesar la petici&oacute;n")

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Status statusResul;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            GastosJpaController dao = new GastosJpaController(emf);
            Gastos lugarFound = dao.findGastos(id);
            if (lugarFound == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe gasto con ID " + id);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                dao.destroy(id);
                statusResul = Response.Status.OK;
                mensaje.put("mensaje", "Gasto con ID " + id + " eliminado.");
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
