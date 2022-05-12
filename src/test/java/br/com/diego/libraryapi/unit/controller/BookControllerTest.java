package br.com.diego.libraryapi.unit.controller;

import br.com.diego.libraryapi.controller.BookController;
import br.com.diego.libraryapi.dtos.BookDto;
import br.com.diego.libraryapi.mapper.BookMapper;
import br.com.diego.libraryapi.models.Book;
import br.com.diego.libraryapi.repository.BookRepository;
import br.com.diego.libraryapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
class BookControllerTest {

    private final static String PATH_URL = "/api/v1/library";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService service;

    @MockBean
    private BookMapper bookMapper;

    @MockBean
    private BookRepository repository;

    @Test
    @DisplayName("livro valido, entao devo salvar o livro")
    void mustSaveBook() throws Exception {
        BookDto bookDto = createBookValid();
        Book book = dtoToEntity(bookDto);

        BDDMockito.given(service.getBookById(bookDto.getId())).willReturn(Optional.empty());
        BDDMockito.given(service.saveBook(bookDto)).willReturn(book);

        mockMvc.perform(post(PATH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(bookDto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @ValueSource(strings = {"id", "name", "status"})
    @DisplayName("campo null, devo retornar isBadRequest")
    void mustReturnIsBadRequest(String field) throws Exception {
        BookDto bookDto = createBookValid();
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

        mockMvc.perform(post(PATH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(bookDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("livro ja cadastrado anteriormente, devor retornar mensagem de erro 'Livro ja cadastrado'")
    void mustReturnMessageBookAlrightCreate() throws Exception {
        BookDto bookDto = createBookValid();
        Book book = dtoToEntity(bookDto);

        BDDMockito.given(service.getBookById(bookDto.getId())).willReturn(Optional.of(book));
        BDDMockito.given(service.saveBook(bookDto)).willReturn(book);

        Exception exception = assertThrows(
                NestedServletException.class,
                () -> mockMvc.perform(post(PATH_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectToJson(bookDto)))
                        .andDo(print())
                        .andExpect(status().isBadRequest()));

        assertTrue(exception.getMessage().contains("Livro ja cadastrado"));
    }

    @Test
    @DisplayName("devo retornar lista de livros")
    void mustReturnListBooks() throws Exception {
        List<BookDto> bookDtos = new ArrayList<>();
        bookDtos.add(createBookValid());
        bookDtos.add(createBookValid2());
        List<Book> bookList = dtoListToEntitys(bookDtos);

        BDDMockito.given(service.getAllBooks()).willReturn(bookList);
        BDDMockito.given(bookMapper.entityListToDtos(bookList)).willReturn(bookDtos);

        mockMvc.perform(get(PATH_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("[0]id").value("22"))
                .andExpect(jsonPath("[0]name").value("Clean Code"))
                .andExpect(jsonPath("[0]year").value(2000))
                .andExpect(jsonPath("[0]status").value("NEW"))

                .andExpect(jsonPath("[1]id").value("23"))
                .andExpect(jsonPath("[1]name").value("Clean Architeture"))
                .andExpect(jsonPath("[1]year").value(2009))
                .andExpect(jsonPath("[1]status").value("NEW"));
    }

    @Test
    @DisplayName("devo retornar lista de livros - noContent")
    void mustReturnNoContent() throws Exception {
        BDDMockito.given(service.getAllBooks()).willReturn(new ArrayList<>());
        BDDMockito.given(bookMapper.entityListToDtos(new ArrayList<>())).willReturn(new ArrayList<>());

        mockMvc.perform(get(PATH_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("devo retonar o livro solicitado pelo getById")
    void mustReturnBoookGetById() throws Exception {
        BookDto bookDto = createBookValid();
        Book book = dtoToEntity(bookDto);

        BDDMockito.given(service.getBookById(bookDto.getId())).willReturn(Optional.of(book));
        BDDMockito.given(bookMapper.entityToDto(book)).willReturn(bookDto);

        mockMvc.perform(get(PATH_URL + "/" + bookDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(bookDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("id").value("22"))
                .andExpect(jsonPath("name").value("Clean Code"))
                .andExpect(jsonPath("year").value(2000))
                .andExpect(jsonPath("status").value("NEW"));
    }

    @Test
    @DisplayName("devo deletar o livro")
    void mustDeleteBookById() throws Exception {
        BookDto bookDto = createBookValid();
        Book book = dtoToEntity(bookDto);

        BDDMockito.given(service.getBookById(bookDto.getId())).willReturn(Optional.of(book));
        BDDMockito.given(bookMapper.entityToDto(book)).willReturn(bookDto);

        mockMvc.perform(delete(PATH_URL + "/" + bookDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(bookDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("devo retornar o erro 'isNotFound' ao tentar deletar livro")
    void mustRetunrDontFoundInDeleteBook() throws Exception {
        BookDto bookDto = createBookValid();

        BDDMockito.given(service.getBookById(bookDto.getId())).willReturn(Optional.empty());
        BDDMockito.given(bookMapper.entityToDto(null)).willReturn(null);

        mockMvc.perform(delete(PATH_URL + "/" + bookDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(bookDto)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("devo atualizar o livro")
    void mustUpdateBook() throws Exception {
        BookDto bookDto = createBookValid();
        Book book = dtoToEntity(bookDto);

        BDDMockito.given(service.getBookById(bookDto.getId())).willReturn(Optional.of(book));
        BDDMockito.given(bookMapper.entityToDto(book)).willReturn(bookDto);
        BDDMockito.given(service.update(bookMapper.entityToDto(book))).willReturn(book);

        mockMvc.perform(put(PATH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(bookDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("id").value("22"))
                .andExpect(jsonPath("name").value("Clean Code"))
                .andExpect(jsonPath("year").value(2000))
                .andExpect(jsonPath("status").value("NEW"));
    }

    private BookDto createBookValid() {
        return BookDto.builder()
                .id(22L)
                .name("Clean Code")
                .year(2000)
                .status("NEW")
                .build();
    }

    private BookDto createBookValid2() {
        return BookDto.builder()
                .id(23L)
                .name("Clean Architeture")
                .year(2009)
                .status("NEW")
                .build();
    }

    private Book dtoToEntity(BookDto bookDto) {
        Book book = new Book();
        BeanUtils.copyProperties(bookDto, book);
        return book;
    }

    public List<Book> dtoListToEntitys(List<BookDto> bookDtoList) {
        return bookDtoList
                .stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private static String objectToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
