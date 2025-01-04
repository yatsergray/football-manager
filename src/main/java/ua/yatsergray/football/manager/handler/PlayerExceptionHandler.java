package ua.yatsergray.football.manager.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.yatsergray.football.manager.exception.NoSuchPlayerException;
import ua.yatsergray.football.manager.exception.PlayerConflictException;

@ControllerAdvice
public class PlayerExceptionHandler {

    @ExceptionHandler(NoSuchPlayerException.class)
    public ResponseEntity<String> handleNoSuchPlayerException(NoSuchPlayerException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(PlayerConflictException.class)
    public ResponseEntity<String> handlePlayerConflictException(PlayerConflictException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
