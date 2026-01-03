package users;
import lombok.Value;
import net.datafaker.Faker;

import java.util.concurrent.ThreadLocalRandom;

@Value
public class UpdateUser {

    String email;
    String name;

    private static final Faker faker = new Faker();

    public static UpdateUser generateNewDataOfUser() {
        return new UpdateUser(
                faker.internet().emailAddress(),
                faker.name().firstName()
                        + ThreadLocalRandom.current().nextInt(1000, 10000)
        );
    }
}
