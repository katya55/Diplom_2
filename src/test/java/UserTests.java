
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import users.Creds;
import users.UpdateUser;
import users.Users;
import users.UsersClient;

import static org.junit.jupiter.api.Assertions.*;


public class UserTests {

    UsersClient usersClient = new UsersClient();
    private String accessToken;

    @AfterEach
    public void dropUser() {
        if (accessToken != null) {
            usersClient.deleteUser(accessToken);
        }
    }

    //Регистрация и логин пользователя
    @Test
    @DisplayName("Успешное создание клиента")
    public void createUniqueUser() {
        Users user = Users.randomUser();
        ValidatableResponse createResponse = usersClient.createUser(user);
        usersClient.checkCreated(createResponse, user);
        //получение логина/пароля
        var creds = Creds.getCreds(user);
        ValidatableResponse loginResponse = usersClient.loginCourier(creds);
        String accessToken = usersClient.checkLogin(loginResponse, user);

        assertNotNull(accessToken);
        assertFalse(accessToken.isEmpty());

    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createDuplicateUser() {
        Users user = Users.randomUser();
        ValidatableResponse createResponse = usersClient.createUser(user);
        usersClient.checkCreated(createResponse, user);

        //получение логина/пароля
        var creds = Creds.getCreds(user);
        ValidatableResponse loginResponse = usersClient.loginCourier(creds);
        ValidatableResponse loginResponse2 = usersClient.loginCourier(creds);
        usersClient.checkErrorCreateDuplicateUser(loginResponse2);
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithoutEmail() {
        Users user = Users.invalidUser();
        ValidatableResponse createResponse = usersClient.createUser(user);
        usersClient.checkErrorCreateUserWithoutEmail(createResponse);
    }

    @Test
    @DisplayName("Авторизация пользователя c некорректным логином")
    public void loginWithInvalidLogin() {
        var creds = Creds.withoutEmail();
        ValidatableResponse loginResponse = usersClient.loginCourier(creds);
        usersClient.checkErrorLoginUserWithInvalidCreds(loginResponse);
    }

    @Test
    @DisplayName("Авторизация пользователя c некорректным паролем")
    public void loginWithInvalidPassword() {
        var creds = Creds.withoutPassword();
        ValidatableResponse loginResponse = usersClient.loginCourier(creds);
        usersClient.checkErrorLoginUserWithInvalidCreds(loginResponse);
    }

    @Test
    @DisplayName("Изменение данных с авторизацией")
    public void changeDataWithAuth() {

        Users user = Users.randomUser();
        ValidatableResponse createResponse = usersClient.createUser(user);
        usersClient.checkCreated(createResponse, user);

        var creds = Creds.getCreds(user);
        ValidatableResponse loginResponse = usersClient.loginCourier(creds);
        accessToken = usersClient.checkLogin(loginResponse, user);

        UpdateUser updateUser = UpdateUser.generateNewDataOfUser();
        ValidatableResponse createResponse2 = usersClient.updateDataOfUser(updateUser, accessToken);
        usersClient.checkUserUpdated(createResponse2, updateUser);
    }

    @Test
    @DisplayName("Изменение данных без авторизации")
    public void changeDataWithoutAuth() {
        Users user = Users.randomUser();
        ValidatableResponse createResponse = usersClient.createUser(user);
        usersClient.checkCreated(createResponse, user);

        var creds = Creds.getCreds(user);
        ValidatableResponse loginResponse = usersClient.loginCourier(creds);
        accessToken = usersClient.checkLogin(loginResponse, user);

        UpdateUser updateUser = UpdateUser.generateNewDataOfUser();
        ValidatableResponse createResponse2 = usersClient.updateDataOfUserWithoutAuth(updateUser);
        usersClient.checkUpdateWithoutAuthError(createResponse2);

    }

}






