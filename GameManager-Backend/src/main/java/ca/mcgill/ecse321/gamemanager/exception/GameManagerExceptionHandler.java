package ca.mcgill.ecse321.gamemanager.exception;

import ca.mcgill.ecse321.gamemanager.dto.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GameManagerExceptionHandler {
    @ExceptionHandler(GameManagerException.class)
    public ResponseEntity<ErrorDto> handleEventRegistrationException(GameManagerException e) {
        return new ResponseEntity<ErrorDto>(new ErrorDto(e.getMessage()), e.getStatus());
    }
}
