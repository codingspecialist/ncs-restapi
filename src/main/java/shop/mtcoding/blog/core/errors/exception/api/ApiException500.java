package shop.mtcoding.blog.core.errors.exception.api;

import org.springframework.http.HttpStatus;
import shop.mtcoding.blog.core.utils.ApiUtil;

public class ApiException500 extends RuntimeException {

    public ApiException500(String msg) {
        super(msg);
    }

    public ApiUtil body() {
        return new ApiUtil(500, getMessage());
    }

    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}