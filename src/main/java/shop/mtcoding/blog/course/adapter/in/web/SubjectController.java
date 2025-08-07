package shop.mtcoding.blog.course.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.utils.Resp;
import shop.mtcoding.blog.course.adapter.in.web.dto.SubjectRequest;
import shop.mtcoding.blog.course.application.port.in.SubjectUseCase;


@RequestMapping("/api/courses/{courseId}/subjects")
@RequiredArgsConstructor
@RestController
public class SubjectController {
    private final SubjectUseCase subjectUseCase;

    @PostMapping
    public ResponseEntity<?> save(@PathVariable("courseId") Long courseId, @RequestBody SubjectRequest.Save reqDTO) {
        subjectUseCase.교과목등록(courseId, reqDTO.toCommand());
        return ResponseEntity.ok(Resp.ok(null));
    }
}
