package shop.mtcoding.blog.core.errors.exception.api;

import org.springframework.http.HttpStatus;
import shop.mtcoding.blog.core.utils.Resp;

public class Exception404 extends RuntimeException {

    public Exception404(String msg) {
        super(msg);
    }

    public Resp body() {
        return Resp.fail(404, getMessage());
    }

    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}