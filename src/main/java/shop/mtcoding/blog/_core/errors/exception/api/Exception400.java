package shop.mtcoding.blog._core.errors.exception.api;

import org.springframework.http.HttpStatus;
import shop.mtcoding.blog._core.utils.Resp;

public class Exception400 extends RuntimeException {

    public Exception400(String msg) {
        super(msg);
    }

    public Resp body() {
        return Resp.fail(400, getMessage());
    }

    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }
}
