import io.restassured.RestAssured;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BooksApiTest {
    @BeforeMethod
    public void setup() {
        RestAssured.baseURI = "https://bookstore.toolsqa.com";
    }

    @Test
    public void getBooks() {
        given()
                .when()
                .basePath("/BookStore/v1")
                .get("/Books")
                .then()
                .assertThat()
                .statusCode(200)
                .body("books[0].author", equalTo("Richard E. Silverman"));
    }

    @Test
    public void getBook() {
        given()
                .when()
                .basePath("/BookStore/v1")
                .get("/Book?ISBN=9781449325862")
                .then()
                .assertThat()
                .statusCode(200)
                .body("author", equalTo("Richard E. Silverman"));
    }

    @Test
    public void getBookWithParameter() {
        given()
                .param("ISBN", "9781449325862")
                .when()
                .basePath("/BookStore/v1")
                .get("/Book")
                .then()
                .assertThat()
                .statusCode(200)
                .body("author", equalTo("Richard E. Silverman"));
    }

    @Test
    public void getBookWithHashMapParameter() {
        Map<String, String> map = new HashMap<>();
        map.put("ISBN", "9781449325862");

        given()
                .params(map)
                .when()
                .basePath("/BookStore/v1")
                .get("/Book")
                .then()
                .assertThat()
                .statusCode(200)
                .body("author", equalTo("Richard E. Silverman"));
    }
}
