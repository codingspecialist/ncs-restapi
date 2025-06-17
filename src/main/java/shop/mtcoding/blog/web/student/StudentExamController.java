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

@RequiredArgsConstructor
@Controller
public class StudentExamController {

    private final HttpSession session;
    private final ExamService examService;

    @GetMapping("/api/student/paper")
    public String studentPaperList(Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        var modelData = examService.학생_응시가능한시험지목록(sessionUser);
        var respDTO = new StudentExamResponse.MyPaperListDTO(modelData.studentId(), modelData.papers(), modelData.attendanceMap());
        model.addAttribute("model", respDTO);
        return "student/paper/list";
    }

    @GetMapping("/api/student/paper/{paperId}/start")
    public String studentExamStartInfo(@PathVariable("paperId") Long paperId, Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var modelData = examService.학생_시험시작정보(sessionUser, paperId);
        var respDTO = new StudentExamResponse.McqStartDTO(modelData.paperPS(), modelData.studentName(), modelData.subjectElementListPS(), modelData.questionListPS());
        model.addAttribute("model", respDTO);
        return "student/paper/mcq-start";
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

        var modelData = examService.학생_시험결과목록(sessionUser);
        var respDTOs = modelData.exams().stream().map(StudentExamResponse.ResultDTO::new).toList();
        model.addAttribute("models", respDTOs);
        return "student/exam-result/list";
    }

    @GetMapping("/api/student/exam/{examId}")
    public String studentExamResultDetail(@PathVariable(value = "examId") Long examId, Model model) {
        var modelData = examService.학생_시험결과상세(examId);
        var respDTO = new StudentExamResponse.ResultDetailDTO(modelData.exam(), modelData.subjectElements(), modelData.teacher());

        model.addAttribute("model", respDTO);
        return "student/exam-result/detail";
    }


    @PutMapping("/api/student/exam/sign")
    public ResponseEntity<?> sign(@RequestBody StudentExamRequest.SignDTO reqDTO) {
        examService.학생_사인저장(reqDTO);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }


}
