import POJOClasses.Location;
import POJOClasses.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    void test1() {

        given() // Preparation(token, request body, paramaters, cookies...)
                .when() // for URL, request method(get,post,put,patch, delete)
                // get, post, put, patch, delete dont belong Postman.
                // They are known as http methods. All programming languages use these methods.
                .then(); // response(response body, tests, extract data from the response)

    }

    @Test()
    void StatusCodeTest() {
        given() // burda herhangi bir token yoktu. ama her turlu given la baslamamiz lazim
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then() // response body, objectin kendivariable leri var. place name post code gibi
                .log().body() // prints the response body to the console
                .log().status() // prints the status of the request
                .statusCode(200); // tests if the status code is the same as the expected.
    }

    @Test
    void contentType() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .contentType(ContentType.JSON); // tests if the response body is in JSON format, assert gibi calisiyor
    }

    @Test
    void checkCountryFromResponseBody() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("country", equalTo("United States")); // body deki country ismi bu mu?


        // Hamcrest:Lets us to test values from the response body.

    }

    // postman                                          Rest Assured
    // pm.postman.json() -> body                        body()
    // pm.response.json().country                       body("country")
    // pm.response,json().places[0].'place name'        body("places[0]."'place name'") // gives the place name varible of the first element of places list
    // burda places bir array cunku^^^

    //Eger variable larda bosluk varsa mesela place name gibi "'place name'" seklinde yaziyoruz


    // Send a request to the same URL "http://api.zippopotam.us/us/90210" and check if the state is California
    @Test
    void checkStateFromResponse() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .statusCode(200)
                .body("places[0].state", equalTo("California"));
    }

    @Test
    void checkStateAbbreviationFromResponse() {
        // Send a request to the same URL "http://api.zippopotam.us/us/90210"
        // and check if the state abbreviation is "CA"

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].'state abbreviation'", equalTo("CA")); // bosluk oldugu icin ' ' kullandik
    }

    @Test
    void bodyArrayHasItem() {
        // Send a request to the same URL "http://api.zippopotam.us/tr/01000"
        // and check if the body has "Buyukdikili Koyu"


        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .body("places.'place name'", hasItem("Büyükdikili Köyü"));
        // When we dont use index, it gets all place names from the response and creates an Array with them.
        // HasItem checks if that Array contains "Büyükdikili Köyü" value in it. index kullansak belli indexe gidecekti


    }

    @Test
    void arraySizeTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places", hasSize(1)); // checks the size of the array in the body.
        // Mesala arrayin size i 10 olmali, bunu boyle kontrol ediyoruz...
    }

    @Test
    void arraySizeTest2() {
        // Send a request to the same URL "http://api.zippopotam.us/tr/01000"
        // and check if the places name array's size 71.

        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .body("places.'place.name'", hasSize(71));
    }

    @Test
    void multipleTests() {
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .statusCode(200)
                .body("places", hasSize(71))
                .body("places.'place name'", hasItem("Büyükdikili Köyü"))
                .contentType(ContentType.JSON);
    }

    @Test
    void pathParameterTest() { // the parameters that are separated with/ are called PATH parameters.

        given() // Parameterlari given da kullaniyorduk, en basttaki testte yaziyor aciklamada
                .pathParams("Country", "us")
                .pathParams("ZipCode", "90210")  // us yerine country kullan, 90210 yerine zipcode kullan gibi
                .log().uri() // prints the request url
                .when()
                .get("http://api.zippopotam.us/{Country}/{ZipCode}")
                .then()
                .log().body()
                .statusCode(200);

    }

    @Test
    void pathParameterTest2() {
        // send a get request for zipcodes between 90210 and 90213 and verify that in all responses the size
        // of the places array is 1

        for (int i = 90210; i <= 90213; i++) {
            given()
                    .pathParams("ZipCode", i) // Assagida i yerine ZipCode yazsin diyoruz.
                    .when()
                    .get("http://api.zippopotam.us/us/{ZipCode}")
                    .then()
                    .log().body()
                    .body("places", hasSize(1)); // checks the size of the array in the body.
        }
    }

    @Test
    void queryParameterTest() { // if the paramter is seperated by '?', it is called query parameter
        given()
                .param("page", 3) // https://gorest.co.in/public/v1/users?page=3 olmasi icin, 3.sayfaya
                .pathParams("APIName", "users")
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/{APIName}")
                .then()
                .log().body()
                .statusCode(200)
                .body("meta.pagination.page", equalTo(3));
    }

    @Test
    void queryParamTest1() {
        // send the same request for the pages between 1-10 and check if
        // the page number we send from request and page number we get from response are the same

        for (int i = 1; i <= 10; i++) {
            given()
                    .param("page", i)
                    .pathParams("APIName", "users")
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/{APIName}")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("meta.pagination.page", equalTo(i));
        }
    }

    @Test(dataProvider = "pageNumbers")
    void queryParamTestWithDataProvider(int page) {
        // send the same request for the pages between 1-10 and check if
        // the page number we send from request and page number we get from response are the same


        given()
                .param("page", page)
                .pathParams("APIName", "users")
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/{APIName}")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("meta.pagination.page", equalTo(page));

    }

    @DataProvider // DataProvider 2D array le calisiyordu.
    public Object[][] pageNumbers() {

        Object[][] pageNumberList = {{1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}};

        return pageNumberList;
    }

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;


    @BeforeClass
    public void setUp() { // bosuna surekli yazmayalim diye
        baseURI = "https://gorest.co.in/public/v1";
        // if the request URL in the request method doesnt have http part, restAssured puts baseURI to the beginning
        // of the URL in the request method.

        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .addPathParams("APIName", "users")
                .addParam("page", 2)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.BODY)
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();


    }

    @Test
    void baseURITest() { // http yi yazmayinca beforeclass calistigi icin, http yi otomatik olarak kendi yaziyo
        given()
                .param("page", 3) // https://gorest.co.in/public/v1/users?page=3 olmasi icin, 3.sayfaya
                .pathParams("APIName", "users")
                .log().uri()
                .when()
                .get("/{APIName}")
                .then()
                .log().body()
                .statusCode(200)
                .body("meta.pagination.page", equalTo(3));
    }

    @Test
    void requestAndResponseSpecTest() {

        given()
                .param("page", 3) // https://gorest.co.in/public/v1/users?page=3 olmasi icin, 3.sayfaya
                .spec(requestSpecification)
                .when()
                .get("/{APIName}")
                .then()
                .body("meta.pagination.page", equalTo(3))
                .spec(responseSpecification);

    }

    // Extract Data from JSON object

    @Test
    void extractStringData() { // extract methodla birlikte bir tane value alabiliyoruz, burada place name
        String placeName = given()
                .pathParams("Country", "us")
                .pathParams("ZipCode", "90210")
                .when()
                .get("http://api.zippopotam.us/{Country}/{ZipCode}")
                .then()
                .log().body()
                .statusCode(200)
                .extract().path("places[0].'place name'"); // with extract method our request returns a value(Not objects)
        // extract returns only one part of the response(the part that we specify in path method)
        // we can assign it to a variable and use it however we want

        System.out.println("placeName= " + placeName);
    }

    @Test
    void extractInteger() {
        int Limit = given()
                .spec(requestSpecification)
                .when()
                .get("/{APIName}")//  https://gorest.co.in/public/v1/users?page=2
                .then()
                .spec(responseSpecification)
                .extract().path("meta.pagination.limit");
        // we are not allowed ta assign an int to a String (cannot assign a type to another type)

        System.out.println("Limit is: " + Limit);
    }

    @Test
    void extractListData() {

        List<Integer> idList = given()
                .spec(requestSpecification)
                .when()
                .get("/{APIName}")//  https://gorest.co.in/public/v1/users?page=2
                .then()
                .spec(responseSpecification)
                .body("data[1].id", equalTo(5296959)) // bunu ben ekledim ve calisti.
                .extract().path("data.id"); // yukarida Buyukdikili ornegindeki gibi, index no girmedigimiz icin
        // direk array olusturuyor. dolayisiyla return bir List olacak
        System.out.println("idList.get: " + idList.size());
        System.out.println("idList.get: " + idList.get(1));
        Assert.assertTrue(idList.contains(5296959));

    }

    // // send get request to https://gorest.co.in/public/v1/users.
    //    // extract all names from data to a list
    @Test
    void extractListData1() {
        List<String> allNames = given()
                .pathParams("APINames", "users")
                .when()
                .get("/{APINames}")
                .then()
                .spec(responseSpecification)
                .extract().path("data.name");

        System.out.println("nameList size: " + allNames.size());
        System.out.println("name list.get(5) " + allNames.get(5));

    }

    @Test
    void extractResponse() {

        Response response = given()
                .pathParams("APINames", "users")
                .when()
                .get("/{APINames}")
                .then()
                .spec(responseSpecification)
                .extract().response(); // returns the entire Response and assigns it to a Response object.
        // By using this object we are able to reach any part of the response.
        // Sanirim birden fazla variable almak icin yapiyoruz.

        int limit = response.path("meta.pagination.limit");
        System.out.println("limit " + limit);

        String current = response.path("meta.pagination.links.current");
        System.out.println("current " + current);

        List<Integer> idList = response.path("data.id");
        System.out.println("idListSize " + idList.size());

        List<String> nameList = response.path("data.name");

        Assert.assertTrue(nameList.contains("Mrs. Anilabh Pothuvaal"));

    }

    // POJO (Plain Old Java Object)
    @Test
    void extractJsonPOJO() {
        Location location = given()
                .pathParams("ZipCode", 90210)
                .when()
                .get("http://api.zippopotam.us/us/{ZipCode}")
                .then()
                .log().body()
                .extract().as(Location.class); // this request extract the entire response and assigns it to location class as a location object
        // we can not extract the body partially (e.g. cannot extract place object seperately)

        System.out.println("location.getPostCode() = " + location.getPostCode());
        System.out.println("location.getCountry() = " + location.getCountry());
        System.out.println("location.getCountryAbbreviation() = " + location.getCountryAbbreviation());
        System.out.println("location.getPlaces().get(0).getPlaceName() = " + location.getPlaces().get(0).getPlaceName());
        System.out.println("location.getPlaces().get(0).getLongitude() = " + location.getPlaces().get(0).getLongitude());
        System.out.println("location.getPlaces().get(0).getState() = " + location.getPlaces().get(0).getState());
        System.out.println("location.getPlaces().get(0).getStateAbbreviation() = " + location.getPlaces().get(0).getStateAbbreviation());
        System.out.println("location.getPlaces().get(0).getLatitude() = " + location.getPlaces().get(0).getLatitude());

        // public class Location{               public class Place{
        //      String post code;                    String place name;
        //      String country;                      String longitude;
        //      String country abbreviation          String state;
        //      List<Place> places;                  String state abbreviation;
        //                                           String lattitude;
        // }                        }
        // bu bir object adi, location, post code varible i country variable i var, ve place diye array i var
        // place listinin also variable lari var, abbrevetion lattidute gibi


    }

    // extract.path() ==> we can extract only one value or list of that values
    //                 String name = extract.path(data[0].name)
    //                 List<String> nameList extract.path(data.name)
    //
    // extract.as() ==> We can extract the entire response body. It doesn't let us to extract one part of the body separately.
    //                  So we need to create classes for the entire body.
    //                  extract.as(Location.class)
    //                  extract.as(Place.class) cannot extract like this
    //
    // extract.jsonPath() ==> We can extract the entire body as well as any part of the body. So if we need only one part of the
    //                        body we don't need to create classes for the entire body
    //                        extract.jsonPath().getObject(Location.class)
    //                        extract.jsonPath().getObject(Place.class)


    @Test
    void extractWithJsonPath() {
        User user = given()
                .when()
                .get("/users")
                .then()
                .log().body()
                .extract().jsonPath().getObject("data[0]", User.class);

        System.out.println("user.getName()= " + user.getName());
        System.out.println("user.getMail()= " + user.getEmail());
        System.out.println("user.getId() = " + user.getEmail());


    }

    @Test
    void extractWithJsonPath2() {
        List<User> userList = given()
                .when()
                .get("/users")
                .then()
                .log().body()
                .extract().jsonPath().getList("data", User.class); // burda List kullandik...

        System.out.println("User.getName()= "+ userList.get(0).getName());
        System.out.println("User.get email= "+userList.get(0).getEmail());
        System.out.println("User get Id= "+ userList.get(0).getId());

    }

    @Test
    void extractWithJsonPath3(){
    Response response=    given()
                .when()
                .get("/users")
                .then()
                .log().body()
                .extract().response();

        System.out.println("response.jsonPath().getInt(\"meta.pagination.page\") = " + response.jsonPath().getInt("meta.pagination.page"));
        response.path("meta.pagination.page");
        System.out.println("response.jsonPath().getString(\"data[1].name\") = " + response.jsonPath().getString("data[1].name"));
        List<User> dataList= response.jsonPath().getList("data",User.class);
        System.out.println("dataList.get(5).getName() = " + dataList.get(5).getName());

    }
}
