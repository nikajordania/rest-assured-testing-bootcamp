import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class SomeServiceCallTest {
    @Test
    public void setHeaders() {
        given()
                .log().headers()
                .header("Accept", "application/json")
//                .header("Content-Type", "application/json")
//                .contentType("application/json")
                .contentType(ContentType.JSON)
                .baseUri("https://reqres.in/api/users")
                .when().get()
                .then().statusCode(200);

    }

    @Test
    public void bodyValidationHardAssertion() {
        RestAssured.baseURI = "https://api.example.com";
        Response response = RestAssured.get("/user/123");
        response.then().assertThat()
                .body("name", equalTo("John Doe"))
                .body("orders[0].total", greaterThan(0))
                .body("address.city", equalTo("New York"));
    }

    @Test
    public void bodyValidationSoftAssertion() {
        RestAssured.baseURI = "https://api.example.com";
        Response response = RestAssured.get("/user/123");
        response.then().assertThat()
                .body("name", equalTo("John Doe"),
                        "orders[0].total", greaterThan(0),
                        "address.city", equalTo("New York"));
    }

    @Test
    public void queryParameters() {
        RestAssured.baseURI = "https://api.example.com";

        String category = "electronics";
        int page = 2;

        Response response = RestAssured.given()
                .queryParam("category", category)
                .queryParam("page", page)
                .get("/products");

        System.out.println("Response: " + response.getBody().asString());
    }

    @Test
    public void testService() {

        RequestSpecification request = RestAssured.given();
        request.header("Accept", "application/json");
        request.header("Content-Type", "application/json");
        request.baseUri("https://www.asdasd.tech");

        Response response = CallService.call(request);
        response.then().statusCode(200);

        response.prettyPrint();
    }

    @Test
    public void testService2() {
        RequestSpecification requestSpecification = CallService.requestSpecification()
                .baseUri("https://www.swapi.tech");

        Response response = CallService.call(requestSpecification);
        response.then().statusCode(200);

        response.prettyPrint();
    }
}
