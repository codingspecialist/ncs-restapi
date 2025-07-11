package shop.mtcoding.blog.web.student.exam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog.core.utils.Resp;
import shop.mtcoding.blog.domain.course.exam.ExamService;
import shop.mtcoding.blog.domain.course.subject.paper.EvaluationWay;
import shop.mtcoding.blog.domain.user.User;

@RequiredArgsConstructor
@Controller
public class StudentExamController {

    private final HttpSession session;
    private final ExamService examService;

    @GetMapping("/api/student/paper")
    public String studentPaperList(Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        var modelData = examService.학생응시가능한시험지목록(sessionUser);
        var respDTO = new StudentExamResponse.MyPaperItems(modelData.studentId(), modelData.papers());
        model.addAttribute("model", respDTO);
        return "student/paper/list";
    }

    @GetMapping("/api/student/paper/{paperId}/start")
    public String studentExamStartInfo(@PathVariable("paperId") Long paperId, Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var modelData = examService.학생시험시작정보(sessionUser, paperId);

        if (modelData.paperPS().getEvaluationWay() == EvaluationWay.MCQ) {
            var respDTO = new StudentExamResponse.McqStartDTO(modelData.paperPS(), modelData.studentName(), modelData.subjectElementListPS(), modelData.questionListPS());
            model.addAttribute("model", respDTO);
            return "student/paper/mcq-start";
        } else {
            var respDTO = new StudentExamResponse.RubricStartDTO(modelData.paperPS(), modelData.studentName(), modelData.subjectElementListPS(), modelData.questionListPS());
            model.addAttribute("model", respDTO);
            return "student/paper/rubric-start";
        }


    }

    @PostMapping("/api/student/exam/mcq")
    public ResponseEntity<?> studentExamMcqSave(@RequestBody StudentExamRequest.McqSave reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        examService.학생객관식시험응시(reqDTO, sessionUser);
        return ResponseEntity.ok(Resp.ok(null));
    }

    @PostMapping("/api/student/exam/rubric")
    public ResponseEntity<?> studentExamRubricSave(@RequestBody StudentExamRequest.RubricSave reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        examService.학생루브릭시험응시(reqDTO, sessionUser);
        return ResponseEntity.ok(Resp.ok(null));
    }

    @GetMapping("/api/student/exam")
    public String studentExamResultList(Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        var modelData = examService.학생시험결과목록(sessionUser);
        var respDTOs = modelData.exams().stream().map(StudentExamResponse.ResultDTO::new).toList();
        model.addAttribute("models", respDTOs);
        return "student/exam-result/list";
    }

    @GetMapping("/api/student/exam/{examId}")
    public String studentExamResultDetail(@PathVariable(value = "examId") Long examId, Model model) {
        var modelData = examService.시험상세결과(examId);

        if (modelData.exam().getPaper().getEvaluationWay() == EvaluationWay.MCQ) {
            var respDTO = new StudentExamResponse.McqResultDetailDTO(modelData.exam(), modelData.subjectElements(), modelData.teacher());

            model.addAttribute("model", respDTO);
            return "student/exam-result/mcq-detail";
        } else {
            var respDTO = new StudentExamResponse.RubricResultDetailDTO(modelData.exam(), modelData.subjectElements(), modelData.teacher());

            model.addAttribute("model", respDTO);
            return "student/exam-result/rubric-detail";
        }
    }


    @PutMapping("/api/student/exam/sign")
    public ResponseEntity<?> sign(@RequestBody StudentExamRequest.SignDTO reqDTO) {
        examService.학생사인저장(reqDTO);
        return ResponseEntity.ok(Resp.ok(null));
    }


}
