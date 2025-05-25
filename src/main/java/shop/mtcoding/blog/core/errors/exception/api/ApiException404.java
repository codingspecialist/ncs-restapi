package shop.mtcoding.blog.core.errors.exception.api;

import org.springframework.http.HttpStatus;
import shop.mtcoding.blog.core.utils.ApiUtil;

public class ApiException404 extends RuntimeException {

    public ApiException404(String msg) {
        super(msg);
    }

    public ApiUtil body() {
        return new ApiUtil(404, getMessage());
    }

    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}