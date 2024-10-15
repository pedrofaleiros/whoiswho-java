package dev.pedrofaleiros.whoiswho_api;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.pedrofaleiros.whoiswho_api.exception.ErrorResponse;
import dev.pedrofaleiros.whoiswho_api.exception.LoginException;
import dev.pedrofaleiros.whoiswho_api.exception.bad_request.CustomBadRequestException;
import dev.pedrofaleiros.whoiswho_api.exception.not_found.CustomEntityNotFoundException;
import dev.pedrofaleiros.whoiswho_api.exception.websocket.WsErrorException;
import dev.pedrofaleiros.whoiswho_api.exception.websocket.WsWarningException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @ExceptionHandler(JWTCreationException.class)
    public ResponseEntity<?> handleJWTCreationException(JWTCreationException e) {
        var response = new ErrorResponse("Erro ao criar token JWT");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<?> handleJWTVerificationException(JWTVerificationException e) {
        var response = new ErrorResponse("Token JWT invalido");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(CustomBadRequestException ex,
            WebRequest request) {
        var response = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<?> handleLoginException(LoginException ex, WebRequest request) {
        var response = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(CustomEntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(CustomEntityNotFoundException ex,
            WebRequest request) {
        var response = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthException(AuthenticationException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors()
                .forEach((error) -> errors.add(error.getDefaultMessage()));
        var response = new ErrorResponse(errors.get(0));
        return ResponseEntity.badRequest().body(response);
    }

    @MessageExceptionHandler(RuntimeException.class)
    public void handleWsWarningException(RuntimeException ex,
            SimpMessageHeaderAccessor headerAccessor) {
        var sessionId = headerAccessor.getSessionId();

        System.out.println(ex.getMessage());

        if (ex instanceof WsErrorException) {
            messagingTemplate.convertAndSendToUser(sessionId, "/queue/errors", ex.getMessage(), createHeaders(sessionId));
        } else if (ex instanceof WsWarningException) {
            messagingTemplate.convertAndSendToUser(sessionId, "/queue/warnings", ex.getMessage(), createHeaders(sessionId));
        } else if (ex instanceof CustomEntityNotFoundException) {
            messagingTemplate.convertAndSendToUser(sessionId, "/queue/warnings", ex.getMessage(), createHeaders(sessionId));
        } else {
            // TODO: temporario
            messagingTemplate.convertAndSendToUser(sessionId, "/queue/warnings", ex.getMessage(), createHeaders(sessionId));
        }
    }

    // TODO: temporario
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleGlobalException(RuntimeException ex, WebRequest request) {
        System.out.println(ex.getMessage());
        var response = new ErrorResponse(ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
