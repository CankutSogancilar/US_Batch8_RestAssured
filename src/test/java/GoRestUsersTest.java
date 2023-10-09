import POJOClasses.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTest {
    // in UI testing we test the function manually first and then we write automation tests.

    // Ilk test case ini postmane bakarak yazdik, hoca bu case i tavsiye etmedi. Body nin icini...

    public String createRandomName() {   // random isim metodu
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String createRandomEmail() {  // random email metodu
        return RandomStringUtils.randomAlphabetic(10) + "@techno.com";
    }

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void setUp() {
        baseURI = "https://gorest.co.in/public/v2/users";

        requestSpecification = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer 1b1e316a8c242d7478c51dc77a5bfff72ae52deff84ae4a6c5ffe59182670618")
                .setContentType(ContentType.JSON)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.BODY) // log body yi gormek istedigimden ekliyorum buraya
                .expectContentType(ContentType.JSON)
                .build();
    }

    @Test
    void createNewUser() {
        given()
                .spec(requestSpecification)
                .body("{\"name\":\"" + createRandomName() + "\", \"gender\":\"male\",\"email\":\"" + createRandomEmail() + "\",\"status\":\"active\"}")
                .when()
                .post("")
                .then()
                .spec(responseSpecification)
                .statusCode(201);

        //.body("email",equalTo("eda1234567@gmail.com"));


        // Karisik bir yontem, cunku hata yapma olasiligi yuksek yazarken
    }

    @Test
    void createNewUserWithMaps() { // map yaratip eklemek cok daha kolay
        Map<String, String> user = new HashMap<>();
        user.put("name", createRandomName());
        user.put("gender", "male");
        user.put("email", createRandomEmail());
        user.put("status", "active");

        given()
                .spec(requestSpecification)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(201)
                .spec(responseSpecification)
                .body("email", equalTo(user.get("email")));
    }

    User requestBody = new User();
    Response responseBody;

    @Test
    void createNewUserWithObject() {

        requestBody.setName(createRandomName());
        requestBody.setEmail(createRandomEmail());
        requestBody.setGender("male");
        requestBody.setStatus("active");

        responseBody = given()
                .spec(requestSpecification)
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(201)
                .spec(responseSpecification)
                .body("email", equalTo(requestBody.getEmail()))
                .extract().response();

        System.out.println(requestBody.getName());


    }

    /**
     * Write create user negative test
     **/

    @Test(dependsOnMethods = "createNewUserWithObject")
    void createUserNegativeTest() {
        requestBody.setName(createRandomName());
        requestBody.setGender("female");

        given()
                .spec(requestSpecification)
                .body(requestBody)
                .when()
                .post("")
                .then()
                .statusCode(422)
                .spec(responseSpecification)
                .body("[0].message", equalTo("has already been taken")); // Body de array olarak cikiyo o
        // yuzden index kullandik...


    }

    /**
     * get the user you created in createAUserWithObjects test
     **/
    @Test(dependsOnMethods = "createNewUserWithObject")
    void getUserById() {
        given()
                .spec(requestSpecification)
                .pathParam("userId", responseBody.path("id"))
                .when()
                .get("/{userId}")
                .then()
                .statusCode(200)
                .spec(responseSpecification)
                .body("name", equalTo(requestBody.getName()))
                .body("email", equalTo(requestBody.getEmail()));
    }

    /**
     * Update the user you created in createAUserWithObjects
     **/

    @Test (dependsOnMethods = "createNewUserWithObject")
    void updateUser(){
requestBody.setName("Angel");
requestBody.setEmail(createRandomEmail());

        given()
                .spec(requestSpecification)
                .pathParam("userId", responseBody.path("id"))
                .body(requestBody)
                .when()
                .put("/{userId}")
                .then()
                .statusCode(200)
                .spec(responseSpecification)
                .body("name",equalTo(requestBody.getName()));
        System.out.println(requestBody.getName());
    }

    /**
     * Delete the user we created in createAUserWithObjects
     **/

    @Test(dependsOnMethods = "createNewUserWithObject")
    void deleteUser(){
        given()
                .spec(requestSpecification)
                .pathParam("userId",responseBody.path("id"))
                .when()
                .delete("/{userId}")
                .then()
                .statusCode(204);
    }

    /**
     * create delete user negative test
     **/

    @Test(dependsOnMethods = {"createNewUserWithObject","deleteUser"})
    void deleteUserNegativeTest(){

        given()
                .spec(requestSpecification)
                .pathParam("userId",responseBody.path("id"))
                .when()
                .delete("/{userId}")
                .then()
                .statusCode(404);
    }


}
