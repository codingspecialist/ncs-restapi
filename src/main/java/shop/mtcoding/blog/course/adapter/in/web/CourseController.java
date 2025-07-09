package shop.mtcoding.blog.course.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.anno.SessionUser;
import shop.mtcoding.blog._core.utils.Resp;
import shop.mtcoding.blog.course.adapter.in.web.dto.CourseRequest;
import shop.mtcoding.blog.course.adapter.in.web.dto.CourseResponse;
import shop.mtcoding.blog.course.application.port.in.CourseUseCase;
import shop.mtcoding.blog.user.domain.User;

@RequestMapping("/api/courses")
@RequiredArgsConstructor
@RestController
public class CourseController {

    private final CourseUseCase courseUseCase;

    @GetMapping
    public ResponseEntity<?> getCourses(
            @SessionUser User sessionUser,
            @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = "id", page = 0) Pageable pageable) {

        var output = courseUseCase.과정목록(sessionUser.getTeacher().getId(), pageable);
        var respDTO = CourseResponse.MaxPage.from(output.coursePG());
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody CourseRequest.Save reqDTO) {
        var output = courseUseCase.과정등록(reqDTO.toCommand());
        var respDTO = CourseResponse.Max.from(output.course());
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<?> detail(@PathVariable(value = "courseId") Long courseId) {
        return ResponseEntity.ok(Resp.ok(null));
    }

    @GetMapping("/{courseId}/detail")
    public ResponseEntity<?> detailWithSubjectAndStudents(@PathVariable(value = "courseId") Long courseId) {
        return ResponseEntity.ok(Resp.ok(null));
    }

}
