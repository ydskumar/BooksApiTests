package api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookApiTest {

	@BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8082"; // change if deployed elsewhere
    }

    @Test
    @Order(1)
    public void testGetBooks() {
        when()
            .get("/books")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    @Order(2)
    public void testAddBook() {
    	long dynamicId = new Random().nextLong(1, 10000);

    	String requestBody = "{"
    	        + "\"id\": " + dynamicId + ","
    	        + "\"title\": \"Dynamic Book Title\""
    	        + "}";

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/books")
        .then()
            .statusCode(200) // or 201 based on your API
            .body("id", equalTo((int) dynamicId)) // Cast to int if API returns int
            .body("title", equalTo("Dynamic Book Title"));
    }
    
}
