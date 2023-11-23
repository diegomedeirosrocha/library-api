package br.com.diego.libraryapi.integration;

import br.com.diego.libraryapi.LibraryApiApplication;
import br.com.diego.libraryapi.integration.config.BaseAPI;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.given;

@ActiveProfiles("integration")
@SpringBootTest(classes = LibraryApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class HealtCheckTest extends BaseAPI {

    @Test
    @DisplayName("devo retornar 200 - healthCheck")
    void healthCheckReturn200() {
        basePath = "/actuator/health";

        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setContentType("application/json")
                .setAccept("*/*")
                .build();

        given().log().all()
                .spec(requestSpec)
                .get()
                .then()
                    .assertThat()
                    .statusCode(200);
    }

}
