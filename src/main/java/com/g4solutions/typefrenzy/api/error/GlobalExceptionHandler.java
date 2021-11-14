package com.g4solutions.typefrenzy.api.error;

import com.g4solutions.typefrenzy.api.error.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    List<String> errors = new ArrayList<String>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }

    return handleExceptionInternal(
        ex,
        new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors),
        headers,
        HttpStatus.BAD_REQUEST,
        request
    );
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    String error = ex.getParameterName() + " parameter is missing";

    return new ResponseEntity<>(
        new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error),
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler({ConstraintViolationException.class})
  public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex,
      WebRequest request) {
    List<String> errors = new ArrayList<>();

    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      errors.add(violation.getRootBeanClass().getName() + " " +
          violation.getPropertyPath() + ": " + violation.getMessage());
    }

    return new ResponseEntity<>(
        new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors),
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request) {
    String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

    return new ResponseEntity<Object>(
        new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error),
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST
    );
  }


  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ResponseEntity<Object> resourceNotFoundException(
      ResourceNotFoundException ex, WebRequest request) {
    return new ResponseEntity<>(
        new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), ex.getMessage()),
        new HttpHeaders(),
        HttpStatus.NOT_FOUND
    );
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

    return new ResponseEntity<Object>(
        new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error),
        new HttpHeaders(),
        HttpStatus.NOT_FOUND
    );
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    StringBuilder builder = new StringBuilder();
    builder.append(ex.getMethod());
    builder.append(" method is not supported for this request. Supported methods are ");
    ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

    return new ResponseEntity<>(
        new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), builder.toString()),
        new HttpHeaders(),
        HttpStatus.METHOD_NOT_ALLOWED
    );
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> internalServerError(
      IllegalArgumentException ex, WebRequest request) {
    return new ResponseEntity<>(
        new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getMessage()),
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST
    );
  }
}
