package br.com.diego.libraryapi;

import br.com.diego.libraryapi.models.Book;
import br.com.diego.libraryapi.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(BookRepository bookRepository) {
        return args -> {
            bookRepository.save(Book.builder().id(1L).name("Codigo Limpo").year(2007).status("NEW").build());
            bookRepository.save(Book.builder().id(2L).name("Lessons Learning").year(2000).status("USED").build());
            bookRepository.save(Book.builder().id(3L).name("Jornada Java").year(2020).status("NEW").build());
            bookRepository.save(Book.builder().id(4L).name("Developing Testing").year(2017).status("NEW").build());
        };
    }
}