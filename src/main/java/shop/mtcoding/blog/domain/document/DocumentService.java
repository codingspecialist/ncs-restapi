package shop.mtcoding.blog.domain.document;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.core.errors.exception.api.Exception404;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.course.CourseRepository;
import shop.mtcoding.blog.domain.course.exam.Exam;
import shop.mtcoding.blog.domain.course.exam.ExamRepository;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.SubjectRepository;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElementRepository;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.PaperRepository;
import shop.mtcoding.blog.domain.course.subject.paper.PaperVersion;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.QuestionRepository;
import shop.mtcoding.blog.domain.user.User;
import shop.mtcoding.blog.domain.user.teacher.Teacher;
import shop.mtcoding.blog.domain.user.teacher.TeacherRepository;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class DocumentService {

    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final QuestionRepository questionRepository;
    private final PaperRepository paperRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectElementRepository elementRepository;
    private final ExamRepository examRepository;

    public DocumentModel.CourseSlice 과정목록(User sessionUser, Pageable pageable) {
        Page<Course> coursePagePS = courseRepository.findAllByTeacherId(sessionUser.getTeacher().getId(), pageable);
        return new DocumentModel.CourseSlice(coursePagePS);
    }

    public DocumentModel.SubjectItems 교과목목록(Long courseId) {
        List<Subject> subjectListPS = subjectRepository.findAllByCourseId(courseId);
        return new DocumentModel.SubjectItems(subjectListPS);
    }

    public DocumentModel.No1 no1(Long subjectId) {
        Subject subjectPS = subjectRepository.findById(subjectId).orElseThrow(() -> new Exception404("해당 교과목이 없어요"));
        Teacher teacherPS = teacherRepository.findById(subjectPS.getTeacher().getId())
                .orElseThrow(() -> new Exception404("해당 선생님이 존재하지 않아요"));
        Paper paperPS = paperRepository.findBySubjectIdAndPaperVersion(subjectId, PaperVersion.ORIGINAL)
                .orElseThrow(() -> new Exception404("해당 교과목의 본평가 시험지를 찾을 수 없습니다."));
        List<Question> questionListPS = questionRepository.findAllByPaperId(paperPS.getId());
        return new DocumentModel.No1(subjectPS, questionListPS, teacherPS, paperPS);
    }

    public DocumentModel.No2 no2(Long subjectId) {
        Paper paperPS = paperRepository.findBySubjectIdAndPaperVersion(subjectId, PaperVersion.ORIGINAL)
                .orElseThrow(() -> new Exception404("해당 교과목의 본평가 시험지를 찾을 수 없습니다."));
        List<Question> questionListPS = questionRepository.findAllByPaperId(paperPS.getId());

        return new DocumentModel.No2(paperPS.getEvaluationWay(), paperPS.getSubject(), questionListPS);
    }

    public DocumentModel.No3 no3(Long subjectId) {
        Paper paperPS = paperRepository.findBySubjectIdAndPaperVersion(subjectId, PaperVersion.ORIGINAL)
                .orElseThrow(() -> new Exception404("해당 교과목의 본평가 시험지를 찾을 수 없습니다."));
        List<Question> questionListPS = questionRepository.findAllByPaperId(paperPS.getId());
        List<SubjectElement> elementListPS = elementRepository.findAllBySubjectId(subjectId);
        Teacher teacherPS = teacherRepository.findById(paperPS.getSubject().getTeacher().getId())
                .orElseThrow(() -> new Exception404("해당 시험에 선생님이 존재하지 않아서 사인을 찾을 수 없어요"));
        return new DocumentModel.No3(paperPS, elementListPS, questionListPS, teacherPS);
    }

    public DocumentModel.No4 no4(Long subjectId, Integer currentIndex) {
        List<Exam> examListPS = examRepository.findBySubjectIdAndIsUseOrderByStudentNameAsc(subjectId);
        Exam examPS = examListPS.get(currentIndex);
        if (examPS == null) throw new Exception404("시험친 기록이 없어요");

        Integer prevIndex = currentIndex > 0 ? currentIndex - 1 : null;
        Integer nextIndex = currentIndex < examListPS.size() - 1 ? currentIndex + 1 : null;

        List<SubjectElement> elementListPS = elementRepository.findAllBySubjectId(subjectId);
        Teacher teacherPS = teacherRepository.findById(examPS.getTeacher().getId())
                .orElseThrow(() -> new Exception404("해당 시험에 선생님이 존재하지 않아서 사인을 찾을 수 없어요"));

        return new DocumentModel.No4(examPS, elementListPS, teacherPS, prevIndex, nextIndex, currentIndex);
    }

    public DocumentModel.No5 no5(Long subjectId) {
        List<Exam> examListPS = examRepository.findAllBySubjectIdAndPaperVersion(subjectId, PaperVersion.ORIGINAL);
        List<Exam> reExamListPS = examRepository.findAllBySubjectIdAndPaperVersion(subjectId, PaperVersion.RETEST);
        Teacher teacherPS = teacherRepository.findById(examListPS.get(0).getTeacher().getId())
                .orElseThrow(() -> new Exception404("해당 선생님이 존재하지 않아요"));
        return new DocumentModel.No5(examListPS, reExamListPS, teacherPS);
    }

}