package steps;

import config.URLBase;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps extends URLBase {
    private Order order;
    private final static String ENDPOINT_ORDERS = "/api/v1/orders";
    private final static String ENDPOINT_CANCEL_ORDER = "/api/v1/orders/cancel";

    public void setOrder(Order order) {
        this.order = order;
    }


    public static void cancelOrder(Long trackNumber) {
        given()
                .header("Content-Type", "application/json")
                .body("{\"track\": " + trackNumber + "}")
                .when()
                .put(ENDPOINT_CANCEL_ORDER)
                .then()
                .log().all();
    }


    @Step("Создать заказ, проверить код ответа и номер заказа(track)")
    public Response createOrderSteps(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(order)
                        .when()
                        .post (ENDPOINT_ORDERS);
        return response;
    }

    @Step("Получить список заказов, проверить что он не пустой и код ответа")
    public Response getOrderSteps(){
        return given()
                .get (ENDPOINT_ORDERS);
    }
}