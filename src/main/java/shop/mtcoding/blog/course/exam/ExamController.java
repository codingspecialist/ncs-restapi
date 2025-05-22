package shop.mtcoding.blog.course.exam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.utils.ApiUtil;
import shop.mtcoding.blog.course.CourseResponse;
import shop.mtcoding.blog.course.CourseService;
import shop.mtcoding.blog.course.subject.SubjectService;
import shop.mtcoding.blog.user.User;

import java.util.List;

/**
 * 주소에 my가 붙어있으면 학생쪽, 없으면 관리자 or 강사 or 직원
 */
@RequiredArgsConstructor
@Controller
public class ExamController {
    private final HttpSession session;
    private final ExamService examService;
    private final CourseService courseService;
    private final SubjectService subjectService;

    @PostMapping("/api/teacher/exam/absent")
    public ResponseEntity<?> 결석입력(@RequestBody ExamRequest.AbsentDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        examService.결석입력(reqDTO, sessionUser);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }

    @PutMapping("/api/student/exam/sign")
    public ResponseEntity<?> sign(@RequestBody ExamRequest.StudentSignDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        examService.학생사인저장(reqDTO, sessionUser);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }

    @PutMapping("/api/teacher/exam/update")
    public ResponseEntity<?> update(@RequestBody ExamRequest.UpdateDTO reqDTO) {
        examService.총평남기기(reqDTO);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }


    @GetMapping("/api/teacher/exam/result")
    public String teacherResult(Model model, @RequestParam("subjectId") Long subjectId) {
        List<ExamResponse.ResultDTO> respDTO = examService.교과목별시험결과(subjectId);
        model.addAttribute("models", respDTO);
        return "course/exam/teacher-result-list";
    }


    @GetMapping("/api/teacher/exam/course")
    public String course(Model model, @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = "id", page = 0) Pageable pageable) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        CourseResponse.PagingDTO respDTO = courseService.과정목록(sessionUser.getTeacher().getId(), pageable);
        model.addAttribute("paging", respDTO);
        return "course/exam/teacher-course-list";
    }

    @GetMapping("/api/teacher/exam/subject")
    public String subject(@RequestParam("courseId") Long courseId, Model model) {
        List<ExamResponse.SubjectDTO> respDTO = subjectService.과정별교과목(courseId);
        model.addAttribute("models", respDTO);
        return "course/exam/teacher-subject-list";
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

    @GetMapping("/api/teacher/exam/{examId}/result")
    public String teacherResultDetail(@PathVariable(value = "examId") Long examId, Model model) {
        ExamResponse.ResultDetailDTO respDTO = examService.시험친결과상세보기(examId);
        model.addAttribute("model", respDTO);
        return "course/exam/teacher-result-detail";
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
