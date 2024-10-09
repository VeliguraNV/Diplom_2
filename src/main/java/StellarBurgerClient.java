import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class StellarBurgerClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    public static final String CRATER_BUN_ID = "61c0c5a71d1f82001bdaaa6c";
    public static final String INVALID_INGREDIENT_ID = "{\n \"ingredients\": [\"1234543254\"]\n" +
            "}";

    public ValidatableResponse createUser(User user) {
        return given()
                .log()
                .all()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(user)
                .post("/api/auth/register")
                .then()
                .log()
                .all();
    }

    public ValidatableResponse loginUser(UserCredentials credentials) {
        return given()
                .log()
                .all()
                .auth()
                .oauth2(credentials.getToken())
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(credentials)
                .post("/api/auth/login")
                .then()
                .log()
                .all();
    }


    public void deleteUser(String token) {
        given()
                .log()
                .all()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", token) // Добавляем токен в заголовок
                .delete("/api/auth/user")
                .then()
                .log()
                .all();
    }

    public ValidatableResponse updateUser(User originalUser, User updatedUser, String token) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(updatedUser)
                .patch("/api/auth/user")
                .then()
                .log().all();
    }
    public ValidatableResponse createOrder(String ingredient) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body("{\n \"ingredients\": [\"" + ingredient + "\"] \n}")
                .post("/api/orders")
                .then().log().all();
    }

    public ValidatableResponse createOrderWithNoIngredients(String ingredient) {
        if (ingredient == " ") {
            return given()
                    .log().all()
                    .baseUri(BASE_URL)
                    .header("Content-type", "application/json")
                    .body("{ }")
                    .post("/api/orders")
                    .then().log().all();
        } else {
            return given()
                    .log().all()
                    .baseUri(BASE_URL)
                    .header("Content-type", "application/json")
                    .body(INVALID_INGREDIENT_ID)
                    .post("/api/orders")
                    .then().log().all();
        }
    }
    public ValidatableResponse getUserOrders(String token){
        return given()
                .log().all()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .get("/api/orders")
                .then().log().all();
    }

}



