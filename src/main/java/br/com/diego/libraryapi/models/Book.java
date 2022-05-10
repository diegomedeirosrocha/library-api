package br.com.diego.libraryapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Component

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "livro")
public class Book {
    @Id
    private Long id;

    private String name;

    private String status;

    private int year;

    private Date dataCreate;
}
