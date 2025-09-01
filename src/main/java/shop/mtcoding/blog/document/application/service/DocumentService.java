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
import shop.mtcoding.blog.course.application.repository.CourseRepository;
import shop.mtcoding.blog.course.application.repository.SubjectRepository;
import shop.mtcoding.blog.course.application.repository.SubjectElementRepository;
import shop.mtcoding.blog.exam.application.domain.Exam;
import shop.mtcoding.blog.exam.application.domain.Paper;
import shop.mtcoding.blog.exam.application.domain.Question;
import shop.mtcoding.blog.exam.application.domain.enums.PaperVersion;
import shop.mtcoding.blog.exam.application.repository.ExamRepository;
import shop.mtcoding.blog.exam.application.repository.PaperRepository;
import shop.mtcoding.blog.exam.application.repository.QuestionRepository;
import shop.mtcoding.blog.user.application.domain.Teacher;
import shop.mtcoding.blog.user.application.domain.User;
import shop.mtcoding.blog.user.application.repository.UserRepository;
import shop.mtcoding.blog.document.web.dto.DocumentResponse;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class DocumentService {

    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final QuestionRepository questionRepository;
    private final PaperRepository paperRepository;
    private final UserRepository userRepository;
    private final SubjectElementRepository elementRepository;
    private final ExamRepository examRepository;

    public DocumentResponse.CourseSlice 과정목록(User sessionUser, Pageable pageable) {
        Page<Course> coursePagePS = courseRepository.findAllByTeacherId(sessionUser.getTeacher().getId(), pageable);
        return new DocumentResponse.CourseSlice(coursePagePS);
    }

    public DocumentResponse.SubjectItems 교과목목록(Long courseId) {
        List<Subject> subjectListPS = subjectRepository.findAllByCourseId(courseId);
        return new DocumentResponse.SubjectItems(subjectListPS);
    }

    public DocumentResponse.No1 no1(Long subjectId) {
        Subject subjectPS = subjectRepository.findById(subjectId).orElseThrow(() -> new Exception404("해당 교과목이 없어요"));
        User teacherUser = userRepository.findById(subjectPS.getCourseTeacher().getTeacher().getId())
                .orElseThrow(() -> new Exception404("해당 선생님이 존재하지 않아요"));
        Teacher teacherPS = teacherUser.getTeacher();
        Paper paperPS = paperRepository.findBySubjectIdAndPaperVersion(subjectId, PaperVersion.ORIGINAL)
                .orElseThrow(() -> new Exception404("해당 교과목의 본평가 시험지를 찾을 수 없습니다."));
        List<Question> questionListPS = questionRepository.findAllByPaperId(paperPS.getId());
        return new DocumentResponse.No1(subjectPS, questionListPS, teacherPS, paperPS);
    }

    public DocumentResponse.No2 no2(Long subjectId) {
        Paper paperPS = paperRepository.findBySubjectIdAndPaperVersion(subjectId, PaperVersion.ORIGINAL)
                .orElseThrow(() -> new Exception404("해당 교과목의 본평가 시험지를 찾을 수 없습니다."));
        List<Question> questionListPS = questionRepository.findAllByPaperId(paperPS.getId());

        return new DocumentResponse.No2(paperPS.getEvaluationWay(), null, questionListPS);
    }

    public DocumentResponse.No3 no3(Long subjectId) {
        Paper paperPS = paperRepository.findBySubjectIdAndPaperVersion(subjectId, PaperVersion.ORIGINAL)
                .orElseThrow(() -> new Exception404("해당 교과목의 본평가 시험지를 찾을 수 없습니다."));
        List<Question> questionListPS = questionRepository.findAllByPaperId(paperPS.getId());
        List<SubjectElement> elementListPS = elementRepository.findAllBySubjectId(subjectId);
        User teacherUser = userRepository.findById(1L)
                .orElseThrow(() -> new Exception404("해당 시험에 선생님이 존재하지 않아서 사인을 찾을 수 없어요"));
        Teacher teacherPS = teacherUser.getTeacher();
        return new DocumentResponse.No3(paperPS, elementListPS, questionListPS, teacherPS);
    }

    public DocumentResponse.No4 no4(Long subjectId, Integer currentIndex) {
        List<Exam> examListPS = examRepository.findBySubjectIdAndIsUseOrderByStudentNameAsc(subjectId);
        Exam examPS = examListPS.get(currentIndex);
        if (examPS == null)
            throw new Exception404("시험친 기록이 없어요");

        Integer prevIndex = currentIndex > 0 ? currentIndex - 1 : null;
        Integer nextIndex = currentIndex < examListPS.size() - 1 ? currentIndex + 1 : null;

        List<SubjectElement> elementListPS = elementRepository.findAllBySubjectId(subjectId);
        User teacherUser = userRepository.findById(examPS.getCourseTeacher().getTeacher().getId())
                .orElseThrow(() -> new Exception404("해당 시험에 선생님이 존재하지 않아서 사인을 찾을 수 없어요"));
        Teacher teacherPS = teacherUser.getTeacher();

        return new DocumentResponse.No4(examPS, elementListPS, teacherPS, prevIndex, nextIndex, currentIndex);
    }

    public DocumentResponse.No5 no5(Long subjectId) {
        List<Exam> examListPS = examRepository.findAllBySubjectIdAndPaperVersion(subjectId, PaperVersion.ORIGINAL);
        List<Exam> reExamListPS = examRepository.findAllBySubjectIdAndPaperVersion(subjectId, PaperVersion.RETEST);
        User teacherUser = userRepository.findById(examListPS.get(0).getCourseTeacher().getTeacher().getId())
                .orElseThrow(() -> new Exception404("해당 선생님이 존재하지 않아요"));
        Teacher teacherPS = teacherUser.getTeacher();
        return new DocumentResponse.No5(examListPS, reExamListPS, teacherPS);
    }
}