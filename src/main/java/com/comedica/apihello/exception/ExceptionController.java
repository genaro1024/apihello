package com.comedica.apihello.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.comedica.apihello.common.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import java.io.FileNotFoundException;


@ControllerAdvice
@Slf4j
public class ExceptionController extends ResponseEntityExceptionHandler {

    public ExceptionController() {
        super();
    }

    // API

    // 400

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        GenericResponse<String> genericResponse = new GenericResponse<>();
        genericResponse.setCode(1);
        genericResponse.setMsg("Error 400 Invalid request body");
        genericResponse.setContent("{}");
        genericResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        log.error("Error 400: {}", genericResponse, ex);
        return handleExceptionInternal(ex, genericResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        GenericResponse<String> genericResponse = new GenericResponse<>();
        genericResponse.setCode(1);
        genericResponse.setMsg("Error 400 Validation failed for arguments");
        genericResponse.setContent("{}");
        genericResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        log.error("Error 400: {}", genericResponse, ex);
        return handleExceptionInternal(ex, genericResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    // 404
    @ExceptionHandler({ ResourceNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(final Exception ex, final WebRequest request) {
        GenericResponse<String> genericResponse = new GenericResponse<>();
        genericResponse.setCode(1);
        genericResponse.setMsg("Error 404 Resource Not Found");
        genericResponse.setContent("{}");
        genericResponse.setStatus(HttpStatus.NOT_FOUND.value());
        log.error("Error 404: {}", genericResponse, ex);
        return handleExceptionInternal(ex, genericResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // 500
    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class, FileNotFoundException.class, RuntimeException.class })
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        GenericResponse<String> genericResponse = new GenericResponse<>();
        genericResponse.setCode(1);
        genericResponse.setMsg("Error 500 An internal error occurred");
        genericResponse.setContent("{}");
        genericResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.error("Error 500: {}", genericResponse, ex);
        return handleExceptionInternal(ex, genericResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    // Catch-all handler for any other exceptions
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAnyException(final Exception ex, final WebRequest request) {
        GenericResponse<String> genericResponse = new GenericResponse<>();
        genericResponse.setCode(1);
        genericResponse.setMsg("Error 500 An unexpected error occurred");
        genericResponse.setContent("{}");
        genericResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());       
        log.error("Unhandled error: {}", genericResponse, ex);
        return handleExceptionInternal(ex, genericResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
