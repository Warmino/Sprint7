package steps;

import config.URLBase;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Courier;


public class CourierSteps extends URLBase {
    private Courier courier;
    private final static String CREATE_ENDPOINT_COURIER = "/api/v1/courier";
    private final static String LOGIN_ENDPOINT_COURIER ="/api/v1/courier/login";

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    @Step("Создать курьера")
    public Response createCourier(){
        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post (CREATE_ENDPOINT_COURIER);
    }

    @Step("Авторизовать курьера")
    public Response loginCourier(){
        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post (LOGIN_ENDPOINT_COURIER);
    }

    @Step("Удалить курьера")
    public void deleteCourier(){
        Integer id =
                RestAssured.given()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .when()
                        .post (LOGIN_ENDPOINT_COURIER)
                        .then().extract().body().path("id");
        if (id != null) {
            RestAssured.given()
                    .delete (CREATE_ENDPOINT_COURIER+ "/{id}", id.toString());}
    }

}