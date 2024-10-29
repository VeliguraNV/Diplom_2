import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.is;

public class UpdateUserWithAuthTest {
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

    @Before
    public void loginUser_success() {

        UserCredentials creds = UserCredentials.fromUser(user, token);
        ValidatableResponse response = client.loginUser(creds);

        response.assertThat().statusCode(200).body("success", is(true));
    }

    @Test
    public void updateEmailAndName_success() {

        String newEmail = "new_" + user.getEmail();
        String newName = "New_" + user.getName();

        User updatedUser = new User(newEmail, user.getPassword(), newName);

        // Отправляем запрос на обновление данных пользователя
        ValidatableResponse response = client.updateUser(updatedUser, token);

        response.assertThat().statusCode(200).body("success", is(true));

        // Проверяем, что email и имя пользователя обновлены
        response.assertThat().body("user.email", is(newEmail)); // Проверка, что email обновлен
        response.assertThat().body("user.name", is(newName));   // Проверка, что имя обновлено
    }

    @After
    public void deleteUser_afterTest() {
        if (token != null) {
            client.deleteUser(token);
        }
    }
}