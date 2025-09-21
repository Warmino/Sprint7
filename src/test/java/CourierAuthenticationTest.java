import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;

import static org.apache.http.HttpStatus.*;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierAuthenticationTest {
    CourierSteps CourierSteps = new CourierSteps();
    Courier courier = new Courier ("Anton","12345");

    @Before
    public void setUp(){
        CourierSteps.setUp();
    }
    @Test
    @DisplayName("Авторизация курьера в системе")
    @Description("Проверка авторизации с валидными значениями логина и паролем")
    public void successfulCourierAuthorization(){
        CourierSteps.setCourier(courier);
        CourierSteps.createCourier();
        CourierSteps.loginCourier()
                .then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Авторизация без логина")
    @Description("Проверка невозможности авторизоваться без указания логина")
    public void unauthorizedCourierLoginWithoutUsername(){
        CourierSteps.setCourier(new Courier("","12345"));
        CourierSteps.loginCourier()
                .then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Авторизация без пароля")
    @Description("Проверка авторизации без пароля")
    public void unauthorizedCourierLoginWithoutPassword(){
        CourierSteps.setCourier(new Courier("SuperCourier",""));
        CourierSteps.loginCourier()
                .then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Авторизация без учетной записии")
    @Description("Проверка авторизации без учетной записии")
    public void unauthorizedLoginWithIncorrectAccountDetails(){
        CourierSteps.setCourier(courier);
        CourierSteps.loginCourier()
                .then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Авторизация с  неверного логина ")
    @Description("Проверка авторизации с неверным логином")
    public void unauthorizedCourierLoginWithInvalidUsername() {
        CourierSteps.setCourier(courier);
        CourierSteps.createCourier();
        CourierSteps.setCourier(new Courier("Ivan","12345"));
        CourierSteps.loginCourier()
                .then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(SC_NOT_FOUND);
        CourierSteps.setCourier(courier);
    }

    @Test
    @DisplayName("Авторизация с неверным паролем ")
    @Description("Проверка авторизации с неверного пароля")
    public void unauthorizedCourierLoginWithInvalidPassword() {
        CourierSteps.setCourier(courier);
        CourierSteps.createCourier();
        CourierSteps.setCourier(new Courier("Anton","1234"));
        CourierSteps.loginCourier()
                .then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(SC_NOT_FOUND);
        CourierSteps.setCourier(courier);
    }
    @After
    public void cleanData(){
        CourierSteps.deleteCourier();
    }

}