package lecture_2;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class JsonBodyGenerationTest {

    @Test
    public void incorrectWayTest() {
        String requestBody = "{\n" +
                "  \"id\": 10,\n" +
                "  \"name\": \"doggie\",\n" +
                "  \"category\": {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"Dogs\"\n" +
                "  },\n" +
                "  \"photoUrls\": [\n" +
                "    \"string\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"available\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("https://petstore3.swagger.io/api/v3/pet")
                .then()
                .statusCode(200);
    }

    //    Create JSON from a HashMap
//    https://github.com/rest-assured/rest-assured/wiki/Usage#create-json-from-a-hashmap
    @Test
    public void createJsonUsingHasMap() {
//        {
//            "name": "morpheus",
//                "job": "leader"
//        }
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("name", "morpheus");
        jsonAsMap.put("job", "leader");

        given()
                .contentType(JSON)
                .body(jsonAsMap)
                .log().all()
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .log()
                .all();
    }

    @Test
    public void testJson1() {
        JSONObject category = new JSONObject();
        category.put("id", 0);
        category.put("name", "string");

        JSONArray photoUrls = new JSONArray();
        photoUrls.put("string").put("asdsd");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 0);
        jsonObject.put("category", category);
        jsonObject.put("name", "doggie");

        System.out.println(jsonObject.toString());
    }

    @Test
    public static void testJson2() {
        JSONObject jsonObject = new JSONObject()
                .put("id", 0)
                .put("category", new JSONObject()
                        .put("id", 0)
                        .put("name", "string"))
//                .put("photoUrls", new String[]{"string"})
                .put("photoUrls", new JSONArray().put("string"));

        System.out.println(jsonObject.toString(4));
    }

    @Test
    public static void test3() {
        Faker faker = new Faker();

        JSONObject jsonObject = new JSONObject()
                .put("id", faker.number().randomNumber())
                .put("category", new JSONObject()
                        .put("id", faker.number().randomNumber())
                        .put("name", faker.animal().name()));

        System.out.println(jsonObject.toString(4));


    }

    @Test
    public void extractResponseAsJsonObjectString() {

        String response = given()
                .when()
                .get("http://ergast.com/api/f1/2016/drivers.json")
                .then()
                .assertThat()
                .body("MRData.DriverTable.Drivers.driverId[-1]", equalTo("wehrlein")).extract().response().body().asString();

        JSONObject jsonResponse = new JSONObject(response);

        System.out.println("Response JSONObject: " + jsonResponse);

        JSONObject mrData = jsonResponse.getJSONObject("MRData");
        JSONObject driverTable = mrData.getJSONObject("DriverTable");
        JSONArray drivers = driverTable.getJSONArray("Drivers");

        for (int i = 0; i < drivers.length(); i++) {
            JSONObject driver = drivers.getJSONObject(i);
            String driverId = driver.getString("driverId");
            String givenName = driver.getString("givenName");
            String familyName = driver.getString("familyName");
            String nationality = driver.getString("nationality");

            System.out.println("Driver " + (i + 1) + ":");
            System.out.println("Driver ID: " + driverId);
            System.out.println("Given Name: " + givenName);
            System.out.println("Family Name: " + familyName);
            System.out.println("Nationality: " + nationality);
            System.out.println("----------------------");
        }
    }

    @Test
    public void extractResponseAsJsonObject() {

        String json = given()
                .when()
                .get("http://ergast.com/api/f1/2016/drivers.json")
                .then()
                .assertThat()
                .body("MRData.DriverTable.Drivers.driverId[-1]", equalTo("wehrlein")).extract().body().asString();

        JSONObject jsonResponse = new JSONObject(json);

        System.out.println("Response JSONObject: " + jsonResponse);

        JSONObject mrData = jsonResponse.getJSONObject("MRData");
        JSONObject driverTable = mrData.getJSONObject("DriverTable");
        JSONArray drivers = driverTable.getJSONArray("Drivers");

        JSONObject firstDriver = drivers.getJSONObject(0);

        System.out.println(firstDriver);
    }

    @Test
    public void extractResponseAsJsonObjectPart() {

        String json = given()
                .when()
                .get("http://ergast.com/api/f1/2016/drivers.json")
                .then()
                .extract().jsonPath()
                .getJsonObject("MRData.DriverTable.Drivers");

        System.out.println(json);
        JSONObject jsonResponse = new JSONObject(json);

        System.out.println("Response JSONObject: " + jsonResponse);

        JSONObject mrData = jsonResponse.getJSONObject("MRData");
        JSONObject driverTable = mrData.getJSONObject("DriverTable");
        JSONArray drivers = driverTable.getJSONArray("Drivers");

        JSONObject firstDriver = drivers.getJSONObject(0);

        System.out.println(firstDriver);
    }

    @Test
    public void jsonArray() {

        String string = given()
                .when()
                .get("https://api.restful-api.dev/objects")
                .then()
                .extract().body().asString();

        JSONArray jsonArray = new JSONArray(string);

        System.out.println(jsonArray.getJSONObject(0).opt("asdasd"));

    }

    @Test
    public void testObjectMapping() {
        String jsonBody = given()
                .contentType(JSON)
                .log().all()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .log()
                .all().extract().body().asString();

        JSONObject jsonObject = new JSONObject(jsonBody);

        assertThat(jsonObject.getInt("page"), equalTo(2));
        assertThat(jsonObject.getInt("per_page"), equalTo(6));
        assertThat(jsonObject.getInt("total"), equalTo(12));
        assertThat(jsonObject.getInt("total_pages"), equalTo(2));

        JSONArray dataArray = jsonObject.getJSONArray("data");
        assertThat(dataArray.length(), equalTo(6));

        JSONObject firstDataObject = dataArray.getJSONObject(0);
        assertThat(firstDataObject.getInt("id"), equalTo(7));
        assertThat(firstDataObject.getString("email"), equalTo("michael.lawson@reqres.in"));
        assertThat(firstDataObject.getString("first_name"), equalTo("Michael"));
        assertThat(firstDataObject.getString("last_name"), equalTo("Lawson"));
        assertThat(firstDataObject.getString("avatar"), equalTo("https://reqres.in/img/faces/7-image.jpg"));

        JSONObject supportObject = jsonObject.getJSONObject("support");
        assertThat(supportObject.getString("url"), equalTo("https://reqres.in/#support-heading"));
        assertThat(supportObject.getString("text"), equalTo("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }
}
