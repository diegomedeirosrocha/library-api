package br.com.diego.libraryapi.unit.mapper;

import br.com.diego.libraryapi.dtos.BookDto;
import br.com.diego.libraryapi.mapper.BookMapper;
import br.com.diego.libraryapi.models.Book;
import br.com.diego.libraryapi.data.factory.BookFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@RunWith(SpringRunner.class)
@SpringBootTest
class BookMapperTest {
    @Autowired
    BookMapper bookMapper;

    @Test
    @DisplayName("convert dto to entity - sucess")
    void validarConverterDtoToEntity() {
        BookDto bookDto = BookFactory.createBookDtoValid();

        Book book = bookMapper.dtoToEntity(bookDto);

        Assert.notNull(book);
        assertThat(book.getId()).isEqualTo(22L);
        assertThat(book.getName()).isEqualTo("Clean Code");
        assertThat(book.getStatus()).isEqualTo("NEW");
        assertThat(book.getYear()).isEqualTo(2000);
    }

    @Test
    @DisplayName("convert dto to entity null - sucess")
    void validarConverterDtoToEntityImputNull() {
        Book book = bookMapper.dtoToEntity(null);

        Assert.isNull(book);
    }

    @Test
    @DisplayName("save book - sucess")
    void validarConverterEnityToDto() {
        Book book = BookFactory.createBookValid();

        BookDto bookDto = bookMapper.entityToDto(book);

        Assert.notNull(bookDto);
        assertThat(bookDto.getId()).isEqualTo(22L);
        assertThat(bookDto.getName()).isEqualTo("Clean Code");
        assertThat(bookDto.getStatus()).isEqualTo("NEW");
        assertThat(bookDto.getYear()).isEqualTo(2000);
    }


    @Test
    @DisplayName("convert entity to dto null - sucess")
    void validarConverterEntityToDtoImputNull() {
        BookDto bookDto = bookMapper.entityToDto(null);

        Assert.isNull(bookDto);
    }

    @Test
    @DisplayName("convert entityListToDtos - sucess")
    void convertListEntityListoToDtosSucess() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(BookFactory.createBookValid());
        bookList.add(BookFactory.createBookValid2());

        List<BookDto> bookDtos = bookMapper.entityListToDtos(bookList);

        assertThat(bookDtos)
                .isNotEmpty()
                .extracting("id", "name", "status", "year")
                .contains(tuple(22L, "Clean Code", "NEW", 2000),
                        tuple(24L, "Jornada Java", "NEW", 2020))
                .hasSize(2);
    }
}
