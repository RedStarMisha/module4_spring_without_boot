package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

//    @org.springframework.web.bind.annotation.ExceptionHandler
//    public ResponseEntity<String> error(Throwable e) {
//        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//    }

//    @org.springframework.web.bind.annotation.ExceptionHandler({PSQLException.class})
//    public ResponseEntity<String> sqlError(PSQLException e) {
//        log.warn(e.getMessage());
//        return new ResponseEntity<>(new String(e.getMessage().getBytes(Charset.forName("Cp1251"))), HttpStatus.BAD_REQUEST);
//    }
}
