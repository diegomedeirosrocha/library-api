package br.com.diego.libraryapi.controller;

import br.com.diego.libraryapi.dtos.BookDto;
import br.com.diego.libraryapi.exception.BusinessException;
import br.com.diego.libraryapi.mapper.BookMapper;
import br.com.diego.libraryapi.models.Book;
import br.com.diego.libraryapi.service.BookService;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Api("Library API")
@RequestMapping("/api/v1/library")
@RestController
@Slf4j
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private BookMapper bookMapper;

    @SneakyThrows
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@RequestBody @Valid BookDto bookDto) {
        if (bookService.getBookById(bookDto.getId()).isPresent()) {
            throw new BusinessException("Livro ja cadastrado");
        }

        log.info("save book id {} ", bookDto.getId());
        return bookMapper.entityToDto(bookService.saveBook(bookDto));
    }

    @GetMapping
    public List<BookDto> getAll() {
        List<Book> bookDtoList = bookService.getAllBooks();

        if (bookDtoList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        return bookMapper.entityListToDtos(bookDtoList);
    }

    @GetMapping("/{id}")
    public BookDto get(@PathVariable long id) {
        log.info("getBook id: {} ", id);
        return bookService
                .getBookById(id)
                .map(book -> bookMapper.entityToDto(book))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.info(" delete book id: {} ", id);

        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        bookService.deleteBook(book);
    }

    @PutMapping()
    public BookDto update(@RequestBody @Valid BookDto bookDto) {
        Book book = bookService.getBookById(bookDto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return bookMapper.entityToDto(bookService.update(book));
    }
}
