import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class GetUserOrdersTest {
    private StellarBurgerClient client = new StellarBurgerClient();
    private User user;
    private String token;
    private void createUserAndLogin() {
        user = new User(System.currentTimeMillis() + "@mail.ru", "password", "Username");
        ValidatableResponse response = client.createUser(user);
        token = response.extract().jsonPath().getString("accessToken");

        response.assertThat()
                .statusCode(200)
                .body("success", is(true));
    }
    @Before
    public void setUp() {
        createUserAndLogin();
    }

    @After
    public void tearDown() {
        if (token != null) {
            client.deleteUser(token);
        }
    }

    @Test
    public void getUserOrdersWithAuth_success() {
        ValidatableResponse response = client.getUserOrders(token);
        response.assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    public void getUserOrdersWithoutAuth_fail() {
        ValidatableResponse response = client.getUserOrders("");
        response.assertThat()
                .statusCode(401)
                .body("success", is(false));
    }


}