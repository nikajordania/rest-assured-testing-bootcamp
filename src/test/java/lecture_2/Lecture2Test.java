package lecture_2;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lecture_2.models.Driver;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Lecture2Test {

    //    https://restful-api.dev/
    @Test
    public void extractAndCheckSingleValue() {

        given()
                .when()
                .get("http://ergast.com/api/f1/2016/drivers.json")
                .then()
                .assertThat()
                .body("MRData.DriverTable.Drivers.driverId[-1]", equalTo("wehrlein"));
    }

    @Test
    public void extractAndCheckMultipleValues() {

        given()
                .when()
                .get("http://ergast.com/api/f1/2016/drivers.json")
                .then()
                .assertThat()
                .body("MRData.DriverTable.Drivers.driverId", hasItems("alonso", "button"));
    }


    @Test
    public void extractJson() {

        String json = """
                {
                    "message": "An entity of type topic was passed in an invalid format",
                    "meta": {
                        "display": {
                            "topic": [
                                {
                                    "name": [
                                        "must not be blank"
                                    ]
                                },
                                {
                                    "contentType": [
                                        "must not be blank"
                                    ]
                                },
                                {
                                    "content": [
                                        "must not be blank"
                                    ]
                                },
                                {
                                    "version": [
                                        "must not be blank"
                                    ]
                                }
                            ]
                        }
                    }
                }
                """;

        JsonPath jsonPath = new JsonPath(json);

        System.out.println(jsonPath.getString("meta.display.topic.find { it.contentType != null }.contentType[0]"));
        System.out.println(from(json).getString("meta.display.topic.find { it.contentType != null }.contentType[0]"));
    }


    @BeforeTest
    public void initPath() {

//        RestAssured.rootPath = "MRData.DriverTable.Drivers";
    }

    @Test
    public void extractAndCheckMultipleValues2() {
        given()
                .when()
                .get("http://ergast.com/api/f1/2016/drivers.json")
                .then()
                .assertThat()
                .body("driverId", hasItems("alonso", "button"));
    }

    @Test
    public void extractAndCheckMultipleValuesExample2() {

        given()
                .when()
                .get("http://ergast.com/api/f1/2016/drivers.json")
                .then()
                .assertThat()
                .rootPath("MRData.DriverTable.Drivers")
                .body("driverId", hasItems("alonso", "button"))

//                .detachRootPath("Drivers")
                .detachRootPath("DriverTable.Drivers")
                .noRootPath()
                .body("driverId", hasItems("alonso", "button"));
    }

    @Test
    public void extractAndCheckArraySliceSize() {

        given()
                .when()
                .get("http://ergast.com/api/f1/2016/drivers.json")
                .then()
                .assertThat()
                .rootPath("MRData.DriverTable.Drivers")
                .body("driverId[0..2]", hasSize(3));
    }

    @Test
    public void extractAndCheckRange() {

        ValidatableResponse response = given()
                .when()
                .get("http://ergast.com/api/f1/2016/drivers.json")
                .then()
                .assertThat();
//                .rootPath("MRData.DriverTable");

        List<Map<String, String>> filteredDrivers =
                response.extract().jsonPath().getJsonObject("MRData.DriverTable.Drivers.findAll { it.permanentNumber.toInteger() >= 30 }");
        System.out.println(filteredDrivers);

        for (Map<String, String> driver : filteredDrivers) {
            System.out.println("Driver: " + driver.get("givenName") + " " + driver.get("familyName"));
        }

        System.out.println(response.extract().jsonPath().setRootPath("MRData.DriverTable.Drivers").getList("findAll { it.permanentNumber.toInteger() >= 20 && it.permanentNumber.toInteger() >= 30 }.permanentNumber"));
        response
                .body("MRData.DriverTable.Drivers.findAll{ it.permanentNumber >= \"20\" && it.permanentNumber <= \"30\" }.permanentNumber", hasItem("22"))
                .and()
                .body("MRData.DriverTable.Drivers.findAll{ it.permanentNumber >= \"20\" && it.permanentNumber <= \"30\" }.permanentNumber", not(hasItem("33")))
                .and()
                .body("MRData.DriverTable.Drivers.findAll{ it.permanentNumber.toInteger() >= 20 && it.permanentNumber.toInteger() <= 30 } .collect { it.permanentNumber.toInteger() } .sum()", greaterThan(50));


        System.out.println(response.extract().jsonPath().setRootPath("MRData.DriverTable.Drivers")
                .getList("""
                        findAll { it.permanentNumber.toInteger() >= 30 }
                        .collect { [driverId: it.driverId, permanentNumber: it.permanentNumber] }"""


                ));

        List<Driver> drivers = response.extract().jsonPath().setRootPath("MRData.DriverTable.Drivers")
                .getList("""
                        findAll { it.permanentNumber.toInteger() >= 30 }
                        .collect { [driverId: it.driverId, permanentNumber: it.permanentNumber] }
                        """


                );
        System.out.println(drivers);

        List<Map<String, ?>> collectedMap = response.extract().jsonPath().setRootPath("MRData.DriverTable.Drivers")
                .getList("findAll { it.permanentNumber.toInteger() >= 30 }.collect { [driverId: it.driverId, permanentNumber: it.permanentNumber] }");

        System.out.println(collectedMap);

        int anInt = response.extract().jsonPath().getInt("MRData.DriverTable.Drivers.findAll{ it.permanentNumber.toInteger() >= 20 && it.permanentNumber.toInteger() <= 30 } .collect { it.permanentNumber.toInteger() } .sum()");

        System.out.println(anInt);

        assertThat(anInt, Matchers.greaterThanOrEqualTo(146));

    }


    @Test
    public void passParameter() {
        Response response = given()
                .when()
                .get("http://ergast.com/api/f1/2016/drivers.json");


        System.out.println(response.jsonPath()
                .param("n1", 50)
                .setRootPath("MRData.DriverTable.Drivers")
                .getList("""
                        findAll { it.permanentNumber.toInteger() >= n1 }
                        .collect { [driverId: it.driverId, permanentNumber: it.permanentNumber] }"""

                ));

    }


    @Test
    public void findByPassPath() {
        Response response = given()
                .when()
                .get("http://ergast.com/api/f1/2016/drivers.json");


        System.out.println(Optional.ofNullable(response.jsonPath()
                .param("n1", 14)
                .setRootPath("MRData.DriverTable.Drivers")
                .getJsonObject("find { it.permanentNumber.toInteger() == n1 }"
                )));

    }


    @Test
    public void extractMaxAndMinPermanentNumberDriverId() {
        Response response = given()
                .when()
                .get("http://ergast.com/api/f1/2016/drivers.json");


        String highestNumberDriverId = response.path("MRData.DriverTable.Drivers.max { it.permanentNumber.toInteger() }.driverId");
        System.out.println(highestNumberDriverId);
        assertThat(highestNumberDriverId, Matchers.equalTo("wehrlein"));
        String minNumberDriverId = response.path("MRData.DriverTable.Drivers.min { it.permanentNumber.toInteger() }.driverId");
        System.out.println(minNumberDriverId);
    }


}
