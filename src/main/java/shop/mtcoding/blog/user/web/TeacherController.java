package shop.mtcoding.blog.user.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.mtcoding.blog._core.utils.Resp;

@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@RestController
public class TeacherController {

    @GetMapping
    public ResponseEntity<?> items() {

        return ResponseEntity.ok(Resp.ok(null));
    }
}
