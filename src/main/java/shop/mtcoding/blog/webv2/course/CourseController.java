package shop.mtcoding.blog.webv2.course;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/courses")
@RequiredArgsConstructor
@RestController
public class CourseController {

//    private final CourseService courseService;
//
//    @GetMapping
//    public ResponseEntity<?> items(@SessionUser User sessionUser, @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = "id", page = 0) Pageable pageable) {
//        var modelData = courseService.과정목록(sessionUser.getTeacher().getId(), pageable);
//        var respDTO = new CourseResponse.Items(modelData.coursePG());
//        return ResponseEntity.ok(Resp.ok(respDTO));
//    }
//
//    @PostMapping
//    public ResponseEntity<?> save(@RequestBody CourseRequest.SaveDTO reqDTO) {
//        courseService.과정등록(reqDTO);
//        return ResponseEntity.ok(Resp.ok(null));
//    }
//
//    @GetMapping("/{courseId}")
//    public ResponseEntity<?> detail(@PathVariable(value = "courseId") Long courseId) {
//        var modelData = courseService.과정상세(courseId);
//        var respDTO = new CourseResponse.Item(modelData.course());
//        return ResponseEntity.ok(Resp.ok(respDTO));
//    }
//
//    @GetMapping("/{courseId}/detail")
//    public ResponseEntity<?> detailWithSubjectsAndStudents(@PathVariable(value = "courseId") Long courseId) {
//        var modelData = courseService.과정상세_교과목들_학생들(courseId);
//        var respDTO = new CourseResponse.Detail(modelData.course(), modelData.subjects(), modelData.students());
//        return ResponseEntity.ok(Resp.ok(respDTO));
//    }

}
