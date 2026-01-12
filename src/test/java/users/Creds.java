package users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Creds {
    private String email;
    private String password;

    private static final Faker faker = new Faker();

    public static Creds getCreds(Users one) {
        return new Creds(one.getEmail(), one.getPassword());
    }

    public static Creds withoutPassword() {
        return new Creds(faker.internet().emailAddress(),
                null);
    }

    public static Creds withoutEmail() {
        return new Creds(null,
                "1234");
    }
}
