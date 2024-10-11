import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class UserLoginTest {
    private StellarBurgerClient client = new StellarBurgerClient();
    private User user;
    private String token;

    @Before
    public void createUser_success() {
        user = new User(System.currentTimeMillis() + "@mail.ru", "password", "Username");
        ValidatableResponse response = client.createUser(user);
        token = response.extract().jsonPath().getString("accessToken");
        response.assertThat()
                .statusCode(200)
                .body("success",is(true));
    }


    @Test
    public void loginUser_success() {

        UserCredentials creds = UserCredentials.fromUser(user, token);
        ValidatableResponse response = client.loginUser(creds);

        response.assertThat().statusCode(200).body("success", is(true));
    }
    @Test
    public void loginUserWithoutPass() {

        UserCredentials creds = UserCredentials.withoutPassword(user.getEmail(),token);
        ValidatableResponse response = client.loginUser(creds);

        response.assertThat().statusCode(401).body("success", is(false));
    }

    @After
    public void deleteUser_afterTest() {
        if (token != null) {
            client.deleteUser(token);

        }
    }
}
