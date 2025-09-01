package shop.mtcoding.blog.document.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.api.Exception404;
import shop.mtcoding.blog.course.application.domain.Course;
import shop.mtcoding.blog.course.application.domain.Subject;
import shop.mtcoding.blog.course.application.domain.SubjectElement;
import shop.mtcoding.blog.exam.application.domain.Exam;
import shop.mtcoding.blog.exam.application.domain.Paper;
import shop.mtcoding.blog.exam.application.domain.Question;
import shop.mtcoding.blog.exam.application.domain.enums.PaperVersion;
import shop.mtcoding.blog.exam.application.domain.enums.EvaluationWay;
import shop.mtcoding.blog.user.application.domain.Teacher;
import shop.mtcoding.blog.user.application.domain.User;
import shop.mtcoding.blog.document.adapter.CourseRepositoryAdapterInDocument;
import shop.mtcoding.blog.document.adapter.ExamRepositoryAdapterInDocument;
import shop.mtcoding.blog.document.adapter.UserRepositoryAdapterInDocument;
import shop.mtcoding.blog.document.web.dto.DocumentResponse;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class DocumentService {

    private final CourseRepositoryAdapterInDocument courseRepositoryAdapter;
    private final ExamRepositoryAdapterInDocument examRepositoryAdapter;
    private final UserRepositoryAdapterInDocument userRepositoryAdapter;

    public List<DocumentResponse.CourseDTO> 과정목록(User sessionUser, Pageable pageable) {
        Page<Course> coursePagePS = courseRepositoryAdapter.findCoursesByTeacherId(sessionUser.getTeacher().getId(),
                pageable);
        return coursePagePS.getContent().stream().map(DocumentResponse.CourseDTO::new).toList();
    }

    public List<DocumentResponse.SubjectDTO> 교과목목록(Long courseId) {
        List<Subject> subjectListPS = courseRepositoryAdapter.findSubjectsByCourseId(courseId);
        return subjectListPS.stream().map(DocumentResponse.SubjectDTO::new).toList();
    }

    public Object no1(Long subjectId) {
        Subject subjectPS = courseRepositoryAdapter.findSubjectById(subjectId)
                .orElseThrow(() -> new Exception404("해당 교과목이 없어요"));
        User teacherUser = userRepositoryAdapter.findUserById(subjectPS.getCourseTeacher().getTeacher().getId())
                .orElseThrow(() -> new Exception404("해당 선생님이 존재하지 않아요"));
        Teacher teacherPS = userRepositoryAdapter.getTeacherFromUser(teacherUser);
        Paper paperPS = examRepositoryAdapter.findPaperBySubjectIdAndPaperVersion(subjectId, PaperVersion.ORIGINAL)
                .orElseThrow(() -> new Exception404("해당 교과목의 본평가 시험지를 찾을 수 없습니다."));
        List<Question> questionListPS = examRepositoryAdapter.findQuestionsByPaperId(paperPS.getId());

        if (paperPS.getEvaluationWay() == EvaluationWay.MCQ) {
            return new DocumentResponse.No1McqDTO(subjectPS, questionListPS, teacherPS.getSign(), paperPS);
        } else {
            return new DocumentResponse.No1RubricDTO(subjectPS, questionListPS, teacherPS.getSign(), paperPS);
        }
    }

    public Object no2(Long subjectId) {
        Paper paperPS = examRepositoryAdapter.findPaperBySubjectIdAndPaperVersion(subjectId, PaperVersion.ORIGINAL)
                .orElseThrow(() -> new Exception404("해당 교과목의 본평가 시험지를 찾을 수 없습니다."));
        List<Question> questionListPS = examRepositoryAdapter.findQuestionsByPaperId(paperPS.getId());

        if (paperPS.getEvaluationWay() == EvaluationWay.MCQ) {
            return new DocumentResponse.No2McqDTO(paperPS.getSubject(), questionListPS);
        } else {
            return new DocumentResponse.No2RubricDTO(paperPS.getSubject(), questionListPS);
        }
    }

    public Object no3(Long subjectId) {
        Paper paperPS = examRepositoryAdapter.findPaperBySubjectIdAndPaperVersion(subjectId, PaperVersion.ORIGINAL)
                .orElseThrow(() -> new Exception404("해당 교과목의 본평가 시험지를 찾을 수 없습니다."));
        List<Question> questionListPS = examRepositoryAdapter.findQuestionsByPaperId(paperPS.getId());
        List<SubjectElement> elementListPS = courseRepositoryAdapter.findSubjectElementsBySubjectId(subjectId);
        User teacherUser = userRepositoryAdapter.findUserById(1L)
                .orElseThrow(() -> new Exception404("해당 시험에 선생님이 존재하지 않아서 사인을 찾을 수 없어요"));
        Teacher teacherPS = userRepositoryAdapter.getTeacherFromUser(teacherUser);

        if (paperPS.getEvaluationWay() == EvaluationWay.MCQ) {
            return new DocumentResponse.No3McqDTO(paperPS, elementListPS, questionListPS, teacherPS);
        } else {
            return new DocumentResponse.No3RubricDTO(paperPS, questionListPS, teacherPS);
        }
    }

    public Object no4(Long subjectId, Integer currentIndex) {
        List<Exam> examListPS = examRepositoryAdapter.findExamsBySubjectIdAndIsUseOrderByStudentNameAsc(subjectId);
        Exam examPS = examListPS.get(currentIndex);
        if (examPS == null)
            throw new Exception404("시험친 기록이 없어요");

        Integer prevIndex = currentIndex > 0 ? currentIndex - 1 : null;
        Integer nextIndex = currentIndex < examListPS.size() - 1 ? currentIndex + 1 : null;

        List<SubjectElement> elementListPS = courseRepositoryAdapter.findSubjectElementsBySubjectId(subjectId);
        User teacherUser = userRepositoryAdapter.findUserById(examPS.getCourseTeacher().getTeacher().getId())
                .orElseThrow(() -> new Exception404("해당 시험에 선생님이 존재하지 않아서 사인을 찾을 수 없어요"));
        Teacher teacherPS = userRepositoryAdapter.getTeacherFromUser(teacherUser);

        if (examPS.getPaper().getEvaluationWay() == EvaluationWay.MCQ) {
            return new DocumentResponse.No4McqDTO(examPS, elementListPS, teacherPS, prevIndex, nextIndex, currentIndex);
        } else {
            return new DocumentResponse.No4RubricDTO(examPS, elementListPS, teacherPS, prevIndex, nextIndex,
                    currentIndex);
        }
    }

    public DocumentResponse.No5DTO no5(Long subjectId) {
        List<Exam> examListPS = examRepositoryAdapter.findExamsBySubjectIdAndPaperVersion(subjectId,
                PaperVersion.ORIGINAL);
        List<Exam> reExamListPS = examRepositoryAdapter.findExamsBySubjectIdAndPaperVersion(subjectId,
                PaperVersion.RETEST);
        User teacherUser = userRepositoryAdapter.findUserById(examListPS.get(0).getCourseTeacher().getTeacher().getId())
                .orElseThrow(() -> new Exception404("해당 선생님이 존재하지 않아요"));
        Teacher teacherPS = userRepositoryAdapter.getTeacherFromUser(teacherUser);
        return new DocumentResponse.No5DTO(examListPS, reExamListPS, teacherPS);
    }
}