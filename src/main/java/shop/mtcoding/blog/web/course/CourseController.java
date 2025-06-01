package shop.mtcoding.blog.web.course;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.mtcoding.blog.domain.course.CourseContainer;
import shop.mtcoding.blog.domain.course.CourseService;
import shop.mtcoding.blog.domain.user.User;
import shop.mtcoding.blog.domain.user.teacher.Teacher;
import shop.mtcoding.blog.domain.user.teacher.TeacherService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class CourseController {

    private final HttpSession session;
    private final CourseService courseService;
    private final TeacherService teacherService;

    @GetMapping("/api/course-menu/course")
    public String list(Model model, @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = "id", page = 0) Pageable pageable) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        CourseContainer.list cn = courseService.과정목록(sessionUser.getTeacher().getId(), pageable);
        CourseResponse.ListDTO respDTO = new CourseResponse.ListDTO(cn.coursePG());
        model.addAttribute("paging", respDTO);

        return "v2/course/list";
    }

    @GetMapping("/api/course-menu/course/save-form")
    public String saveForm(Model model) {
        List<Teacher> ens = teacherService.강사목록();
        List<CourseResponse.TeacherDTO> respDTOs = ens.stream().map(CourseResponse.TeacherDTO::new).toList();
        model.addAttribute("models", respDTOs);
        return "v2/course/save-form";
    }


    @PostMapping("/api/course-menu/course/save")
    public String save(CourseRequest.SaveDTO reqDTO) {
        courseService.과정등록(reqDTO);
        return "redirect:/api/course-menu/course";
    }

    @GetMapping("/api/course-menu/course/{courseId}")
    public String detail(@PathVariable(value = "courseId") Long courseId, @RequestParam(value = "tabNum", required = false, defaultValue = "0") Integer tabNum, Model model) {
        CourseContainer.Detail cn = courseService.과정상세(courseId);
        CourseResponse.DetailDTO respDTO = new CourseResponse.DetailDTO(cn.course(), cn.subjects(), cn.students());
        model.addAttribute("model", respDTO);

        // 과정 상세보기에서 무슨 학생등록 버튼 클릭하면 리다이렉션되면, 탭번호가 1, 교과목등록이면 탭번호 0
        model.addAttribute("tabNum", tabNum);
        return "v2/course/detail";
    }

}
