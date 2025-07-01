package shop.mtcoding.blog._core.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@Data
public class Resp<T> {
    private Integer status;
    private String msg;
    private T body;


    public static <B> ResponseEntity<Resp<B>> ok(B body) {
        Resp<B> resp = new Resp<>(200, "성공", body);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }


    public static ResponseEntity<Resp<?>> fail(HttpStatus status, String msg) {
        Resp<?> resp = new Resp<>(status.value(), msg, null);
        return new ResponseEntity<>(resp, status);
    }

    public static Resp<?> fail(Integer status, String msg) { // 스프링 도움 안받을때!!
        Resp<?> resp = new Resp<>(status, msg, null);
        return resp;
    }
}