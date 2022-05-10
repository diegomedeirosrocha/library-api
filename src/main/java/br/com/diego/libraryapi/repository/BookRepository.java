package br.com.diego.libraryapi.repository;

import br.com.diego.libraryapi.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
