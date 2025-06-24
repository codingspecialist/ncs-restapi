package shop.mtcoding.blog.web.course;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog.core.utils.Resp;
import shop.mtcoding.blog.domain.course.CourseModel;
import shop.mtcoding.blog.domain.course.CourseService;
import shop.mtcoding.blog.domain.user.User;

@RequiredArgsConstructor
@RestController
public class CourseController {

    private final HttpSession session;
    private final CourseService courseService;

    @GetMapping("/api/course-menu/course")
    public ResponseEntity<?> items(@PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = "id", page = 0) Pageable pageable) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        CourseModel.Slice slice = courseService.과정목록(sessionUser.getTeacher().getId(), pageable);
        CourseResponse.ListDTO respDTO = new CourseResponse.ListDTO(slice.coursePG());

        return ResponseEntity.ok(Resp.ok(respDTO));
    }


    @PostMapping("/api/course-menu/course/save")
    public String save(CourseRequest.SaveDTO reqDTO) {
        courseService.과정등록(reqDTO);
        return "redirect:/api/course-menu/course";
    }

    @GetMapping("/api/course-menu/course/{courseId}")
    public String detail(@PathVariable(value = "courseId") Long courseId, @RequestParam(value = "tabNum", required = false, defaultValue = "0") Integer tabNum, Model model) {
        CourseModel.Detail detail = courseService.과정상세(courseId);
        CourseResponse.DetailDTO respDTO = new CourseResponse.DetailDTO(detail.course(), detail.subjects(), detail.students());
        model.addAttribute("model", respDTO);

        // 과정 상세보기에서 무슨 학생등록 버튼 클릭하면 리다이렉션되면, 탭번호가 1, 교과목등록이면 탭번호 0
        model.addAttribute("tabNum", tabNum);
        return "course/detail";
    }

}
