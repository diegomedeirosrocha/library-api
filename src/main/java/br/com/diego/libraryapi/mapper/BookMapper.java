package br.com.diego.libraryapi.mapper;

import br.com.diego.libraryapi.dtos.BookDto;
import br.com.diego.libraryapi.models.Book;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public Book dtoToEntity(BookDto bookDto) {
        if (bookDto == null) {
            return null;
        } else {
            Book book = new Book();
            BeanUtils.copyProperties(bookDto, book);
            return book;
        }
    }

    public BookDto entityToDto(Book book) {
        if (book == null) {
            return null;
        } else {
            BookDto bookDto = new BookDto();
            BeanUtils.copyProperties(book, bookDto);
            return bookDto;
        }
    }

    public List<BookDto> entityListToDtos(List<Book> books) {
        return books
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}
