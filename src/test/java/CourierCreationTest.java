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
import static org.hamcrest.Matchers.is;

public class CourierCreationTest {
    private final CourierSteps courierSteps = new CourierSteps();
    private Faker faker = new Faker();

    @Before
    public void setUp(){
        courierSteps.setUp();
        Courier courier = new Courier(faker.name().username(), faker.internet().password(), faker.name().fullName());
        courierSteps.setCourier(courier);
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Проверка успешного создания курьера")
    public void successfulCourierCreation(){
        courierSteps.createCourier()
                .then().statusCode(SC_CREATED)
                .and()
                .assertThat().body("ok", is(true));
    }

    @Test
    @DisplayName("Создания уже существующего курьера")
    @Description("Проверка на создание существующего курьера ")
    public void duplicateCourierCreation(){
        Courier originalCourier = new Courier(faker.name().username(), faker.internet().password(), faker.name().fullName());
        courierSteps.setCourier(originalCourier);
        courierSteps.createCourier();
        courierSteps.setCourier(originalCourier);
        courierSteps.createCourier()
                .then().statusCode(SC_CONFLICT)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка на создание без логина")
    public void invalidCourierCreationWithoutLogin(){
        Courier courier = new Courier("", faker.internet().password(), faker.name().fullName());
        courierSteps.setCourier(courier);
        courierSteps.createCourier()
                .then().statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Создание курьера без логина и пароля ")
    @Description("Проверка на создание без логина и пароля")
    public void createCourierWithoutLoginAndPassword(){
        Courier courier = new Courier("", "", faker.name().fullName());
        courierSteps.setCourier(courier);
        courierSteps.createCourier()
                .then().statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка на создание без пароля")
    public void invalidCourierCreationWithoutPassword(){
        Courier courier = new Courier(faker.name().username(), "", faker.name().fullName());
        courierSteps.setCourier(courier);
        courierSteps.createCourier()
                .then().statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без указания имени ")
    @Description("Проверка создании курьера без имени")
    public void invalidCourierCreationWithoutName() {
        Courier courier = new Courier(faker.name().username(), faker.internet().password(), "");
        courierSteps.setCourier(courier);
        courierSteps.createCourier()
                .then().statusCode(SC_CREATED)
                .and()
                .assertThat().body("ok", is(true));
    }

    @After
    public void tearDown(){
        courierSteps.deleteCourier();
    }
}