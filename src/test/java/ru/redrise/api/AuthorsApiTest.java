package ru.redrise.api;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthorsApiTest {

    //private final Logger log = LoggerFactory.getLogger(getClass());

    private static final int RECENT_RECORDS_LIST_SIZE = 10;
    private static final String TEST_AUTHOR_NAME = "파르나르";
    private static final long TEST_AUTHOR_ID = 1656621508;

    private static final String HOST_URI = "http://localhost";
    private static final int HOST_PORT = 8080; // default 8080

    private RequestSpecification specification;

    @BeforeClass
    public void beforeClass() {
        this.specification = given()
                .baseUri(HOST_URI)
                .port(HOST_PORT);
    }

    // note: remember '.log().all()'
    @Test(testName = "Check recent authors")
    public void checkAuthors() {
        JsonPath recent = specification
                .when()
                .get("/api/author")
                .then()
                .assertThat()
                .statusCode(200)
                .log().body()
                .extract()
                .jsonPath();

        List<Map<String, Object>> list = recent.getList("$");

        Assert.assertEquals(RECENT_RECORDS_LIST_SIZE, list.size());
    }

    @Test(testName = "Check authors schema")
    public void checkSchema() {
        specification
                .when()
                .get("/api/author?recent")
                .then()
                .assertThat()
                .statusCode(200)
                .body(
                        JsonSchemaValidator.matchesJsonSchemaInClasspath("author.json")
                );
    }

    //curl 'localhost:8080/api/author/by/name/Kern'
    @Test(testName = "Check by name")
    public void checkAuthors1() {
        String requestedPath = "/api/author/by/name/" + TEST_AUTHOR_NAME;

        JsonPath namesMatched = specification
                .when()
                .get(requestedPath)
                .then()
                .assertThat()
                .statusCode(200)
                .log().body()
                .extract()
                .jsonPath();

        List<Map<String, Object>> list = namesMatched.getList("");

        Assert.assertFalse(list.isEmpty());
    }

    // curl 'localhost:8080/api/author/by/id/-605622692'
    /* Expected:
        {
            "id": 1656621508,
            "authorName": "파르나르"
        }
    * */
    @Test(testName = "Check by id")
    public void checkAuthors2() {
        String requestedPath = "/api/author/by/id/" + TEST_AUTHOR_ID;

        specification
                .when()
                .get(requestedPath)
                .then()
                .assertThat()
                .statusCode(200)
                .body("$", hasEntry("id", 1656621508))
                .body("authorName", equalTo("파르나르"))
                .log().body()
                .extract()
                .jsonPath();
    }
}
