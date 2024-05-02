import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CallService {
    public static Response call(RequestSpecification request) {
        Response response = request
                .accept("application/json")
                .when()
                .get("/api/people/1");

        return response;
    }

    public static RequestSpecification requestSpecification() {
        RequestSpecification request = RestAssured.given();
        request.header("Accept", "application/json");
        request.header("Content-Type", "application/json");

        return request;
    }
}
