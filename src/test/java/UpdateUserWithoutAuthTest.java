import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class UpdateUserWithoutAuthTest {
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
                .body("success", is(true));
    }

    @Test
    public void updateEmailAndName_fail() {
        String newEmail = "new_" + user.getEmail();
        String newName = "New_" + user.getName();
        User updatedUser = new User(newEmail, user.getPassword(), newName);
        // Отправляем запрос на обновление данных пользователя без авторизации
        ValidatableResponse response = client.updateUserWithoutAuth(updatedUser);
        response.assertThat().statusCode(401).body("success", is(false));
    }

    @After
    public void deleteUser_afterTest() {
        if (token != null) {
            client.deleteUser(token);
        }
    }
}

