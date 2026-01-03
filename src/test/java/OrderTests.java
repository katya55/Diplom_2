import io.restassured.response.ValidatableResponse;
import orders.Order;
import orders.OrderClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import users.Creds;
import users.Users;
import users.UsersClient;

import java.util.ArrayList;
import java.util.List;

public class OrderTests {
    UsersClient usersClient = new UsersClient();
    OrderClient orderClient = new OrderClient();
    Order order = new Order();

    @AfterEach //удалить заказы

    @Test
    @DisplayName("Создание заказа с авторизацией с ингридиентами")
    public void createOrder() {
        Users user = Users.randomUser();
        ValidatableResponse createResponse = usersClient.createUser(user);
        usersClient.checkCreated(createResponse, user);
        //получение логина/пароля
        var creds = Creds.getCreds(user);
        ValidatableResponse loginResponse = usersClient.loginCourier(creds);
        String accessToken = usersClient.checkLogin(loginResponse, user);

        //создание заказа
        List<String> listOfIngredients = orderClient.getIngredients(accessToken);
        List<String> order = new ArrayList<>();
        order.addAll(listOfIngredients);
        ValidatableResponse createResponse1 = orderClient.createOrder(order, accessToken);
        orderClient.checkCreateOrder(createResponse1);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuth() {
        Users user = Users.randomUser();
        ValidatableResponse createResponse = usersClient.createUser(user);
        usersClient.checkCreated(createResponse, user);
        //получение логина/пароля
        var creds = Creds.getCreds(user);
        ValidatableResponse loginResponse = usersClient.loginCourier(creds);
        String accessToken = usersClient.checkLogin(loginResponse, user);

        //создание заказа
        List<String> listOfIngredients = orderClient.getIngredients(accessToken);
        List<String> order = new ArrayList<>();
        order.addAll(listOfIngredients);
        ValidatableResponse createResponse1 = orderClient.createOrderWithoutAuth(order);
        orderClient.checkErrorOrderWithoutAuth(createResponse1);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        Users user = Users.randomUser();
        ValidatableResponse createResponse = usersClient.createUser(user);
        usersClient.checkCreated(createResponse, user);
        //получение логина/пароля
        var creds = Creds.getCreds(user);
        ValidatableResponse loginResponse = usersClient.loginCourier(creds);
        String accessToken = usersClient.checkLogin(loginResponse, user);

        //создание заказа
        List<String> listOfIngredients = orderClient.getIngredients(accessToken);
        List<String> order = new ArrayList<>();
        ValidatableResponse createResponse1 = orderClient.createOrder(order, accessToken);
        orderClient.checkErrorOrderWithoutIngredients(createResponse1);
    }

    @Test
    @DisplayName("Создание заказа c неверным хешем ингредиентов")
    public void createOrderWithIncorrectIngredients() {
        Users user = Users.randomUser();
        ValidatableResponse createResponse = usersClient.createUser(user);
        usersClient.checkCreated(createResponse, user);
        //получение логина/пароля
        var creds = Creds.getCreds(user);
        ValidatableResponse loginResponse = usersClient.loginCourier(creds);
        String accessToken = usersClient.checkLogin(loginResponse, user);

        //создание заказа
        List<String> listOfIngredients = orderClient.getIngredients(accessToken);
        List<String> invalidIngredients = List.of("invalid_id_1", "12345_fake_id");
        ValidatableResponse createResponse1 = orderClient.createOrder(invalidIngredients, accessToken);
        orderClient.checkOrderWithIncorrectIngredients(createResponse1);
    }

    @Test
    @DisplayName("Получение заказов авторизованного конкретного пользователя")
    public void getOrdersSpecificUser() {
        Users user = Users.randomUser();
        ValidatableResponse createResponse = usersClient.createUser(user);
        usersClient.checkCreated(createResponse, user);
        //получение логина/пароля
        var creds = Creds.getCreds(user);
        ValidatableResponse loginResponse = usersClient.loginCourier(creds);
        String accessToken = usersClient.checkLogin(loginResponse, user);

        //создание заказа
        List<String> listOfIngredients = orderClient.getIngredients(accessToken);
        List<String> order = new ArrayList<>();
        order.addAll(listOfIngredients);
        ValidatableResponse createResponse1 = orderClient.createOrder(order, accessToken);
        orderClient.checkCreateOrder(createResponse1);

        //получение заказа
        ValidatableResponse createResponse2 = orderClient.getOrdersSpecificUser(accessToken);
        orderClient.checkOrdersSpecificUser(createResponse2);

    }

    @Test
    @DisplayName("Получение заказов неавторизованного конкретного пользователя")
    public void getOrdersSpecificUserWithoutAuth() {
        Users user = Users.randomUser();
        ValidatableResponse createResponse = usersClient.createUser(user);
        usersClient.checkCreated(createResponse, user);
        //получение логина/пароля
        var creds = Creds.getCreds(user);
        ValidatableResponse loginResponse = usersClient.loginCourier(creds);
        String accessToken = usersClient.checkLogin(loginResponse, user);

        //создание заказа
        List<String> listOfIngredients = orderClient.getIngredients(accessToken);
        List<String> order = new ArrayList<>();
        order.addAll(listOfIngredients);
        ValidatableResponse createResponse1 = orderClient.createOrder(order, accessToken);
        orderClient.checkCreateOrder(createResponse1);

        //получение заказа
        ValidatableResponse createResponse2 = orderClient.getOrdersSpecificUserWithoutAuth();
        orderClient.checkErrorGetOrderWithoutAuth(createResponse2);

    }
}
