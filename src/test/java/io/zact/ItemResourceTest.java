package io.zact;

import io.quarkus.test.junit.QuarkusTest;
import io.zact.model.Item;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ItemResourceTest {

    @Test
    public void testGetAllItemsEndpoint() {
        given()
                .when().get("/items")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void testGetItemByIdEndpoint() {
        given()
                .pathParam("id", 24L)
                .when().get("/items/{id}")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void testCreateItemEndpoint() {
        Item newItem = new Item();
        newItem.setName("Test Item");
        newItem.setCount("1");
        newItem.setStatus("Active");

        given()
                .body(newItem)
                .contentType(MediaType.APPLICATION_JSON)
                .when().post("/items")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    public void testUpdateItemEndpoint() {
        Item updatedItem = new Item();
        updatedItem.setName("Updated Test Item");
        updatedItem.setCount("2");
        updatedItem.setStatus("Inactive");

        given()
                .pathParam("id",24L)
                .body(updatedItem)
                .contentType(MediaType.APPLICATION_JSON)
                .when().put("/items/{id}")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void testDeleteItemEndpoint() {
        given()
                .pathParam("id", 24L)
                .when().delete("/items/{id}")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }
}