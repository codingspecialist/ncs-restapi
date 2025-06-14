package shop.mtcoding.blog.web.paper;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.mtcoding.blog.core.utils.ApiUtil;
import shop.mtcoding.blog.domain.course.CourseModel;
import shop.mtcoding.blog.domain.course.CourseService;
import shop.mtcoding.blog.domain.course.subject.SubjectModel;
import shop.mtcoding.blog.domain.course.subject.SubjectService;
import shop.mtcoding.blog.domain.course.subject.paper.PaperModel;
import shop.mtcoding.blog.domain.course.subject.paper.PaperService;
import shop.mtcoding.blog.domain.user.User;

import java.util.List;

/**
 * 1. 시험지
 */

@RequiredArgsConstructor
@Controller
public class PaperController {
    private final HttpSession session;
    private final PaperService paperService;
    private final SubjectService subjectService;
    private final CourseService courseService;

    // 1. 시험지관리 - 과정목록 (완)
    @GetMapping("/api/paper-menu/course")
    public String courseList(Model model, @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = "id", page = 0) Pageable pageable) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        CourseModel.Slice slice = courseService.과정목록(sessionUser.getTeacher().getId(), pageable);
        PaperResponse.CourseListDTO respDTO = new PaperResponse.CourseListDTO(slice.coursePG());
        model.addAttribute("model", respDTO);

        return "paper/course-list";
    }

    // 2. 시험지관리 - 교과목목록 (페이징필요)
    @GetMapping("/api/paper-menu/course/{courseId}/subject")
    public String subject(@PathVariable("courseId") Long courseId, Model model) {
        SubjectModel.Items items = subjectService.과정별교과목(courseId);
        List<PaperResponse.SubjectDTO> respDTO = items.subjects().stream().map(PaperResponse.SubjectDTO::new).toList();
        model.addAttribute("models", respDTO);
        return "paper/subject-list";
    }

    // 3. 시험지관리 - 과정목록 - 교과목목록 - 시험지목록(교과목별) (완)
    @GetMapping("/api/paper-menu/subject/{subjectId}/paper")
    public String list(Model model, @PathVariable("subjectId") Long subjectId) {
        PaperModel.Items items = paperService.교과목별시험지목록(subjectId);
        List<PaperResponse.DTO> respDTO = items.papers().stream().map(PaperResponse.DTO::new).toList();
        model.addAttribute("models", respDTO);
        return "paper/paper-list";
    }

    // 4-1. 시험지관리 - 과정목록 - 교과목목록 - 시험지목록(교과목별) - 시험지등록 폼
    @GetMapping("/api/paper-menu/subject/{subjectId}/paper/save-form")
    public String saveForm(@PathVariable("subjectId") Long subjectId, Model model) {
        model.addAttribute("subjectId", subjectId);
        return "paper/save-form";
    }

    // 4-2. 시험지관리 - 과정목록 - 교과목목록 - 시험지목록(교과목별) - 시험지상세
    @GetMapping("/api/paper-menu/paper/{paperId}")
    public String detail(@PathVariable("paperId") Long paperId, Model model) {
        PaperModel.Detail detail = paperService.시험지상세(paperId);
        PaperResponse.QuestionListDTO respDTO = new PaperResponse.QuestionListDTO(detail.paper(), detail.questions());
        model.addAttribute("model", respDTO);
        return "paper/detail";
    }

    // 5. 시험지관리 - 과정목록 - 교과목목록 - 시험지목록(교과목별) - 시험지등록(교과목별)
    @PostMapping("/api/paper-menu/subject/{subjectId}/paper/save")
    public String save(@PathVariable("subjectId") Long subjectId, PaperRequest.SaveDTO reqDTO) {
        paperService.시험지등록(subjectId, reqDTO);
        return "redirect:/api/paper-menu/subject/" + subjectId + "/paper";
    }

    // 6. 시험지관리 - 과정목록 - 교과목목록 - 시험지목록 - 시험지상세 - 문제등록 폼
    @GetMapping("/api/paper-menu/paper/{paperId}/question/save-form")
    public String questionSaveForm(@PathVariable(name = "paperId") Long paperId, Model model) {
        PaperModel.NextQuestion nextQuestion = paperService.다음문제준비(paperId);
        model.addAttribute("model", nextQuestion);
        return "paper/question/save-form";
    }

    // 7. 시험지관리 - 과정목록 - 교과목목록 - 시험지목록 - 시험지상세 - 문제등록
    @PostMapping("/api/paper-menu/paper/{paperId}/question/save")
    public ResponseEntity<?> questionSave(@RequestBody PaperRequest.QuestionSaveDTO reqDTO) {
        paperService.문제등록(reqDTO);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }


}
