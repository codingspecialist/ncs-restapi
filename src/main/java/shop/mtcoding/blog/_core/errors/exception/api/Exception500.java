package shop.mtcoding.blog._core.errors.exception.api;

import org.springframework.http.HttpStatus;
import shop.mtcoding.blog._core.utils.Resp;

public class Exception500 extends RuntimeException {

    public Exception500(String msg) {
        super(msg);
    }

    public Resp body() {
        return Resp.fail(500, getMessage());
    }

    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}