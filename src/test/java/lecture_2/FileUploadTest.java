package lecture_2;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class FileUploadTest {
    @Test
    public void testFileUpload() {
        File file = new File("src/main/resources/scan_img.png");

        given()
                .log().all()
                .accept(ContentType.JSON)
                .contentType(ContentType.MULTIPART)
                .multiPart("file", file, "image/png")
                .when()
                .post("http://localhost:8000/ocr/");

    }

    @Test
    public void testOCR() throws IOException {
        //        http://localhost:8000/docs
        File file = new File("src/main/resources/scan_img.png");

        String expectedText = Files.readString(Paths.get("src/main/resources/expected_text.txt"));
        System.out.println(expectedText);
        Response response = given()
                .log().all()
                .accept(ContentType.JSON)
                .contentType(ContentType.MULTIPART)
                .multiPart("file", file, "image/png")
                .when()
                .post("http://localhost:8000/ocr/");

        String returnedText = response.body().jsonPath().getString("text");
        System.out.println(returnedText);

        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        int distance = levenshteinDistance.apply(expectedText, returnedText);

        double similarityPercentage = (1.0 - (double) distance / Math.max(expectedText.length(), returnedText.length())) * 100;

        Assert.assertTrue(similarityPercentage >= 95, "Text similarity is below 95%.");
    }
}
