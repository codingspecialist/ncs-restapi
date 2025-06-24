package shop.mtcoding.blog.core.errors.exception.api;

import org.springframework.http.HttpStatus;
import shop.mtcoding.blog.core.utils.ApiUtil;

public class Exception403 extends RuntimeException {

    public Exception403(String msg) {
        super(msg);
    }

    public ApiUtil body() {
        return new ApiUtil(403, getMessage());
    }

    public HttpStatus status() {
        return HttpStatus.FORBIDDEN;
    }
}