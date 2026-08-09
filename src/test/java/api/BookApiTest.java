package api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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
        String baseUrl = System.getProperty("api.baseUrl", System.getenv("API_BASE_URL"));
        if (baseUrl != null && !baseUrl.isBlank()) {
            RestAssured.baseURI = baseUrl;
        } else {
            String host = System.getProperty("api.host", System.getenv().getOrDefault("API_HOST", "localhost"));
            String port = System.getProperty("api.port", System.getenv().getOrDefault("API_PORT", "8080"));
            RestAssured.baseURI = String.format("http://%s:%s", host, port);
        }
    }

    @Test
    @Order(1)
    @DisplayName("GET /books should return 200 and a list")
    public void testGetBooks() {
        when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    @Order(2)
    @DisplayName("POST /books with valid body should return 201 and the created book")
    public void testAddBook() {
        long id = System.currentTimeMillis() % 100000 + 1;
        String requestBody = String.format("{\"id\": %d, \"title\": \"Dynamic Book Title\"}", id);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/books")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo("Dynamic Book Title"));
    }

    @Test
    @Order(3)
    @DisplayName("POST /books with blank title should return 400")
    public void testAddBookWithBlankTitle() {
        String requestBody = "{\"id\": 10, \"title\": \"\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/books")
                .then()
                .statusCode(400);
    }
}
