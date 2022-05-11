package br.com.diego.libraryapi.integration.config;

import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.*;

public class BaseAPI {

    @BeforeEach
    public void preCondition() {

        baseURI = "http://localhost";
        port = 8085;
        basePath = "/api/v1/library";

        enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
