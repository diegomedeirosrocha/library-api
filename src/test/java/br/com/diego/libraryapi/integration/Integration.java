package br.com.diego.libraryapi.integration;

import br.com.diego.libraryapi.LibraryApiApplication;
import br.com.diego.libraryapi.data.factory.BookFactory;
import br.com.diego.libraryapi.dtos.BookDto;
import br.com.diego.libraryapi.integration.commons.BookRestImpl;
import br.com.diego.libraryapi.integration.config.BaseAPI;
import br.com.diego.libraryapi.models.Book;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("integration")
@SpringBootTest(classes = LibraryApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class Integration extends BaseAPI {

    @Autowired
    BookRestImpl bookRestImpl;

    @Test
    @DisplayName("livro validao, deveria cadastrar o livro com sucesso")
    void mustCreatBook() {
        BookDto bookDto = BookFactory.createBookDtoValid();
        bookDto.setId(99L);
        Response response = bookRestImpl.cadastrar(bookDto);

        response.then()
                .assertThat()
                .statusCode(201)
                .body("id", equalTo(99))
                .body("name", equalTo("Clean Code"))
                .body("status", equalTo("NEW"))
                .body("year", equalTo(2000));
    }

    @Test
    @DisplayName("com id nulo, deveria retornar erro 400 no cadastro de livro")
    void mustReturError500CreatBook() {
        BookDto bookDto = BookFactory.createBookDtoValid();
        bookDto.setId(null);
        Response response = bookRestImpl.cadastrar(bookDto);

        response.then()
                .assertThat()
                .statusCode(400);
    }


    @ParameterizedTest
    @ValueSource(strings = {"id", "name", "status"})
    @DisplayName("campo null, devo retornar isBadRequest")
    void mustReturnError400(String field) {
        BookDto bookDto = BookFactory.createBookDtoValid();
        switch (field) {
            case "id":
                bookDto.setId(null);
                break;
            case "name":
                bookDto.setName(null);
                break;
            case "status":
                bookDto.setStatus(null);
                break;
            default:
                Assertions.fail("field dont parametrized");
        }

        Book book = new Book();
        BeanUtils.copyProperties(bookDto, book);

        Response response = bookRestImpl.cadastrar(bookDto);

        response.then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DisplayName("devo retornar livro pesquisado")
    void mustReturnBook() {
        Response response = bookRestImpl.get("1");

        response.then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", equalTo("Codigo Limpo"))
                .body("status", equalTo("NEW"))
                .body("year", equalTo(2007));
    }

    @Test
    @DisplayName("devo retornar lista livros")
    void mustReturnAllBooks() {
        Response response = bookRestImpl.getAll();

        List<BookDto> bookDtos = response
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath().getList(".", BookDto.class);

        assertThat(bookDtos).extracting("id", "name", "status", "year")
                .containsOnly(tuple(1L, "Codigo Limpo", "NEW", 2007),
                        tuple(2L, "Lessons Learning", "USED", 2000),
                        tuple(3L, "Jornada Java", "NEW", 2020),
                        tuple(4L, "Developing Testing", "NEW", 2017))
                .hasSize(4);
    }

    //TODO DELETE

    //TODO UPDATE

}
