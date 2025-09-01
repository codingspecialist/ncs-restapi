package shop.mtcoding.blog.exam.web;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.utils.Resp;
import shop.mtcoding.blog.course.application.service.CourseService;
import shop.mtcoding.blog.course.application.service.SubjectService;
import shop.mtcoding.blog.exam.application.domain.enums.EvaluationWay;
import shop.mtcoding.blog.exam.application.service.ExamService;
import shop.mtcoding.blog.exam.web.dto.ExamTeacherRequest;
import shop.mtcoding.blog.exam.web.dto.ExamTeacherResponse;

@RequestMapping("/api/teachers/exams")
@RequiredArgsConstructor
@RestController
public class ExamTeacherController {

    private final CourseService courseService;
    private final SubjectService subjectService;
    private final ExamService examService;
    private final HttpSession session;

    @GetMapping("/{examId}")
    public ResponseEntity<?> teacherResultDetail(@PathVariable(value = "examId") Long examId) {
        var modelData = examService.시험상세결과들(examId);

        if (modelData.evaluationWay() == EvaluationWay.MCQ) {
            var respDTO = new ExamTeacherResponse.ResultMcqDetails(examId, modelData.exams(),
                    modelData.subjectElements(), modelData.teacher());
            return ResponseEntity.ok(Resp.ok(respDTO));
        } else {
            var respDTO = new ExamTeacherResponse.ResultRubricDetails(examId, modelData.exams(),
                    modelData.subjectElements(), modelData.teacher());
            return ResponseEntity.ok(Resp.ok(respDTO));
        }
    }

    // 시험을 치지 않아도 Exam은 만들어져야 한다.
    @PostMapping("/not-taken-reason")
    public ResponseEntity<?> 미응시이유입력(@RequestBody ExamTeacherRequest.NotTakenReason reqDTO) {
        examService.강사미응시이유처리(reqDTO);
        return ResponseEntity.ok(Resp.ok(null));
    }

    @PutMapping("/{examId}/mcq")
    public ResponseEntity<?> 채점하기(@PathVariable("examId") Long examId,
            @RequestBody ExamTeacherRequest.GradeMcq reqDTO) {
        examService.강사객관식채점하기(examId, reqDTO);
        return ResponseEntity.ok(Resp.ok(null));
    }

    @PutMapping("/{examId}/rubric")
    public ResponseEntity<?> 채점하기(@PathVariable("examId") Long examId,
            @RequestBody ExamTeacherRequest.GradeRubric reqDTO) {
        examService.강사루브릭채점하기(examId, reqDTO);
        return ResponseEntity.ok(Resp.ok(null));
    }
}
