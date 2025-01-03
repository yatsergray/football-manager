package ua.yatsergray.football.manager.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.yatsergray.football.manager.exception.NoSuchPlayerException;

@ControllerAdvice
public class PlayerExceptionHandler {

    @ExceptionHandler(NoSuchPlayerException.class)
    public ResponseEntity<String> handleNoSuchPlayerException(NoSuchPlayerException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
