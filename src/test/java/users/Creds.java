package users;

import lombok.Value;
import net.datafaker.Faker;

@Value
public class Creds {
    String email;
    String password;

    private static final Faker faker = new Faker();

    public static Creds getCreds(Users one) {
        return new Creds(one.getEmail(), one.getPassword());
    }

    public static Creds withoutPassword() {
        return new Creds( faker.internet().emailAddress(),
                null);
    }

    public static Creds withoutEmail() {
        return new Creds(null,
                "1234");
    }
}
