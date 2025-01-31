package ca.mcgill.ecse321.gamemanager.exception.handler;

import ca.mcgill.ecse321.gamemanager.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDto> handleResponseStatusException(ResponseStatusException ex) {
        ErrorDto errorDto = new ErrorDto(String.valueOf(Collections.singletonList(ex.getReason())));
        return new ResponseEntity<>(errorDto, ex.getStatusCode());
    }
}

