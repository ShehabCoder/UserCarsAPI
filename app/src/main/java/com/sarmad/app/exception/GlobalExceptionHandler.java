package com.sarmad.app.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarmad.app.exception.UserAlreadyExistsException;
import com.sarmad.app.exception.UserErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;




@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) throws JsonProcessingException {
        UserErrorMessage errorMessage = new UserErrorMessage();

        errorMessage.setStatus(HttpStatus.CONFLICT.value());
        errorMessage.setMessage(ex.getMessage());
        errorMessage.setTimestamp(System.currentTimeMillis());


        String jsonResponse = objectMapper.writeValueAsString(errorMessage);

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonResponse);
    }




    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) throws JsonProcessingException {
        UserErrorMessage errorMessage = new UserErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                System.currentTimeMillis()
        );

        String jsonResponse = objectMapper.writeValueAsString(errorMessage);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonResponse);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex) throws JsonProcessingException {
        UserErrorMessage errorMessage = new UserErrorMessage();

        errorMessage.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorMessage.setMessage(ex.getMessage());
        errorMessage.setTimestamp(System.currentTimeMillis());

        String jsonResponse = objectMapper.writeValueAsString(errorMessage);

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonResponse);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) throws JsonProcessingException {
        UserErrorMessage errorMessage = new UserErrorMessage();

        errorMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        errorMessage.setMessage(ex.getMessage());
        errorMessage.setTimestamp(System.currentTimeMillis());

        String jsonResponse = objectMapper.writeValueAsString(errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonResponse);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<UserErrorMessage> handleException(RuntimeException ex) {
        UserErrorMessage errorMessage = new UserErrorMessage();

        errorMessage.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorMessage.setMessage(ex.getMessage());
        errorMessage.setTimestamp(System.currentTimeMillis());


        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
