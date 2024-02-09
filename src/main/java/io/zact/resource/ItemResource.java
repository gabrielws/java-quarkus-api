package io.zact.resource;

import io.quarkus.logging.Log;
import io.zact.model.Item;
import io.zact.service.ItemService;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    @Inject // Injeta a dependência do serviço de item
    ItemService itemService;

    // Endpoint para obter todos os itens
    @GET
    public Response getAllItems() {
        try {
            List<Item> items = itemService.get();
            Log.info("[SUCCESS] - [API][GET][getAllItems] Message: " + itemService.mapItemsToString(items));
            return Response.ok(items).build(); // Retorna 200 OK
        } catch (Exception e) {
            Log.info("[ERROR] - [API][GET][getAllItems] Message: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao buscar os itens.").build(); // Retorna um erro 500
        }
    }

    // Endpoint para obter um item pelo ID
    @GET
    @Path("/{id}")
    public Response getItemById(@PathParam("id") Long id) {
        try {
            Item item = itemService.getById(id);
            Log.info("[SUCCESS] - [API][GET][getItemById] Message: [ID: " + item.getId() + ", Nome: " + item.getName() + ", Count: " + item.getCount() + ", Status: " + item.getStatus() + "]");
            return Response.ok(item).build(); // Retorna 200 OK
        } catch (EntityNotFoundException e) {
            Log.info("[ERROR] - [API][GET][getItemById] Message: " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build(); // Retorna um erro 404
        } catch (Exception e) {
            Log.info("[ERROR] - [API][GET][getItemById] Message: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao buscar o item.").build(); // Retorna um erro 500
        }
    }

    // Endpoint para criar um novo item
    @POST
    public Response createItem(Item item) {
        try {
            itemService.create(item);
            Log.info("[SUCCESS] - [API][POST][createItem] Message: [ID: " + item.getId() + ", Nome: " + item.getName() + ", Count: " + item.getCount() + ", Status: " + item.getStatus() + "]");
            return Response.status(Response.Status.CREATED).build(); // Retorna 201 Created
        } catch (ValidationException e) {
            Log.info("[ERROR] - [API][GET][createItem] Message: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build(); // Retorna um erro 400
        } catch (Exception e) {
            Log.info("[ERROR] - [API][GET][createItem] Message: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao criar o item.").build(); // Retorna um erro 500
        }
    }

    // Endpoint para atualizar um item existente
    @PUT
    @Path("/{id}")
    public Response updateItem(@PathParam("id") Long id, Item item) {
        try {
            // Define o ID do item com o ID fornecido no URL
            item.setId(id);
            itemService.update(item);
            Log.info("[SUCCESS] - [API][PUT][updateItem] Message: [ID: " + item.getId() + ", Nome: " + item.getName() + ", Count: " + item.getCount() + ", Status: " + item.getStatus() + "]");
            return Response.ok().build(); // Retorna 200 OK
        } catch (ValidationException e) {
            Log.info("[ERROR] - [API][GET][updateItem] Message: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build(); // Retorna um erro 400
        } catch (Exception e) {
            Log.info("[ERROR] - [API][GET][updateItem] Message: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity("Erro ao atualizar o item.").build(); // Retorna um erro 500
        }
    }

    // Endpoint para excluir um item pelo ID
    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") Long id) {
        try {
            itemService.delete(id);
            Log.info("[SUCCESS] - [API][DELETE][deleteItem] Message: O Item com ID:" + id + " foi excluído.");
            return Response.ok().build(); // Retorna 200 OK
        } catch (EntityNotFoundException e) {
            Log.info("[ERROR] - [API][GET][deleteItem] Message: " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build(); // Retorna um erro 404
        } catch (Exception e) {
            Log.info("[ERROR] - [API][GET][deleteItem] Message: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao excluir o item.").build(); // Retorna um erro 500
        }
    }
}