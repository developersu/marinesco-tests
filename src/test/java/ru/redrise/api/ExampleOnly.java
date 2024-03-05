package ru.redrise.api;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ExampleOnly {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final String jsonExample = """
            [
               {
                  "addedDate" : "2024-01-01",
                  "authors" : [
                     {
                        "authorName" : "Демиденко Светлана Владимировна",
                        "id" : -458202851
                     }
                  ],
                  "container" : "f.fb2-759836-765669",
                  "deleted" : "0",
                  "fileExtension" : "fb2",
                  "fileSize" : 706185,
                  "fileSizeForHumans" : "689,63 KB",
                  "fsFileName" : "765663",
                  "genres" : [
                     {
                        "genreId" : "nonf_biography",
                        "humanReadableDescription" : null
                     }
                  ],
                  "id" : -1826362880,
                  "libId" : "765663",
                  "libraryId" : 1,
                  "libraryVersion" : "20240101",
                  "line" : null,
                  "position" : 0,
                  "serNo" : "",
                  "series" : "",
                  "title" : "Перелистывая память"
               },
               {
                  "addedDate" : "2024-01-01",
                  "authors" : [
                     {
                        "authorName" : "Май Дмитрий",
                        "id" : 311992404
                     }
                  ],
                  "container" : "f.fb2-759836-765669",
                  "deleted" : "0",
                  "fileExtension" : "fb2",
                  "fileSize" : 507378,
                  "fileSizeForHumans" : "495,49 KB",
                  "fsFileName" : "765643",
                  "genres" : [
                     {
                        "genreId" : "child_tale",
                        "humanReadableDescription" : null
                     }
                  ],
                  "id" : -1782438812,
                  "libId" : "765643",
                  "libraryId" : 1,
                  "libraryVersion" : "20240101",
                  "line" : null,
                  "position" : 0,
                  "serNo" : "2",
                  "series" : "Детство",
                  "title" : "Новогодняя сказка"
               },
               {
                  "addedDate" : "2024-01-01",
                  "authors" : [
                     {
                        "authorName" : "Демиденко Светлана Владимировна",
                        "id" : -458202851
                     }
                  ],
                  "container" : "f.fb2-759836-765669",
                  "deleted" : "0",
                  "fileExtension" : "fb2",
                  "fileSize" : 3526176,
                  "fileSizeForHumans" : "3,36 MB",
                  "fsFileName" : "765661",
                  "genres" : [
                     {
                        "genreId" : "nonf_biography",
                        "humanReadableDescription" : null
                     }
                  ],
                  "id" : -1753421740,
                  "libId" : "765661",
                  "libraryId" : 1,
                  "libraryVersion" : "20240101",
                  "line" : null,
                  "position" : 0,
                  "serNo" : "",
                  "series" : "",
                  "title" : "Любите меня"
               },
               {
                  "addedDate" : "2024-01-01",
                  "authors" : [
                     {
                        "authorName" : "Алексеев Борис",
                        "id" : -1665780547
                     }
                  ],
                  "container" : "f.fb2-759836-765669",
                  "deleted" : "0",
                  "fileExtension" : "fb2",
                  "fileSize" : 449204,
                  "fileSizeForHumans" : "438,68 KB",
                  "fsFileName" : "765633",
                  "genres" : [
                     {
                        "genreId" : "adventure",
                        "humanReadableDescription" : null
                     },
                     {
                        "genreId" : "child_tale",
                        "humanReadableDescription" : null
                     },
                     {
                        "genreId" : "children",
                        "humanReadableDescription" : null
                     }
                  ],
                  "id" : -1665535280,
                  "libId" : "765633",
                  "libraryId" : 1,
                  "libraryVersion" : "20240101",
                  "line" : null,
                  "position" : 0,
                  "serNo" : "",
                  "series" : "",
                  "title" : "Новогоднее путешествие Большой Лужи"
               },
               {
                  "addedDate" : "2024-01-01",
                  "authors" : [
                     {
                        "authorName" : "Элья Крепкая",
                        "id" : -183449638
                     }
                  ],
                  "container" : "f.fb2-759836-765669",
                  "deleted" : "0",
                  "fileExtension" : "fb2",
                  "fileSize" : 610834,
                  "fileSizeForHumans" : "596,52 KB",
                  "fsFileName" : "765654",
                  "genres" : [
                     {
                        "genreId" : "sf_action",
                        "humanReadableDescription" : null
                     },
                     {
                        "genreId" : "sf_heroic",
                        "humanReadableDescription" : null
                     },
                     {
                        "genreId" : "adventure",
                        "humanReadableDescription" : null
                     }
                  ],
                  "id" : -1609360729,
                  "libId" : "765654",
                  "libraryId" : 1,
                  "libraryVersion" : "20240101",
                  "line" : null,
                  "position" : 0,
                  "serNo" : "",
                  "series" : "",
                  "title" : "Старый рыцарь"
               },
               {
                  "addedDate" : "2024-01-01",
                  "authors" : [
                     {
                        "authorName" : "Бёме Юлия",
                        "id" : 2108296075
                     }
                  ],
                  "container" : "f.fb2-759836-765669",
                  "deleted" : "0",
                  "fileExtension" : "fb2",
                  "fileSize" : 12985383,
                  "fileSizeForHumans" : "12,38 MB",
                  "fsFileName" : "765630",
                  "genres" : [
                     {
                        "genreId" : "child_tale",
                        "humanReadableDescription" : null
                     },
                     {
                        "genreId" : "children",
                        "humanReadableDescription" : null
                     }
                  ],
                  "id" : -1518406716,
                  "libId" : "765630",
                  "libraryId" : 1,
                  "libraryVersion" : "20240101",
                  "line" : null,
                  "position" : 0,
                  "serNo" : "6",
                  "series" : "Приключения в саванне",
                  "title" : "Тафити и банда обезьян"
               },
               {
                  "addedDate" : "2024-01-01",
                  "authors" : [
                     {
                        "authorName" : "Макаров Марк",
                        "id" : -1967304956
                     }
                  ],
                  "container" : "f.fb2-759836-765669",
                  "deleted" : "0",
                  "fileExtension" : "fb2",
                  "fileSize" : 491992,
                  "fileSizeForHumans" : "480,46 KB",
                  "fsFileName" : "765666",
                  "genres" : [
                     {
                        "genreId" : "child_tale",
                        "humanReadableDescription" : null
                     }
                  ],
                  "id" : -1232223904,
                  "libId" : "765666",
                  "libraryId" : 1,
                  "libraryVersion" : "20240101",
                  "line" : null,
                  "position" : 0,
                  "serNo" : "",
                  "series" : "",
                  "title" : "Сказ о Иване царевиче и сером волке"
               },
               {
                  "addedDate" : "2024-01-01",
                  "authors" : [
                     {
                        "authorName" : "Блэк Архиус",
                        "id" : 1779566922
                     }
                  ],
                  "container" : "f.fb2-759836-765669",
                  "deleted" : "0",
                  "fileExtension" : "fb2",
                  "fileSize" : 323584,
                  "fileSizeForHumans" : "316,00 KB",
                  "fsFileName" : "765635",
                  "genres" : [
                     {
                        "genreId" : "sf_social",
                        "humanReadableDescription" : null
                     },
                     {
                        "genreId" : "humor_anecdote",
                        "humanReadableDescription" : null
                     }
                  ],
                  "id" : -1163766944,
                  "libId" : "765635",
                  "libraryId" : 1,
                  "libraryVersion" : "20240101",
                  "line" : null,
                  "position" : 0,
                  "serNo" : "",
                  "series" : "",
                  "title" : "Огонь лишь начало"
               },
               {
                  "addedDate" : "2024-01-01",
                  "authors" : [
                     {
                        "authorName" : "Шабалина Валентина",
                        "id" : -1801290427
                     }
                  ],
                  "container" : "f.fb2-759836-765669",
                  "deleted" : "0",
                  "fileExtension" : "fb2",
                  "fileSize" : 566330,
                  "fileSizeForHumans" : "553,06 KB",
                  "fsFileName" : "765647",
                  "genres" : [
                     {
                        "genreId" : "child_tale",
                        "humanReadableDescription" : null
                     }
                  ],
                  "id" : -865309302,
                  "libId" : "765647",
                  "libraryId" : 1,
                  "libraryVersion" : "20240101",
                  "line" : null,
                  "position" : 0,
                  "serNo" : "",
                  "series" : "",
                  "title" : "Полуночный поросёнок"
               },
               {
                  "addedDate" : "2024-01-01",
                  "authors" : [
                     {
                        "authorName" : "Азк",
                        "id" : 1033971
                     }
                  ],
                  "container" : "f.fb2-759836-765669",
                  "deleted" : "0",
                  "fileExtension" : "fb2",
                  "fileSize" : 746212,
                  "fileSizeForHumans" : "728,72 KB",
                  "fsFileName" : "765632",
                  "genres" : [
                     {
                        "genreId" : "sf_action",
                        "humanReadableDescription" : null
                     },
                     {
                        "genreId" : "adventure",
                        "humanReadableDescription" : null
                     },
                     {
                        "genreId" : "network_literature",
                        "humanReadableDescription" : null
                     },
                     {
                        "genreId" : "popadancy",
                        "humanReadableDescription" : null
                     }
                  ],
                  "id" : -853565692,
                  "libId" : "765632",
                  "libraryId" : 1,
                  "libraryVersion" : "20240101",
                  "line" : null,
                  "position" : 0,
                  "serNo" : "3",
                  "series" : "Дивизион",
                  "title" : "Маневры 4"
               }
            ]
                        
            """;


    @Test(testName = "example 1",
            enabled = false)
    public void check1() {
        JsonPath jsonPath = new JsonPath(jsonExample);


        log.info("" + jsonPath.get("[0].id"));
        log.info("" + jsonPath.get("flatten()[0].id"));
        log.info(jsonPath.get("flatten().findAll{e -> e.id == -1826362880}.genres") + "");
        log.info(jsonPath.get("flatten().findAll{it.id == -1826362880}.genres") + "");
        log.info(jsonPath.get("flatten().findAll{it.authors.id.contains(-458202851)}.id") + "");
        log.info(jsonPath.get("find{it.authors.id.contains(-458202851)}.id") + "\n");

        List<List<Map<String, Object>>> authorIds = jsonPath.getList("flatten().findAll{it.authors.authorName.contains('Азк')}.genres");
        for (List<Map<String, Object>> authors : authorIds) {
            for (Map<String, Object> map : authors) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    log.info(entry.getKey() + "\t→\t" + entry.getValue());
                }
            }
            log.info("\n");
        }

        //log.info(authorId + "\n");

        log.info(jsonPath.get("flatten().findAll{it.title.startsWith('Перелистывая')}.title") + "\n");

        log.info("\n");
        // ------
        List<Map<String, Object>> list = jsonPath.getList("$");

        for (Map<String, Object> maps : list) {
            for (Map.Entry<String, Object> entry : maps.entrySet()) {
                log.info(entry.getKey() + "\t→\t" + entry.getValue());
            }
            log.info("\n");
        }
    }

    @Test(testName = "example 2")
    public void check2() {
        String HOST_URI = "http://localhost";
        int HOST_PORT = 8080;

        RequestSpecification specification = given()
                .baseUri(HOST_URI)
                .port(HOST_PORT);

        specification
                .when()
                .get("/api/book?recent")
                .then()
                .assertThat()
                .statusCode(200)
                .body("find{it.authors.id.contains(-458202851)}.genres",    // Find single record among others
                        hasItems(
                                allOf(
                                        hasEntry("genreId", "nonf_biography"),
                                        hasEntry("humanReadableDescription", null)
                                )
                        )
                )
                .body("find{it.authors.id.contains(1033971)}.genres",    // Find single record among others
                        hasItems(
                                allOf(
                                        hasEntry("genreId", "popadancy"),
                                        hasEntry("humanReadableDescription", null)
                                )
                        )
                )
                .body("find{it.authors.id.contains(311992404)}",    // Find single record among others
                        allOf(
                                hasEntry("fileExtension", "fb2"),
                                hasEntry("serNo", "2")
                        )
                )
                .body("find{it.authors.id.contains(311992404)}",    // Find single record among others
                        anyOf(
                                hasEntry("fileExtension", "fb2"),
                                hasEntry("fileExtension", "100511")
                        )
                )
                .body("find{it.authors.id.contains(311992404)}",    // Find single record among others
                        allOf(
                                hasKey("libraryVersion"),
                                hasKey("id"),
                                not(hasKey("test"))
                        )
                )
                .log().body()
                .extract()
                .jsonPath();
    }

    @Test(testName = "example 3")
    public void check3() {
        String HOST_URI = "http://localhost";
        int HOST_PORT = 8080;

        given()
            .baseUri(HOST_URI)
            .port(HOST_PORT)
        .when()
            .get("/api/book?recent")
        .then()
            .assertThat()
            .statusCode(200)
            .body(
                    JsonSchemaValidator.matchesJsonSchemaInClasspath("book.json")
            );
    }
}
