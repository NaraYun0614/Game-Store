package ca.mcgill.ecse321.gamemanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

public class GameManagerException extends RuntimeException {
    @NonNull
    private HttpStatus status;

    public GameManagerException(@NonNull HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    @NonNull
    public HttpStatus getStatus() {
        return status;
    }
}
