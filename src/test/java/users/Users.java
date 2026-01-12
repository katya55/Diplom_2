package users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;

import java.util.concurrent.ThreadLocalRandom;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private String email;
    private String password;
    private String name;

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
