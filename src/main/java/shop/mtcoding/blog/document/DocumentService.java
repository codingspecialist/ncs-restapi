package shop.mtcoding.blog.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.Exception404;
import shop.mtcoding.blog.course.Course;
import shop.mtcoding.blog.course.CourseRepository;
import shop.mtcoding.blog.course.exam.Exam;
import shop.mtcoding.blog.course.exam.ExamRepository;
import shop.mtcoding.blog.course.subject.Subject;
import shop.mtcoding.blog.course.subject.SubjectRepository;
import shop.mtcoding.blog.course.subject.element.SubjectElement;
import shop.mtcoding.blog.course.subject.element.SubjectElementRepository;
import shop.mtcoding.blog.paper.Paper;
import shop.mtcoding.blog.paper.PaperRepository;
import shop.mtcoding.blog.paper.question.Question;
import shop.mtcoding.blog.paper.question.QuestionRepository;
import shop.mtcoding.blog.user.UserRepository;
import shop.mtcoding.blog.user.teacher.Teacher;
import shop.mtcoding.blog.user.teacher.TeacherRepository;

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
    private final TeacherRepository teacherRepository;
    private final SubjectElementRepository elementRepository;
    private final ExamRepository examRepository;

    public DocumentResponse.No5DTO no5(Long courseId, Long subjectId) {
        List<Exam> examList = examRepository.findByExamGubun(false, subjectId); // 본평가들
        List<Exam> reExamList = examRepository.findByExamGubun(true, subjectId); // 재평가들

        Teacher teacherPS = teacherRepository.findByName(examList.get(0).getTeacherName())
                .orElseThrow(() -> new Exception404("해당 선생님이 존재하지 않아요"));

        return new DocumentResponse.No5DTO(examList, reExamList, teacherPS);
    }

    public DocumentResponse.No2DTO no2(Long courseId, Long subjectId) {
        Paper paperPS = paperRepository.findBySubjectIdAndPaperState(subjectId, false).get(0);
        List<Question> questionList = questionRepository.findByPaperId(paperPS.getId());

        return new DocumentResponse.No2DTO(paperPS.getSubject(), questionList);
    }

    public DocumentResponse.No4DTO no4(Long courseId, Long subjectId, Integer currentIndex) {
        // 1. 본평가, 재평가중에 사용중인 평가들을 학생 이름순으로 조회 (가이름0, 나이름1, 다이름2)
        List<Exam> examListPS = examRepository.findBySubjectIdAndIsUseOrderByStudentNameAsc(subjectId);

        // 2. 가이름 먼저 가져옴
        Exam examPS = examListPS.get(currentIndex);
        if (examPS == null) throw new Exception404("시험친 기록이 없어요");

        // 3. 이전 인덱스, 다음 인덱스 존재유무 확인
        Integer prevIndex = currentIndex - 1;
        Integer nextIndex = currentIndex + 1;

        if (prevIndex < 0) prevIndex = null;
        if (nextIndex >= examListPS.size()) nextIndex = null;

        // 4. 교과목 요소 찾기
        List<SubjectElement> subjectElementListPS =
                elementRepository.findBySubjectId(subjectId);

        // 5. 선생님 사인 찾기
        Teacher teacher = teacherRepository.findByName(examPS.getTeacherName())
                .orElseThrow(() -> new Exception404("해당 시험에 선생님이 존재하지 않아서 사인을 찾을 수 없어요"));


        return new DocumentResponse.No4DTO(examPS, subjectElementListPS, teacher, prevIndex, nextIndex, currentIndex);

    }

    public DocumentResponse.No3DTO no3(Long courseId, Long subjectId) {
        Paper paperPS = paperRepository.findBySubjectIdAndPaperState(subjectId, false).get(0);
        List<Question> questionListPS = questionRepository.findByPaperId(paperPS.getId());

        List<SubjectElement> subjectElementListPS =
                elementRepository.findBySubjectId(subjectId);

        Teacher teacher = teacherRepository.findByName(paperPS.getSubject().getTeacherName())
                .orElseThrow(() -> new Exception404("해당 시험에 선생님이 존재하지 않아서 사인을 찾을 수 없어요"));

        return new DocumentResponse.No3DTO(paperPS, subjectElementListPS, questionListPS, teacher);
    }

    public List<DocumentResponse.CourseDTO> 과정목록() {
        List<Course> courseListPS = courseRepository.findAll();
        return courseListPS.stream().map(DocumentResponse.CourseDTO::new).toList();
    }

    public List<DocumentResponse.SubjectDTO> 교과목목록(Long courseId) {
        List<Subject> subjectListPS = subjectRepository.findByCourseId(courseId);
        return subjectListPS.stream().map(DocumentResponse.SubjectDTO::new).toList();
    }

    public DocumentResponse.No1DTO no1(Long subjectId) {
        Subject subjectPS = subjectRepository.findById(subjectId).orElseThrow(
                () -> new Exception404("해당 교과목이 없어요")
        );


        Teacher teacherPS = teacherRepository.findByName(subjectPS.getTeacherName())
                .orElseThrow(() -> new Exception404("해당 선생님이 존재하지 않아요"));


        Paper paperPS = paperRepository.findBySubjectIdAndPaperState(subjectId, false).get(0);
        List<Question> questionListPS = questionRepository.findByPaperId(paperPS.getId());
        return new DocumentResponse.No1DTO(subjectPS, questionListPS, teacherPS.getSign(), paperPS);
    }
}
