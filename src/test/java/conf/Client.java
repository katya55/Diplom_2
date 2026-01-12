package conf;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;


public class Client {

    public static RequestSpecification spec() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(Envconf.BASE_URL);
    }
}
