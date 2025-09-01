package shop.mtcoding.blog.course.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.anno.SessionUser;
import shop.mtcoding.blog._core.utils.Resp;
import shop.mtcoding.blog.course.application.service.CourseService;
import shop.mtcoding.blog.course.web.dto.CourseRequest;
import shop.mtcoding.blog.course.web.dto.CourseResponse;
import shop.mtcoding.blog.user.application.domain.User;

@RequestMapping("/api/courses")
@RequiredArgsConstructor
@RestController
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<?> getCourses(
            @SessionUser User sessionUser,
            @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = "id", page = 0) Pageable pageable) {

        var respDTO = courseService.과정목록(sessionUser.getTeacher().getId(), pageable);
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody CourseRequest.Save reqDTO) {
        var respDTO = courseService.과정등록(reqDTO);
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    // 과정정보만 포함
    @GetMapping("/{courseId}")
    public ResponseEntity<?> info(@PathVariable(value = "courseId") Long courseId) {
        var respDTO = courseService.과정정보(courseId);
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    // 교과목들, 학생들 포함
    @GetMapping("/{courseId}/detail")
    public ResponseEntity<?> detail(@PathVariable(value = "courseId") Long courseId) {
        // TODO: 평가일, 재평가일 추후 고려하기
        var respDTO = courseService.과정상세(courseId);
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

}
