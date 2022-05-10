package br.com.diego.libraryapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    @NotNull
    private Long id;

    @NotNull
    private String status;

    @NotNull
    private String name;

    @NotNull
    private int year;
}
