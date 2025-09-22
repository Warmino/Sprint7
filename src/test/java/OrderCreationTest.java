import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.OrderSteps;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTest {
    Order order;
    OrderSteps orderSteps = new OrderSteps();
    private Response response;

    public OrderCreationTest(Order order) {
        this.order = order;
    }

    @Before
    public void setUp(){
        OrderSteps.setUp();
    }

    @Parameterized.Parameters
    public static Object[][] getTestData(){
        return new Object[][]{
                {new Order("Анджелина","Джоли","Черкизовская","Кутузовская","+79079437685",2,"2025-09-21","-",new String[]{"BLACK"})},
                {new Order("Дженнифер","Лоуренс","Сокольники","Проспект Мира","+79654530204",3,"2025-09-21","-",new String[]{"GREY"})},
                {new Order("Том ","Круз","Красносельская","Лубянка","+79650549382",4,"2025-09-21","-",new String[]{"BLACK","GREY"})},
                {new Order("Уилл","Смит","Комсомольская","Арбатская","+79050438543",5,"2025-09-21","-",new String[]{})}
        };
    }

    @Test
    @DisplayName("Создание заказа самоката с разным цветом")
    @Description("Проверка заказа самоката с разным цветом ")
    public void checkCreateOrder(){
        orderSteps.setOrder(order);
        response = orderSteps.createOrderSteps()
                .then()
                .statusCode(SC_CREATED)
                .and()
                .assertThat().body("track", notNullValue())
                .extract().response();
    }

    @After
    public void cancelOrder() {

        Long trackNumber = response.jsonPath().getLong("track");


        OrderSteps.cancelOrder(trackNumber);
    }
}