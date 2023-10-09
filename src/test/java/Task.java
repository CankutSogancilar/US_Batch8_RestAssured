import POJOClasses.ToDo;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

public class Task {
    /**
     * Task 1
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * Convert Into POJO
     */

    @Test
    void task1() {
        ToDo todo = given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                //.log().body()
                .statusCode(200)
                .extract().as(ToDo.class);

        System.out.println("todo= " + todo);

    }

    /**
     * Task 2
     * create a get request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     */
    @Test
    void task2() {
        given()
                .pathParam("status", "203")
                .when()
                .get("https://httpstat.us/{status}")
                .then()
                .statusCode(203)
                .contentType(ContentType.TEXT);
    }

    /**
     * Task 3
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */
    @Test
    void task3() {
        String title = given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .log().body()
                .contentType(ContentType.JSON)
                //.body("title",equalTo("quis ut nam facilis et officia qui"))
                .extract().path("title");

        System.out.println(title);
        Assert.assertEquals(title, "quis ut nam facilis et officia qui");
    }

    /**
     * Task 4
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect response completed status to be false
     */

    @Test
    void task4() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .log().body()
                .contentType(ContentType.JSON)
                .body("completed", equalTo(false));


    }

}
