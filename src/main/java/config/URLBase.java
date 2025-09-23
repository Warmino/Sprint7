package config;

import io.restassured.RestAssured;

public class URLBase {
    public static void setUp(){
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }
}
