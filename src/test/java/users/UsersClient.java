package users;

import conf.Client;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.net.HttpURLConnection;

import static conf.Envconf.BASE_URL;
import static org.hamcrest.Matchers.equalTo;


public class UsersClient extends Client {

    @Step("Регистрация пользователя")
    public ValidatableResponse createUser(Users users) {
        return spec()
                .baseUri(BASE_URL)
                .body(users)
                .when()
                .post("/auth/register")
                .then().log().all();
    }

    @Step("Пользователь создан")
    public void checkCreated(ValidatableResponse createResponse, Users user) {
        createResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()));
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginCourier(Creds creds) {
        return spec()
                .body(creds)
                .when()
                .post("/auth/login")
                .then().log().all();
    }

    @Step("Пользователь авторизовался")
    public String checkLogin(ValidatableResponse loginResponse, Users user) {
        return loginResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .extract()
                .path("accessToken");
    }


    @Step("Ошибка при создании уже зарегистрированного пользователя")
    public void checkErrorCreateDuplicateUser(ValidatableResponse createResponse) {
        createResponse.assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Step("Ошибка при создании пользователя без email")
    public void checkErrorCreateUserWithoutEmail(ValidatableResponse createResponse) {
        createResponse.assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Step("Ошибка при логине с невалидным email/password")
    public void checkErrorLoginUserWithInvalidCreds(ValidatableResponse createResponse) {
        createResponse.assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Step("Получение данных")
    public ValidatableResponse getDataOfUser(String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .baseUri(BASE_URL)
                .when()
                .get("auth/user")
                .then().log().all();

    }

    @Step("Обновление данных c авторизацией")
    public ValidatableResponse updateDataOfUser(UpdateUser updateUser, String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .baseUri(BASE_URL)
                .body(updateUser)
                .when()
                .patch("auth/user")
                .then().log().all();

    }

    @Step("Данные пользователя успешно обновлены")
    public void checkUserUpdated(ValidatableResponse response, UpdateUser updateUser) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(updateUser.getEmail()))
                .body("user.name", equalTo(updateUser.getName()));
    }

    @Step("Обновление данных без авторизации")
    public ValidatableResponse updateDataOfUserWithoutAuth(UpdateUser updateUser) {
        return spec()
                .baseUri(BASE_URL)
                .body(updateUser)
                .when()
                .patch("auth/user")
                .then().log().all();

    }

    @Step("Ошибка при изменении данных без авторизации")
    public void checkUpdateWithoutAuthError(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Удаление пользователя")
    public void deleteUser(String accessToken) {
        spec()
                .header("Authorization", accessToken)
                .when()
                .delete("auth/user")
                .then()
                .log().all()
                .statusCode(HttpURLConnection.HTTP_ACCEPTED);
    }
}
