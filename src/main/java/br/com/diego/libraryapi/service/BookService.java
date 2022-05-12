package br.com.diego.libraryapi.service;

import br.com.diego.libraryapi.dtos.BookDto;
import br.com.diego.libraryapi.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book saveBook(BookDto bookDto);

    List<Book> getAllBooks();

    Optional<Book> getBookById(long id);

    void deleteBook(Book book);

    Book update(BookDto bookDto);
}
