package ru.redrise.api;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BooksApiTest {

    //private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String TEST_TITLE = "Евгений Онегин";
    private static final String TEST_SERIES = "Сборник «У солдата есть невеста»";
    private static final Integer TEST_BOOK_ID = -2092944816;

    private static final String HOST_URI = "http://localhost";
    private static final int HOST_PORT = 8080; // default 8080

    private RequestSpecification specification;

    @BeforeClass
    public void beforeClass() {
        this.specification = given()
                .baseUri(HOST_URI)
                .port(HOST_PORT);
    }

    @Test(testName = "Check recent books")
    public void checkRecent() {
        JsonPath recent = specification
                .when()
                .get("/api/book?recent")
                .then()
                .assertThat()
                .statusCode(200)
                .log().body()
                .extract()
                .jsonPath();

        List<Map<String, Object>> list = recent.getList("");

        Assert.assertFalse(list.isEmpty());
    }

    @Test(testName = "Check JSON Schema")
    public void checkSchema() {
        specification
                .when()
                .get("/api/book?recent")
                .then()
                .assertThat()
                .statusCode(200)
                .body(
                        JsonSchemaValidator.matchesJsonSchemaInClasspath("book.json")
                );
    }

    @Test(testName = "Search books by title")
    public void checkByTitle() {
        final String[] authorsExpected = {
            "Комментарий к роману А. С. Пушкина «Евгений Онегин»",
            "Евгений Онегин",
            "Евгений Онегин (Роман в стихове)",
            "Евгений Онегин"
        };

        JsonPath book = specification
                .when()
                .get("/api/book/by/title/" + TEST_TITLE)
                .then()
                .assertThat()
                .statusCode(200)
                .log().body()
                .extract()
                .jsonPath();

        String[] authors = book
                .getList("flatten().findAll{it.authors.authorName.contains('Пушкин Александр Сергеевич')}.collect().title")
                .toArray(String[]::new);

        Arrays.sort(authorsExpected);
        Arrays.sort(authors);

        Assert.assertEquals(authorsExpected, authors);
    }

    @Test(testName = "Search books by series")
    public void checkBySeries() {
        JsonPath recent = specification
                .when()
                .get("/api/book/by/series/" + TEST_SERIES)
                .then()
                .assertThat()
                .statusCode(200)
                .log().body()
                .extract()
                .jsonPath();

        List<Map<String, Object>> list = recent.getList("");

        Assert.assertTrue(list.size() > 1);
    }

    /* Example:
    {
    "id": -2092944816,
    "libraryId": 1,
    "libraryVersion": "20240101",
    "authors": [
        {
            "id": -220073406,
            "authorName": "Щеглов Михаил"
        }
    ],
    "genres": [
        {
            "genreId": "love_history",
            "humanReadableDescription": null
        },
        {
            "genreId": "love_detective",
            "humanReadableDescription": null
        },
        {
            "genreId": "love_short",
            "humanReadableDescription": null
        }
    ],
    "title": "Евгений Онегин, Александр Пушкин. Вся мораль книги с объяснением простыми словами, которую хотел донести автор",
    "series": "",
    "serNo": "",
    "fsFileName": "765420",
    "fileSize": 363564,
    "fileSizeForHumans": "355,04 KB",
    "libId": "765420",
    "deleted": "0",
    "fileExtension": "fb2",
    "addedDate": "2023-12-31",
    "container": "f.fb2-759836-765669",
    "position": 0,
    "line": null
}
    * */
    @Test(testName = "Search books by id",
        enabled = true)
    public void checkById() {
        JsonPath book = specification
                .when()
                .get("/api/book/by/id/" + TEST_BOOK_ID)
                .then()
                .assertThat()
                .statusCode(200)
                .body("",
                        allOf(
                                hasKey("title"),
                                hasKey("series"),
                                hasKey("serNo"),
                                hasEntry("fsFileName", "765420"),
                                hasKey("fileSizeForHumans"),
                                hasEntry("libId", "765420"),
                                hasEntry("deleted", "0"),
                                hasEntry("fileExtension", "fb2"),
                                hasEntry("addedDate", "2023-12-31"),
                                hasEntry("container", "f.fb2-759836-765669"),
                                hasEntry("line", null)
                        )
                )
                .body("fileSize", equalTo(363564))
                .body("position", equalTo(0))
                .log().body()
                .extract()
                .jsonPath();

        Assert.assertEquals(book.getInt("id"), -2092944816);
        Assert.assertEquals(book.getInt("libraryId"), 1);
        Assert.assertEquals(book.getString("libraryVersion"), "20240101");
        Assert.assertEquals(book.getInt("authors[0].id"), -220073406);
        Assert.assertEquals(book.get("authors[0].authorName"), "Щеглов Михаил");

    }

}
