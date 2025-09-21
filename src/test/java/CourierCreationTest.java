import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;

import static org.apache.http.HttpStatus.*;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CourierCreationTest {
    Courier courier = new Courier("Anton", "12345", "Ivan");
    CourierSteps CourierSteps = new CourierSteps();
    @Before
    public void setUp(){
        CourierSteps.setUp();
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Проверка успешного создания курьера")
    public void successfulCourierCreation(){
        CourierSteps.setCourier(courier);
        CourierSteps.createCourier()
                .then().assertThat().body("ok", is(true))
                .and()
                .statusCode(SC_CREATED);
    }

    @Test
    @DisplayName("Создания уже существующего курьера")
    @Description("Проверка на создание существующего курьера ")
    public void duplicateCourierCreation(){
        CourierSteps.setCourier(courier);
        CourierSteps.createCourier();
        CourierSteps.createCourier()
                .then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(SC_CONFLICT);
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка на создание без логина")
    public void invalidCourierCreationWithoutLogin(){
        CourierSteps.setCourier(new Courier("","12345","Ivan"));
        CourierSteps.createCourier()
                .then().statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Создание курьера без логина и пароля ")
    @Description("Проверка на создание без логина и пароля")
    public void createCourierWithoutLoginAndPassword(){
        CourierSteps.setCourier(new Courier("","","Ivan"));
        CourierSteps.createCourier()
                .then().statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка на создание без пароля")
    public void invalidCourierCreationWithoutPassword(){
        CourierSteps.setCourier(new Courier("Anton","","Ivan"));
        CourierSteps.createCourier()
                .then().statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без указания имени ")
    @Description("Проверка создании курьера без имени")
    public void invalidCourierCreationWithoutName() {
        CourierSteps.setCourier(new Courier("Anton", "12345", ""));
        CourierSteps.createCourier()
                .then().assertThat().body("ok", is(true))
                .and()
                .statusCode(SC_CREATED);
    }

    @After
    public void tearDown(){

        CourierSteps.deleteCourier();
    }
}