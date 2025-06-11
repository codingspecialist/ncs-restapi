package shop.mtcoding.blog.web.student;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog.core.utils.ApiUtil;
import shop.mtcoding.blog.domain.course.exam.ExamService;
import shop.mtcoding.blog.domain.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class StudentExamController {

    private final HttpSession session;
    private final ExamService examService;

    @GetMapping("/api/student/paper")
    public String studentPaperList(Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        StudentExamResponse.MyPaperListDTO respDTO = examService.학생_응시가능한시험지목록(sessionUser);
        model.addAttribute("model", respDTO);
        return "student/paper/list";
    }

    @GetMapping("/api/student/paper/{paperId}/start")
    public String studentExamStartInfo(@PathVariable("paperId") Long paperId, Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        StudentExamResponse.StartDTO respDTO = examService.학생_시험시작정보(sessionUser, paperId);
        model.addAttribute("model", respDTO);
        return "student/paper/start";
    }

    @PostMapping("/api/student/exam")
    public ResponseEntity<?> studentExamSave(@RequestBody StudentExamRequest.SaveDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        examService.학생_시험응시(reqDTO, sessionUser);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }

    @GetMapping("/api/student/exam")
    public String studentExamResultList(Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        List<StudentExamResponse.ResultDTO> respDTO = examService.학생_시험결과목록(sessionUser);
        model.addAttribute("models", respDTO);
        return "student/exam-result/list";
    }

    @GetMapping("/api/student/exam/{examId}")
    public String studentExamResultDetail(@PathVariable(value = "examId") Long examId, Model model) {
        StudentExamResponse.ResultDetailDTO respDTO = examService.학생_시험결과상세(examId);

        model.addAttribute("model", respDTO);
        return "student/exam-result/detail";
    }


    @PutMapping("/api/student/exam/sign")
    public ResponseEntity<?> sign(@RequestBody StudentExamRequest.SignDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        examService.학생_사인저장(reqDTO);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }


}
