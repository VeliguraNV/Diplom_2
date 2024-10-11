import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class CreateOrderTest {
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
    @Test
    public void createOrderWithAuth_success() {
        createUserAndLogin();
        ValidatableResponse response1 = client.createOrder(StellarBurgerClient.CRATER_BUN_ID);
        response1.assertThat().statusCode(200).body("success", is(true));
    }
    @Test
    public void createOrderWithoutAuth_success() {
        ValidatableResponse response = client.createOrder(StellarBurgerClient.CRATER_BUN_ID);
        response.assertThat().statusCode(200).body("success", is(true)); //баг с 200 статус кодом
    }
    @Test
    public void createOrderWithAuthAndNoIngredients_fail(){
        createUserAndLogin();
        ValidatableResponse response = client.createOrderWithNoIngredients(" ");
        response.assertThat()
                .statusCode(400)
                .body("success", is(false));
    }
    @Test
    public void createOrderWithAuthAndInvalidIngredient_fail() {
        createUserAndLogin();
        ValidatableResponse response = client.createOrderWithNoIngredients(StellarBurgerClient.INVALID_INGREDIENT_ID);
        response.assertThat()
                .statusCode(500);
    }
    @After
    public void deleteUser_afterTest () {
        if (token != null) {
            client.deleteUser(token);
        }
    }
}
