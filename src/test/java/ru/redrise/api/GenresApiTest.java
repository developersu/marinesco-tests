package ru.redrise.api;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;

public class GenresApiTest {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String HOST_URI = "http://localhost";
    private static final int HOST_PORT = 8080; // default 8080

    private RequestSpecification specification;

    private static final String genreId1 = "test01";
    private static final String genreId2 = "test02";

    @BeforeClass
    public void beforeClass() {
        this.specification = given()
                .baseUri(HOST_URI)
                .port(HOST_PORT);
    }

    @Test(testName = "Genres fetch")
    public void genresFetch() {
        JsonPath recent = specification
                .when()
                .get("/api/genres")
                .then()
                .assertThat()
                .statusCode(200)
                .log().body()
                .extract()
                .jsonPath();

        List<Map<String, Object>> list = recent.getList("");

        Assert.assertFalse(list.isEmpty());
    }

    ///api/genres
    @Test(testName = "Genres create 1",
        enabled = true)
    public void genresCreate1() {
        String payload = String.format("""
                {
                   "genreId" : "%s",
                   "humanReadableDescription" : "test 01 description"
                }
                 """, genreId1);

        Response response = specification
                .contentType(ContentType.JSON)
                .body(payload)
                .log().body()
                .post("/api/genres");

        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_CREATED);
    }

    @Test(testName = "Genres create 2",
        enabled = true)
    public void genresCreate2() {
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("genreId", genreId2);
        jsonData.put("humanReadableDescription", "test 02 description " +
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        Response response = specification
                .contentType(ContentType.JSON)
                .body(jsonData)
                .log().body()
                .post("/api/genres");

        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_CREATED);
    }

    @Test(testName = "Replace genre (PUT)",
    priority = 1,
    dependsOnMethods = {"genresCreate1", "genresCreate2"})
    public void genresReplace() {
        String genreDesc = "REPLACED DESCRIPTION " +
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("genreId", genreId2);
        jsonData.put("humanReadableDescription", genreDesc);

        log.info("about to update:");
        Response response = specification
                .contentType(ContentType.JSON)
                .body(jsonData)
                .log().body()
                .put("/api/genres/" + genreId2);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);

        log.info("Just updated:");
        specification.when()
                .get("/api/genres/by/id/" + genreId2)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", hasEntry("genreId", genreId2))
                .body("humanReadableDescription", equalTo(genreDesc))
                .log().body()
                .extract()
                .jsonPath();
    }

    @Test(testName = "Delete test",
            priority = 1,
            dependsOnMethods = {"genresReplace"})
    public void genresDelete(){
        int record1status = specification
                .log().body()
                .delete("/api/genres/" + genreId1)
                .statusCode();

        int record2status = specification
                .log().body()
                .delete("/api/genres/" + genreId2)
                .statusCode();

        Assert.assertEquals(record1status, HttpStatus.SC_NO_CONTENT);
        Assert.assertEquals(record2status, HttpStatus.SC_NO_CONTENT);
    }
}
