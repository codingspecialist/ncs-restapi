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
import shop.mtcoding.blog.user.User;
import shop.mtcoding.blog.user.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

    public DocumentResponse.No5DTO no5(Long courseId, Long subjectId) {
        List<Exam> examList = examRepository.findByExamGubun(false, subjectId); // 본평가들
        List<Exam> reExamList = examRepository.findByExamGubun(true, subjectId); // 재평가들

        User teacherPS = userRepository.findByTeacherName(examList.get(0).getTeacherName())
                .orElseThrow(() -> new Exception404("해당 선생님이 존재하지 않아요"));

        return new DocumentResponse.No5DTO(examList, reExamList, teacherPS);
    }

    public DocumentResponse.No2DTO no2(Long courseId, Long subjectId) {
        Paper paperPS = paperRepository.findBySubjectIdAndPaperState(subjectId, false).get(0);
        List<Question> questionList = questionRepository.findByPaperId(paperPS.getId());

        return new DocumentResponse.No2DTO(paperPS.getSubject(), questionList);
    }

    public DocumentResponse.No4DTO no4(Long courseId, Long subjectId, Long examId) {
        Exam examPS = null;
        if (examId == 0) {
            List<Exam> examListPS = examRepository.findBySubjectIdAndIsUse(subjectId);

            Optional<Exam> minExamOpt = examListPS.stream()
                    .min(Comparator.comparingLong(Exam::getId));

            if (minExamOpt.isPresent()) {
                examPS = minExamOpt.get();
            } else {
                throw new Exception404("시험친 기록이 없어요");
            }
        } else {
            examPS = examRepository.findById(examId)
                    .orElseThrow(() -> new Exception404("시험을 치지 않았네요"));
        }

        List<SubjectElement> subjectElementListPS =
                elementRepository.findBySubjectId(subjectId);

        User teacher = userRepository.findByTeacherName(examPS.getTeacherName())
                .orElseThrow(() -> new Exception404("해당 시험에 선생님이 존재하지 않아서 사인을 찾을 수 없어요"));

        // 현재 학생 번호 찾기
        Integer currentStudentNo = examPS.getStudent().getStudentNo();

        // NOTE: 다음 학생 번호, 이전 학생 번호로 ExamId 찾기 (만약에 재평가와 본평가가 있으면 재평가만 불러오기)
        Long prevExamId = examRepository.findByStudentNoToExamId(subjectId, currentStudentNo - 1, true);
        Long nextExamId = examRepository.findByStudentNoToExamId(subjectId, currentStudentNo + 1, true);


        return new DocumentResponse.No4DTO(examPS, subjectElementListPS, teacher, prevExamId, nextExamId);
    }

    public DocumentResponse.No3DTO no3(Long courseId, Long subjectId) {
        Paper paperPS = paperRepository.findBySubjectIdAndPaperState(subjectId, false).get(0);
        List<Question> questionListPS = questionRepository.findByPaperId(paperPS.getId());

        List<SubjectElement> subjectElementListPS =
                elementRepository.findBySubjectId(subjectId);

        User teacher = userRepository.findByTeacherName(paperPS.getSubject().getTeacherName())
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


        User teacherPS = userRepository.findByTeacherName(subjectPS.getTeacherName())
                .orElseThrow(() -> new Exception404("해당 선생님이 존재하지 않아요"));


        Paper paperPS = paperRepository.findBySubjectIdAndPaperState(subjectId, false).get(0);
        List<Question> questionListPS = questionRepository.findByPaperId(paperPS.getId());
        return new DocumentResponse.No1DTO(subjectPS, questionListPS, teacherPS.getSign(), paperPS);
    }
}
