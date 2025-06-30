package shop.mtcoding.blog.core.errors;

import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.mtcoding.blog.core.errors.exception.api.*;
import shop.mtcoding.blog.core.utils.Resp;

// RuntimeException이 터지면 해당 파일로 오류가 모인다
@Slf4j
@RestControllerAdvice // 데이터 응답
public class MyExceptionHandler {


    @ExceptionHandler(Exception400.class)
    public ResponseEntity<?> badRequest(Exception400 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception401.class)
    public ResponseEntity<?> unAuthorized(Exception401 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception403.class)
    public ResponseEntity<?> forbidden(Exception403 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception404.class)
    public ResponseEntity<?> notFound(Exception404 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception500.class)
    public ResponseEntity<?> serverError(Exception500 e) {
        log.error(e.getMessage());
        Sentry.captureException(e);
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unknown(Exception e) {
        log.error(e.getMessage());
        Sentry.captureException(e);
        e.printStackTrace();
        return new ResponseEntity<>(Resp.fail(500, "오류 : 관리자에게 문의하세요"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
