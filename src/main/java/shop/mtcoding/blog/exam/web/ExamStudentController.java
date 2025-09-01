package shop.mtcoding.blog.exam.web;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.utils.Resp;
import shop.mtcoding.blog.exam.application.domain.enums.EvaluationWay;
import shop.mtcoding.blog.exam.application.service.ExamService;
import shop.mtcoding.blog.exam.web.dto.ExamStudentRequest;
import shop.mtcoding.blog.exam.web.dto.ExamStudentResponse;
import shop.mtcoding.blog.user.application.domain.User;

@RequestMapping("/api/student/exams")
@RequiredArgsConstructor
@Controller
public class ExamStudentController {

    private final HttpSession session;
    private final ExamService examService;

    @GetMapping("/papers")
    public ResponseEntity<?> studentPaperList() {
        User sessionUser = (User) session.getAttribute("sessionUser");

        var modelData = examService.학생응시가능한시험지목록(sessionUser);
        var respDTO = new ExamStudentResponse.MyPaperItems(modelData.studentId(), modelData.papers());
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    @GetMapping("/papers/{paperId}/start")
    public ResponseEntity<?> studentExamStartInfo(@PathVariable("paperId") Long paperId) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var modelData = examService.학생시험시작정보(sessionUser, paperId);

        if (modelData.paperPS().getEvaluationWay() == EvaluationWay.MCQ) {
            var respDTO = new ExamStudentResponse.McqStartDTO(modelData.paperPS(), modelData.studentName(), modelData.subjectElementListPS(), modelData.questionListPS());
            return ResponseEntity.ok(Resp.ok(respDTO));
        } else {
            var respDTO = new ExamStudentResponse.RubricStartDTO(modelData.paperPS(), modelData.studentName(), modelData.subjectElementListPS(), modelData.questionListPS());
            return ResponseEntity.ok(Resp.ok(respDTO));
        }
    }

    @PostMapping("/mcq")
    public ResponseEntity<?> studentExamMcqSave(@RequestBody ExamStudentRequest.McqSave reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        examService.학생객관식시험응시(reqDTO, sessionUser);
        return ResponseEntity.ok(Resp.ok(null));
    }

    @PostMapping("/rubric")
    public ResponseEntity<?> studentExamRubricSave(@RequestBody ExamStudentRequest.RubricSave reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        examService.학생루브릭시험응시(reqDTO, sessionUser);
        return ResponseEntity.ok(Resp.ok(null));
    }

    @GetMapping
    public ResponseEntity<?> studentExamResultList() {
        User sessionUser = (User) session.getAttribute("sessionUser");

        var modelData = examService.학생시험결과목록(sessionUser);
        var respDTOs = modelData.exams().stream().map(ExamStudentResponse.ResultDTO::new).toList();
        return ResponseEntity.ok(Resp.ok(respDTOs));
    }

    @GetMapping("/{examId}")
    public ResponseEntity<?> studentExamResultDetail(@PathVariable(value = "examId") Long examId) {
        var modelData = examService.시험상세결과(examId);

        if (modelData.exam().getPaper().getEvaluationWay() == EvaluationWay.MCQ) {
            var respDTO = new ExamStudentResponse.McqResultDetailDTO(modelData.exam(), modelData.subjectElements(), modelData.teacher());
            return ResponseEntity.ok(Resp.ok(respDTO));
        } else {
            var respDTO = new ExamStudentResponse.RubricResultDetailDTO(modelData.exam(), modelData.subjectElements(), modelData.teacher());
            return ResponseEntity.ok(Resp.ok(respDTO));
        }
    }


    @PutMapping("/sign")
    public ResponseEntity<?> sign(@RequestBody ExamStudentRequest.SignDTO reqDTO) {
        examService.학생사인저장(reqDTO);
        return ResponseEntity.ok(Resp.ok(null));
    }


}
