package shop.mtcoding.blog.web.exam.student;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog.core.utils.ApiUtil;
import shop.mtcoding.blog.domain.course.exam.ExamService;
import shop.mtcoding.blog.domain.user.User;
import shop.mtcoding.blog.web.exam.ExamRequest;
import shop.mtcoding.blog.web.exam.ExamResponse;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class StudentExamController {

    private final HttpSession session;
    private final ExamService examService;

    @GetMapping("/api/student/paper")
    public String studentPaperList(Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        ExamResponse.MyPaperListDTO respDTO = examService.학생_응시가능한시험지목록(sessionUser);
        model.addAttribute("model", respDTO);
        return "v2/student/paper/list";
    }

    @GetMapping("/api/student/paper/{paperId}/start")
    public String studentExamStartInfo(@PathVariable("paperId") Long paperId, Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        ExamResponse.StartDTO respDTO = examService.학생_시험시작정보(sessionUser, paperId);
        model.addAttribute("model", respDTO);
        return "v2/student/paper/start";
    }

    @PostMapping("/api/student/exam")
    public ResponseEntity<?> studentExamSave(@RequestBody ExamRequest.SaveDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        examService.학생_시험응시(reqDTO, sessionUser);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }

    @GetMapping("/api/student/exam")
    public String studentExamResultList(Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        List<ExamResponse.ResultDTO> respDTO = examService.학생_시험결과목록(sessionUser);
        model.addAttribute("models", respDTO);
        return "v2/student/exam-result/list";
    }

    @GetMapping("/api/student/exam/{examId}")
    public String studentExamResultDetail(@PathVariable(value = "examId") Long examId, Model model) {
        ExamResponse.ResultDetailDTO respDTO = examService.학생_시험결과상세(examId);

        model.addAttribute("model", respDTO);
        return "v2/student/exam-result/detail";
    }


    @PutMapping("/api/student/exam/sign")
    public ResponseEntity<?> sign(@RequestBody ExamRequest.StudentSignDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        examService.학생_사인저장(reqDTO);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }


}
