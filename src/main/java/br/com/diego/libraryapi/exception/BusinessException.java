package br.com.diego.libraryapi.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String s) {
        super(s);
    }

    public BusinessException() {
    }
}
