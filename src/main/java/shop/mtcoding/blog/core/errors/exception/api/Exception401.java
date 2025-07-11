package shop.mtcoding.blog.core.errors.exception.api;

import org.springframework.http.HttpStatus;
import shop.mtcoding.blog.core.utils.Resp;

public class Exception401 extends RuntimeException {

    public Exception401(String msg) {
        super(msg);
    }

    public Resp body() {
        return Resp.fail(401, getMessage());
    }

    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}