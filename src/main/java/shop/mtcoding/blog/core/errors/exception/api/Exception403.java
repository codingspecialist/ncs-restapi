package shop.mtcoding.blog.core.errors.exception.api;

import org.springframework.http.HttpStatus;
import shop.mtcoding.blog.core.utils.Resp;

public class Exception403 extends RuntimeException {

    public Exception403(String msg) {
        super(msg);
    }

    public Resp body() {
        return Resp.fail(403, getMessage());
    }

    public HttpStatus status() {
        return HttpStatus.FORBIDDEN;
    }
}