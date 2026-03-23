package com.hotel.hotel_backend.Exception;


import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

     @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound (
            ResourceNotFoundException ex,
            WebRequest request
    ){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                404,
                "Not Found",
                ex.getMessage(),
                request.getDescription(false)
        );
          return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HabitacionNoDisponibleException.class)
    public ResponseEntity<ErrorResponse> handleHabitacionNoDisponible(
            HabitacionNoDisponibleException ex,
            WebRequest request
    ){
         ErrorResponse error = new ErrorResponse(
                 LocalDateTime.now(),
                 409,
                 "Conflict",
                 ex.getMessage(),
                 request.getDescription(false)
         );
         return new ResponseEntity<>(error, HttpStatus.CONFLICT);

    }



    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExist (
            EmailAlreadyExistException ex,
            WebRequest request
    ){
         ErrorResponse error = new ErrorResponse(
                 LocalDateTime.now(),
                 409,
                 "Conflict",
                 ex.getMessage(),
                 request.getDescription(false)
         );
         return  new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PagoException.class)
    public ResponseEntity<ErrorResponse> hanldePagoException(
            PagoException ex,
            WebRequest request
    ){
         ErrorResponse error = new ErrorResponse(
                 LocalDateTime.now(),
                 402,
                 "Payment Required",
                 ex.getMessage(),
                 request.getDescription(false)
         );
         return new ResponseEntity<>(error, HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request
    )
    {
        List<String> errores = new ArrayList<>();
        for(FieldError error : ex.getBindingResult().getFieldErrors()){
            errores.add(error.getField() + ": " + error.getDefaultMessage());
        }
        String mensaje = String.join(", ", errores);
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                400,
                "Bad Request",
                mensaje,
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            WebRequest request
    ){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                500,
                "Internal Server Error",
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

