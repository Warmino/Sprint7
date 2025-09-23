import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;
import com.github.javafaker.Faker;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierAuthenticationTest {
    private final CourierSteps courierSteps = new CourierSteps();
    private final Faker faker = new Faker();

    @Before
    public void setUp(){
        courierSteps.setUp();

        Courier validCourier = new Courier(faker.name().username(), faker.internet().password());
        courierSteps.setCourier(validCourier);
        courierSteps.createCourier();
    }
    @Test
    @DisplayName("Авторизация курьера в системе")
    @Description("Проверка авторизации с валидными значениями логина и паролем")
    public void successfulCourierAuthorization(){
        courierSteps.loginCourier()
                .then()
                .statusCode(SC_OK)
                .and()
                .assertThat().body("id", notNullValue());
    }

    @Test
    @DisplayName("Авторизация без логина")
    @Description("Проверка невозможности авторизоваться без указания логина")
    public void unauthorizedCourierLoginWithoutUsername(){
        Courier invalidCourier = new Courier("", faker.internet().password());
        courierSteps.setCourier(invalidCourier);
        courierSteps.loginCourier()
                .then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без пароля")
    @Description("Проверка авторизации без пароля")
    public void unauthorizedCourierLoginWithoutPassword(){
        Courier invalidCourier = new Courier(faker.name().username(), "");
        courierSteps.setCourier(invalidCourier);
        courierSteps.loginCourier()
                .then()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без учетной записии")
    @Description("Проверка авторизации без учетной записии")
    public void unauthorizedLoginWithIncorrectAccountDetails(){
        Courier invalidCourier = new Courier(faker.name().username(), faker.internet().password());
        courierSteps.setCourier(invalidCourier);
        courierSteps.loginCourier()
                .then()
                .statusCode(SC_NOT_FOUND)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с  неверного логина ")
    @Description("Проверка авторизации с неверным логином")
    public void unauthorizedCourierLoginWithInvalidUsername() {
        Courier invalidCourier = new Courier(faker.name().username(), faker.internet().password());
        courierSteps.setCourier(invalidCourier);
        courierSteps.loginCourier()
                .then()
                .statusCode(SC_NOT_FOUND)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем ")
    @Description("Проверка авторизации с неверного пароля")
    public void unauthorizedCourierLoginWithInvalidPassword() {
        Courier invalidCourier = new Courier(faker.name().username(), faker.internet().password());
        courierSteps.setCourier(invalidCourier);
        courierSteps.loginCourier()
                .then()
                .statusCode(SC_NOT_FOUND)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }
    @After
    public void cleanData(){
        courierSteps.deleteCourier();
    }
}