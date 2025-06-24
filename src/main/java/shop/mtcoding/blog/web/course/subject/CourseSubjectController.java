package shop.mtcoding.blog.web.course.subject;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import shop.mtcoding.blog.core.utils.Resp;
import shop.mtcoding.blog.domain.course.subject.SubjectService;

@RequestMapping("/api/courses/{courseId}/subjects")
@RequiredArgsConstructor
@Controller
public class CourseSubjectController {

    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<?> save(@PathVariable("courseId") Long courseId, @RequestBody CourseSubjectRequest.SaveDTO reqDTO) {
        subjectService.교과목등록(courseId, reqDTO);
        return ResponseEntity.ok(Resp.ok(null));
    }

}
