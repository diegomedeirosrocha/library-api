package br.com.diego.libraryapi.integration.commons;

import br.com.diego.libraryapi.dtos.BookDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.restassured.RestAssured.given;

@Slf4j
@Component
public class BookRestImpl {

    @Getter
    @Setter
    private Response response;

    public Response create(BookDto bookDto) {
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setContentType("application/json")
                .setAccept("*/*")
                .setBody(bookDto)
                .build();

        Response response = given().log().all()
                .spec(requestSpec)
                .post();

        response.then().log().all();

        return response;
    }

    public Response getById(String id) {
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setContentType("application/json")
                .setAccept("*/*")
                .build();

        Response response = given().log().all()
                .spec(requestSpec)
                .get("/ " + id);

        response.then().log().all();

        return response;
    }

    public Response getAll() {
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setContentType("application/json")
                .setAccept("*/*")
                .build();

        Response response = given().log().all()
                .spec(requestSpec)
                .get();

        response.then().log().all();

        return response;
    }

    public List<BookDto> getListBooksDto(String path) {
        return given()
                .when()
                    .get(path)
                .then()
                    .extract()
                        .body()
                        .jsonPath().getList(".", BookDto.class);
    }

    public Response deleteById(String id) {
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setContentType("application/json")
                .setAccept("*/*")
                .build();

        Response response = given().log().all()
                .spec(requestSpec)
                .delete("/ " + id);

        response.then().log().all();

        return response;
    }

    public Response update(BookDto bookDto) {
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setContentType("application/json")
                .setAccept("*/*")
                .setBody(bookDto)
                .build();

        Response response = given().log().all()
                .spec(requestSpec)
                .put();

        response.then().log().all();

        return response;
    }

}
