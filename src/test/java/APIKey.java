import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class APIKey {

    // Mesela Google a gidiyorum, ben senin senin Map ini kullanmak istiyorum diyorum onlarda bana su kadar ucret
    // karsiliginda Map i kullananilirsin diyor. Parati oduyorum ve bana bu Map i kullanmam icin bir Api Key veriyor.

    // url: https://l9njuzrhf3.execute-api.eu-west-1.amazonaws.com/prod/user
    // key: x-api-key
    // value: GwMco9Tpstd5vbzBzlzW9I7hr6E1D7w2zEIrhOra

    @Test
    void apiKeyTest() {
        given()
                .header("x-api-key", "GwMco9Tpstd5vbzBzlzW9I7hr6E1D7w2zEIrhOra")
                .when()
                .get("https://l9njuzrhf3.execute-api.eu-west-1.amazonaws.com/prod/user")
                .then()
                .log().body()
                .statusCode(200);
    }

    //Use https://www.weatherapi.com/docs/ as a reference.
    //First, You need to signup to weatherapi.com, and then you can find your API key under your account
    //after that, you can use Java to request: http://api.weatherapi.com/v1/current.json?key=[YOUR-API]&q=Indianapolis&aqi=no
    //Parse the json and print the current temperature in F and C.

    // Mesela bir Weather app i kuruyorum, bu siteye gidip sizin data larinizi kullanabilir miyim diye soruyorum onlarda
    // bana su kadar fiyat sonra kullanabilirsin diyor, API Key veriyorlar

    @Test
    void weatherAPI() {

        Response response = given()
                .param("key", "b2b9681a1a3946e694a201643231507") // your API
                .param("q", "Orlando") // q= Indiana dedigi yer
                .param("aqi", "no") // = aqi=no demis yukarda...
                .log().uri() // send ettigimizdde hakkaten yukardakinin aynisi mi diye yazdik bu satiri...
                .when()
                .get("http://api.weatherapi.com/v1/current.json")
                .then()
                .statusCode(200)
                .extract().response();

        float temp_C=response.path("current.temp_c");
        float temp_F= response.path("current.temp_f");

        System.out.println("temp_F = " + temp_F);
        System.out.println("temp_C = " + temp_C);
    }


}
