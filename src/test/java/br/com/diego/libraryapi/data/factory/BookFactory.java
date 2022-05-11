package br.com.diego.libraryapi.data.factory;

import br.com.diego.libraryapi.dtos.BookDto;
import br.com.diego.libraryapi.models.Book;

public class BookFactory {
    public static Book createBookValid() {
        return Book.builder()
                .id(22L)
                .name("Clean Code")
                .year(2000)
                .status("NEW")
                .build();
    }

    public static BookDto createBookDtoValid() {
        return BookDto.builder()
                .id(22L)
                .name("Clean Code")
                .year(2000)
                .status("NEW")
                .build();
    }

    public static Book createBookValid2() {
        return Book.builder()
                .id(24L)
                .name("Jornada Java")
                .year(2020)
                .status("NEW")
                .build();
    }

    private BookDto createBookDtoValid2() {
        return BookDto.builder()
                .id(23L)
                .name("Clean Architeture")
                .year(2009)
                .status("NEW")
                .build();
    }
}
