import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.is;

public class UserCreateTest {
    private StellarBurgerClient client = new StellarBurgerClient();
    private User user;
    private String token;

    @Test
    @DisplayName("Создание пользователя")
    public void createUser_success() {
        user = new User(System.currentTimeMillis() + "@mail.ru", "password", "Username");
        ValidatableResponse response = client.createUser(user);
        token = response.extract().jsonPath().getString("accessToken");
        response.assertThat()
                .statusCode(200)
                .body("success", is(true));
    }


    @Test
    @DisplayName("Проверка на невозможность создания дубликата")
    public void createUserDuplicate() {
        createUser_success();
        user = new User(user.getEmail(), user.getPassword(), user.getName());
        ValidatableResponse response = client.createUser(user);
        token = response.extract().jsonPath().getString("accessToken");
        response.assertThat()
                .statusCode(403)
                .body("success", is(false));
    }

    @Test
    @DisplayName("Невозможность создание пользователя без поля пароля")
    public void createUserWithoutPass() {
        user = new User(System.currentTimeMillis() + "@mail.ru", null, "Username");
        ValidatableResponse response = client.createUser(user);
        response.assertThat()
                .statusCode(403)
                .body("success", is(false));
    }
    @After
    public void deleteUser_afterTest() {
        if (token != null) {
            client.deleteUser(token);
        }
    }
}

