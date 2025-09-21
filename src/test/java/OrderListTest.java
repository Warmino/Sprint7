import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {
    OrderSteps OrderSteps = new OrderSteps();
    @Before
    public void setUp(){
        OrderSteps.setUp();}
    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка получения списка заказов")
    public void checkCreateOrder(){
        OrderSteps.getOrderSteps()
                .then().statusCode(SC_OK)
                .and()
                .assertThat().body("orders", notNullValue());
    }
}