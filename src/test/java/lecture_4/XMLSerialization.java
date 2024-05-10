package lecture_4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import lecture_4.models.xml.petstore.Category;
import lecture_4.models.xml.petstore.Pet;
import lecture_4.models.xml.petstore.Tag;
import org.testng.annotations.Test;

public class XMLSerialization {
    @Test
    public void callXMLSerialization() {
        String xmlBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Pet>\n" +
                "    <id>0</id>\n" +
                "    <Category>\n" +
                "        <id>0</id>\n" +
                "        <name>string</name>\n" +
                "    </Category>\n" +
                "    <name>doggie</name>\n" +
                "    <photoUrls>\n" +
                "        <photoUrl>string</photoUrl>\n" +
                "    </photoUrls>\n" +
                "    <tags>\n" +
                "        <Tag>\n" +
                "            <id>0</id>\n" +
                "            <name>string</name>\n" +
                "        </Tag>\n" +
                "    </tags>\n" +
                "    <status>available</status>\n" +
                "</Pet>";

        RestAssured.given()
                .log().all()
                .contentType(ContentType.XML)
                .body(xmlBody)
                .when()
                .post("https://petstore.swagger.io/v2/pet")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public static void t2() {
        Pet pet = new Pet();
        pet.setId(0);
        pet.setName("doggie");
        pet.setStatus("available");

        Category category = new Category();
        category.setId(0);
        category.setName("string");
        pet.setCategory(category);

        Tag tag = new Tag();
        tag.setId(0);
        tag.setName("string");
        pet.setTags(new Tag[]{tag});

        String xmlBody = RestAssured
                .given()
                .log().all()
                .accept(ContentType.XML)
                .contentType(ContentType.XML)
                .body(pet, ObjectMapperType.JAKARTA_EE)
                .when()
                .post("https://petstore.swagger.io/v2/pet")
                .then()
                .log().all()
                .extract().body().asString();
    }


    @Test
    public static void t3() throws JsonProcessingException {

        XmlMapper xmlMapper = new XmlMapper();

        Pet pet = new Pet();
        pet.setId(0);
        pet.setName("doggie");
        pet.setStatus("available");

        Category category = new Category();
        category.setId(0);
        category.setName("string");
        pet.setCategory(category);

        Tag tag = new Tag();
        tag.setId(0);
        tag.setName("string");
        pet.setTags(new Tag[]{tag});

        String xmlBody = xmlMapper.writeValueAsString(pet);
        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.XML)
                .accept(ContentType.XML)
                .body(pet, ObjectMapperType.JAKARTA_EE)
                .when()
                .post("https://petstore.swagger.io/v2/pet")
                .then()
                .log().all();
    }
}
