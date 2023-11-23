package br.com.diego.libraryapi.integration;

import br.com.diego.libraryapi.LibraryApiApplication;
import br.com.diego.libraryapi.data.factory.BookFactory;
import br.com.diego.libraryapi.dtos.BookDto;
import br.com.diego.libraryapi.integration.commons.BookRestImpl;
import br.com.diego.libraryapi.integration.config.BaseAPI;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.util.Objects;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@ActiveProfiles("integration")
@SpringBootTest(classes = LibraryApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class Contract extends BaseAPI {

    @Autowired
    BookRestImpl bookRestImpl;

    public static File getFileFromResource(String filePath) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return new File(Objects.requireNonNull(classloader.getResource(filePath)).getFile());
    }

    @Test
    @DisplayName("devo validar contrato - create book valid")
    void validSchemaPostV1CreateBookValid() {
        BookDto bookDto = BookFactory.createBookDtoValid();
        bookDto.setId(1999L);

        Response response = bookRestImpl.create(bookDto);

        response.then()
                .assertThat()
                    .statusCode(201)
                    .body(matchesJsonSchemaInClasspath("schemas//postV1CreateBookValido.json"))
                    .log().all();
    }

}