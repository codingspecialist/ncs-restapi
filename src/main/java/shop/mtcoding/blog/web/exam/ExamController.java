package shop.mtcoding.blog.web.exam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog.core.utils.ApiUtil;
import shop.mtcoding.blog.domain.course.CourseService;
import shop.mtcoding.blog.domain.course.exam.ExamRequest;
import shop.mtcoding.blog.domain.course.exam.ExamService;
import shop.mtcoding.blog.domain.course.subject.SubjectService;
import shop.mtcoding.blog.domain.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ExamController {
    private final HttpSession session;
    private final ExamService examService;
    private final CourseService courseService;
    private final SubjectService subjectService;

    @PutMapping("/api/student/exam/sign")
    public ResponseEntity<?> sign(@RequestBody ExamRequest.StudentSignDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        examService.학생사인저장(reqDTO, sessionUser);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }

    @GetMapping("/api/student/exam/result")
    public String studentExamResultList(Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        List<ExamResponse.ResultDTO> respDTO = examService.학생별시험결과(sessionUser);
        model.addAttribute("models", respDTO);
        return "course/exam/student-result-list";
    }

    // notpass 미이수평가보러가기
    @GetMapping("/api/teacher/exam/{examId}/result/notpass")
    public String teacherResultDetailNotPass(@PathVariable(value = "examId") Long examId, Model model) {

        ExamResponse.ResultDetailDTO respDTO = examService.미이수시험친결과상세보기(examId);
        model.addAttribute("model", respDTO);
        return "course/exam/teacher-result-detail-notpass";
    }


    @GetMapping("/api/student/exam/{examId}/result")
    public String studentExamResultDetail(@PathVariable(value = "examId") Long examId, Model model) {
        ExamResponse.ResultDetailDTO respDTO = examService.시험친결과상세보기(examId);

        model.addAttribute("model", respDTO);
        return "course/exam/student-result-detail";
    }

    @GetMapping("/api/student/exam/start")
    public String studentExamStart(@RequestParam("paperId") Long paperId, Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        ExamResponse.StartDTO respDTO = examService.시험응시(sessionUser, paperId);
        model.addAttribute("model", respDTO);
        return "course/exam/student-start";
    }

    @PostMapping("/api/student/exam/save")
    public ResponseEntity<?> studentExamSave(@RequestBody ExamRequest.SaveDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        examService.시험결과저장(reqDTO, sessionUser);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }

    @GetMapping("/api/student/exam")
    public String studentExamPaperList(Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        // TODO: 시험치는 날짜 subject에 evaluationDate 평가일 필요
        ExamResponse.MyPaperListDTO respDTO = examService.나의시험목록(sessionUser);
        model.addAttribute("model", respDTO);
        return "course/exam/student-list";
    }


}
