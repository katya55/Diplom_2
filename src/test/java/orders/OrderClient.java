package orders;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import static conf.Client.spec;
import static conf.Envconf.BASE_URL;

import static org.hamcrest.Matchers.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderClient {

    @Step("Получить список ингридиентов")
    public List<String> getIngredients(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .baseUri(BASE_URL)
                .get("ingredients")
                .then().statusCode(200)
                .extract()
                .path("data._id");
    }



    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrder(List<String> ingredients, String accessToken) {
       return spec()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .body(Map.of("ingredients", ingredients))
                .when()
                .post("orders")
                .then().log().all();
    }

    @Step("Заказа создан")
    public void checkCreateOrder(ValidatableResponse createResponse) {
            createResponse
                    .assertThat()
                    .statusCode(HttpURLConnection.HTTP_OK)
                    .body("success", equalTo(true))
                    .extract()
                    .path("order.number");

    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(List<String> ingredients) {
        return spec()
                .baseUri(BASE_URL)
                .body(Map.of("ingredients", ingredients))
                .when()
                .post("orders")
                .then().log().all()
                .header("Location", equalTo("/login"));
    }


    @Step("Ошибка при создании заказа без авторизации")
    public void checkErrorOrderWithoutAuth(ValidatableResponse createResponse) {
        createResponse.assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));

    }

    @Step("Ошибка при создании заказа без ингредиентов")
    public void checkErrorOrderWithoutIngredients(ValidatableResponse createResponse) {
        createResponse.assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));

    }

    @Step("Ошибка при создании заказа с некорректными хэшами")
    public void checkOrderWithIncorrectIngredients(ValidatableResponse createResponse) {
        createResponse.assertThat()
                .statusCode(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

    @Step("Получение заказов конкретного авторизованного пользователя ")
    public ValidatableResponse getOrdersSpecificUser(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .baseUri(BASE_URL)
                .get("orders")
                .then().statusCode(200);
    }

    @Step("Проверка заказов конкретного пользователя")
    public void checkOrdersSpecificUser(ValidatableResponse createResponse2) {
        createResponse2.assertThat()
                .body("success", equalTo(true))
                .body("orders", not(empty()))
                .body("orders[0]._id", notNullValue());
    }

    @Step("Получение заказов неавторизованного конкретного пользователя")
    public ValidatableResponse getOrdersSpecificUserWithoutAuth() {
        return spec()
                .baseUri(BASE_URL)
                .get("orders")
                .then().statusCode(401);
    }

    @Step("Ошибка при получении заказа пользователя без авторизации")
    public void checkErrorGetOrderWithoutAuth(ValidatableResponse createResponse2) {
        createResponse2.assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));

    }

}
