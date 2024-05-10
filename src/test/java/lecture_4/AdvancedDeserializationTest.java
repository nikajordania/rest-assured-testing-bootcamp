package lecture_4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lecture_4.models.booking.BookingResponseItem;
import lecture_4.models.f1.DriversItem;
import lecture_4.models.f1.DriversResponse;
import lecture_4.models.reqres.ListUsers.ListUsersResponse;
import lecture_4.models.reqres.User.UserResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class AdvancedDeserializationTest {

    @BeforeClass
    public void setUp() {
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    public void getResource() {
        UserResponse userResponse = given()
                .baseUri("https://reqres.in/api/users/2")
                .get()
                .then()
                .log().all().extract().body().as(UserResponse.class);


        System.out.println("userResponse.getData().getEmail() = " + userResponse.data().email());
    }

    @Test
    public void getUsersListFromAPI() {
        ListUsersResponse usersResponse = given()
                .baseUri("https://reqres.in/api")
                .basePath("/users")
                .contentType(ContentType.JSON)
                .when().get()
                .then()
                .statusCode(200)
                .and()
                .extract().body().as(ListUsersResponse.class);

        usersResponse.getData().forEach(System.out::println);
    }

    @Test
    public void deserializeDriversDataFromAPI() {
        Response response = given().when().get("http://ergast.com/api/f1/2016/drivers.json");

        DriversResponse driversResponse = response.then().extract().body().as(DriversResponse.class);

        System.out.println(driversResponse);
    }

    @Test
    public void extractPartOfJsonAsObject() {
        Response response = given().when().get("http://ergast.com/api/f1/2016/drivers.json");

        JsonPath jsonPath = new JsonPath(response.getBody().asString());

        List<DriversItem> drivers = jsonPath.getList("MRData.DriverTable.Drivers", DriversItem.class);

        for (DriversItem driver : drivers) {
            System.out.println("Driver: " + driver.getGivenName() + " " + driver.getFamilyName());
            System.out.println("Nationality: " + driver.getNationality());
            System.out.println("Date of Birth: " + driver.getDateOfBirth());
            System.out.println("--------------");
        }
    }

    @Test
    public void deserializeBookingListFromAPI() {
        Response response = given().when().get("https://restful-booker.herokuapp.com/booking");

        List<BookingResponseItem> bookingResponseItems = response.then().extract().body().jsonPath().getList("$", BookingResponseItem.class);

        System.out.println(bookingResponseItems);
    }

    @Test
    public void deserializeBookingListUsingObjectMapper() throws JsonProcessingException {
        Response response = given().when().get("https://restful-booker.herokuapp.com/booking");

        ObjectMapper objectMapper = new ObjectMapper();
        List<BookingResponseItem> bookingResponseItems = objectMapper.readValue(response.then().extract().body().asString(), new TypeReference<List<BookingResponseItem>>() {
        });

        System.out.println(bookingResponseItems);
    }


}