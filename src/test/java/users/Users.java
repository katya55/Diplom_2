package users;

import lombok.Value;
import net.datafaker.Faker;

import java.util.concurrent.ThreadLocalRandom;

@Value
public class Users {
    String email;
    String password;
    String name;

    private static final Faker faker = new Faker();

    public static Users randomUser() {
        return new Users(
                faker.internet().emailAddress(),
                faker.internet().password(),
                faker.name().firstName()
                        + ThreadLocalRandom.current().nextInt(1000, 10000)
        );
    }

    public static Users invalidUser() {
        return new Users(
                null, // email отсутствует
                faker.internet().password(), // пароль случайный
                faker.name().firstName() + ThreadLocalRandom.current().nextInt(1000, 10000) // имя
        );
    }
}
