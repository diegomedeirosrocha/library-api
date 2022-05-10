package br.com.diego.libraryapi.unit.service.impl;

import br.com.diego.libraryapi.dtos.BookDto;
import br.com.diego.libraryapi.models.Book;
import br.com.diego.libraryapi.repository.BookRepository;
import br.com.diego.libraryapi.service.BookService;
import br.com.diego.libraryapi.service.impl.BookServiceImpl;
import br.com.diego.libraryapi.unit.commons.CommonsTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BookServiceImplTest {

    @MockBean
    private BookRepository repository;

    @MockBean
    private BookServiceImpl bookServiceImpl;

    @Autowired
    private BookService service;

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("save book - sucess")
    void salveBook_success() {
        BookDto bookDto = CommonsTest.createBookDtoValid();
        Book book = new Book();
        BeanUtils.copyProperties(bookDto, book);

        Mockito.when(repository.save(any())).thenReturn(book);

        Book bookResponse = service.saveBook(bookDto);

        Assert.notNull(bookResponse);
        assertThat(bookResponse.getId()).isEqualTo(22L);
        assertThat(bookResponse.getName()).isEqualTo("Clean Code");
        assertThat(bookResponse.getStatus()).isEqualTo("NEW");
        assertThat(bookResponse.getYear()).isEqualTo(2000);

        Mockito.verify(repository, times(1)).save(book);
    }

    @Test
    @DisplayName("get all books - sucess")
    void getAllBooks_success() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(CommonsTest.createBookValid());
        bookList.add(createBookValid2());

        Mockito.when(repository.findAll()).thenReturn(bookList);

        List<Book> bookResponse = service.getAllBooks();

        assertThat(bookResponse)
                .isNotEmpty()
                .extracting("id", "name", "status", "year")
                .contains(tuple(22L, "Clean Code", "NEW", 2000),
                        tuple(23L, "Clean Architeture", "NEW", 2009));

        Mockito.verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("save book by id - sucess")
    void getBookById_success() {
        Book book = CommonsTest.createBookValid();

        Mockito.when(repository.findById(22L)).thenReturn(Optional.ofNullable(book));

        Optional<Book> bookResponse = service.getBookById(22);

        Assert.isTrue(bookResponse.isPresent());
        assertThat(bookResponse.get().getId()).isEqualTo(22L);
        assertThat(bookResponse.get().getName()).isEqualTo("Clean Code");
        assertThat(bookResponse.get().getStatus()).isEqualTo("NEW");
        assertThat(bookResponse.get().getYear()).isEqualTo(2000);

        Mockito.verify(repository, times(1)).findById(22L);
    }

    @Test
    @DisplayName("save book by id - empty")
    void getBookById_empty() {
        Mockito.when(repository.findById(22L)).thenReturn(Optional.empty());

        Optional<Book> bookResponse = service.getBookById(22);

        assertThat(bookResponse).isEmpty();
    }

    @Test
    @DisplayName("Delete book sucess")
    void deleteBook_sucess() {
        Book book = CommonsTest.createBookValid();

        service.deleteBook(book);

        Mockito.verify(repository, times(1)).delete(book);
    }

    @Test
    @DisplayName("Update book sucess")
    void updateBook_sucess() {
        Book book = CommonsTest.createBookValid();
        Book bookUpdate = createBookValid2();
        bookUpdate.setId(22L);

        Mockito.when(repository.saveAndFlush(book)).thenReturn(bookUpdate);
        Book bookReturned = service.update(book);

        Assert.notNull(bookUpdate);
        assertThat(bookReturned.getId()).isEqualTo(22L);
        assertThat(bookReturned.getName()).isEqualTo("Clean Architeture");
        assertThat(bookReturned.getStatus()).isEqualTo("NEW");
        assertThat(bookReturned.getYear()).isEqualTo(2009);

        Mockito.verify(repository, times(1)).saveAndFlush(book);
    }

    private Book createBookValid2() {
        return Book.builder()
                .id(23L)
                .name("Clean Architeture")
                .year(2009)
                .status("NEW")
                .build();
    }


}