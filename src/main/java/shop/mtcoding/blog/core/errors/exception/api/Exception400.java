package shop.mtcoding.blog.core.errors.exception.api;

import org.springframework.http.HttpStatus;
import shop.mtcoding.blog.core.utils.ApiUtil;

public class Exception400 extends RuntimeException {

    public Exception400(String msg) {
        super(msg);
    }

    public ApiUtil body() {
        return new ApiUtil(400, getMessage());
    }

    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }
}
