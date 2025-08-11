package shop.mtcoding.blog.course.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.utils.Resp;
import shop.mtcoding.blog.course.application.service.SubjectService;
import shop.mtcoding.blog.course.web.dto.SubjectRequest;


@RequestMapping("/api/courses/{courseId}/subjects")
@RequiredArgsConstructor
@RestController
public class SubjectController {
    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<?> save(@PathVariable("courseId") Long courseId, @RequestBody SubjectRequest.Save reqDTO) {
        subjectService.교과목등록(courseId, reqDTO.toCommand());
        return ResponseEntity.ok(Resp.ok(null));
    }
}
