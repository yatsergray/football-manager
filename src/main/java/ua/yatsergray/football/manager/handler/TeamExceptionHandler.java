package ua.yatsergray.football.manager.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.yatsergray.football.manager.exception.NoSuchTeamException;
import ua.yatsergray.football.manager.exception.TeamAlreadyExistsException;

@ControllerAdvice
public class TeamExceptionHandler {

    @ExceptionHandler(NoSuchTeamException.class)
    public ResponseEntity<String> handleNoSuchTeamException(NoSuchTeamException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(TeamAlreadyExistsException.class)
    public ResponseEntity<String> handleTeamAlreadyExistsException(TeamAlreadyExistsException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
