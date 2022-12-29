package ua.sirkostya009.posterapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class OccupiedException extends RuntimeException {
    public OccupiedException(String message) {
        super(message);
    }
}
