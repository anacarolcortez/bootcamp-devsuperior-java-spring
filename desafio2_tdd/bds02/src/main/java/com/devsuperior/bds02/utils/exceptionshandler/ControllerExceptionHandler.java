package com.devsuperior.bds02.utils.exceptionshandler;

import com.devsuperior.bds02.utils.exceptions.DatabaseException;
import com.devsuperior.bds02.utils.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException error, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(httpStatus.value());
        err.setError("Resource not found");
        err.setMessage(error.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(httpStatus).body(err);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> entityNotFound(DatabaseException error, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(httpStatus.value());
        err.setError("Database exception");
        err.setMessage(error.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(httpStatus).body(err);
    }

}

