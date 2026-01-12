package users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import net.datafaker.Faker;

import java.util.concurrent.ThreadLocalRandom;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUser {

    private String email;
    private String name;

    private static final Faker faker = new Faker();

    public static UpdateUser generateNewDataOfUser() {
        return new UpdateUser(
                faker.internet().emailAddress(),
                faker.name().firstName()
                        + ThreadLocalRandom.current().nextInt(1000, 10000)
        );
    }
}
