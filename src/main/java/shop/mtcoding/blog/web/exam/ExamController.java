package shop.mtcoding.blog.web.exam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog.domain.course.CourseModel;
import shop.mtcoding.blog.domain.course.CourseService;
import shop.mtcoding.blog.domain.course.exam.ExamService;
import shop.mtcoding.blog.domain.course.subject.SubjectModel;
import shop.mtcoding.blog.domain.course.subject.SubjectService;
import shop.mtcoding.blog.domain.course.subject.paper.EvaluationWay;
import shop.mtcoding.blog.domain.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ExamController {

    private final CourseService courseService;
    private final SubjectService subjectService;
    private final ExamService examService;
    private final HttpSession session;

    @GetMapping("/api/exam-menu/course")
    public String list(Model model, @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = "id", page = 0) Pageable pageable) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        CourseModel.Slice slice = courseService.과정목록(sessionUser.getTeacher().getId(), pageable);
        ExamResponse.CourseListDTO respDTO = new ExamResponse.CourseListDTO(slice.coursePG());
        model.addAttribute("model", respDTO);

        return "exam/course-list";
    }

    @GetMapping("/api/exam-menu/course/{courseId}/subject")
    public String subject(@PathVariable("courseId") Long courseId, Model model) {
        SubjectModel.Items items = subjectService.과정별교과목(courseId);
        List<ExamResponse.SubjectDTO> respDTO = items.subjects().stream().map(ExamResponse.SubjectDTO::new).toList();
        model.addAttribute("models", respDTO);
        return "exam/subject-list";
    }

    @GetMapping("/api/exam-menu/course/{courseId}/subject/{subjectId}/exam")
    public String teacherResult(Model model, @PathVariable("courseId") Long courseId, @PathVariable("subjectId") Long subjectId) {
        var modelData = examService.강사교과목별시험결과(courseId, subjectId);
        model.addAttribute("models", modelData);
        return "exam/list";
    }

    // TODO: 여기서부터 해야함 (시험상세결과들, 현재 id는 알아야함)
    @GetMapping("/api/exam-menu/exam/{examId}")
    public String teacherResultDetail(@PathVariable(value = "examId") Long examId, Model model) {
        var modelData = examService.시험상세결과들(examId);

        if (modelData.evaluationWay() == EvaluationWay.MCQ) {
            var respDTO = new ExamResponse.ResultMcqDetails(examId, modelData.exams(), modelData.subjectElements(), modelData.teacher());
            model.addAttribute("model", respDTO);
            return "exam/mcq-detail";
        } else {
            var respDTO = new ExamResponse.ResultRubricDetails(examId, modelData.exams(), modelData.subjectElements(), modelData.teacher());
            model.addAttribute("model", respDTO);
            return "exam/rubric-detail";
        }
    }

    // 시험을 치지 않아도 Exam은 만들어져야 한다.
    @PostMapping("/api/exam-menu/exam/absent")
    public ResponseEntity<?> 결석입력(@RequestBody ExamRequest.AbsentDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        examService.강사_결석처리(reqDTO, sessionUser);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }

    @PutMapping("/api/exam-menu/exam/{examId}")
    public ResponseEntity<?> update(@PathVariable("examId") Long examId, @RequestBody ExamRequest.UpdateDTO reqDTO) {
        examService.강사_총평남기기(examId, reqDTO);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }
}
