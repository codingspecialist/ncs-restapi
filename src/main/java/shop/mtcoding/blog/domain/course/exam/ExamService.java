package shop.mtcoding.blog.domain.course.exam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.core.errors.exception.api.Exception403;
import shop.mtcoding.blog.core.errors.exception.api.Exception404;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElementRepository;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.PaperRepository;
import shop.mtcoding.blog.domain.course.subject.paper.PaperVersion;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.QuestionRepository;
import shop.mtcoding.blog.domain.user.User;
import shop.mtcoding.blog.domain.user.student.Student;
import shop.mtcoding.blog.domain.user.student.StudentRepository;
import shop.mtcoding.blog.domain.user.teacher.Teacher;
import shop.mtcoding.blog.domain.user.teacher.TeacherRepository;
import shop.mtcoding.blog.web.exam.ExamRequest;
import shop.mtcoding.blog.web.student.exam.StudentExamRequest;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final PaperRepository paperRepository;
    private final StudentRepository studentRepository;
    private final SubjectElementRepository elementRepository;
    private final QuestionRepository questionRepository;
    private final TeacherRepository teacherRepository;


    /// (객관식 -> Exam, ExamAnswer)
    @Transactional
    public void 학생객관식시험응시(StudentExamRequest.McqSave reqDTO, User sessionUser) {
        // 1. 조회
        Paper paper = paperRepository.findById(reqDTO.getPaperId())
                .orElseThrow(() -> new Exception404("시험지를 찾을 수 없어요"));

        Student student = studentRepository.findByUserId(sessionUser.getId())
                .orElseThrow(() -> new Exception404("학생을 찾을 수 없어요"));

        // 2. 재평가라면. 본평가를 찾아서 사용안함이라고 업데이트 해주기
        if (paper.isReTest()) {
            Long subjectId = paper.getSubject().getId();
            Long studentId = student.getId();

            Exam originalExam = examRepository.findBySubjectIdAndStudentIdAndIsUse(subjectId, studentId, true)
                    .orElseThrow(() -> new Exception404("기존 본평가 시험을 찾을 수 없습니다."));

            originalExam.deactivate();
        }

        // 3. 정답지 가져오기
        List<Question> questionList = questionRepository.findAllByPaperId(reqDTO.getPaperId());

        // 4. Exam과 ExamAnswer 비영속 객체 생성
        Exam exam = reqDTO.toEntityWithAnswers(student, paper, questionList);

        // 5. 학생 제출 답안 저장하기 (Exam, ExamAnswer, ExamResult)
        examRepository.save(exam);
    }


    /// (객관식 -> Exam, ExamAnswer)
    @Transactional
    public void 학생루브릭시험응시(StudentExamRequest.RubricSave reqDTO, User sessionUser) {
        // 1. 조회
        Paper paper = paperRepository.findById(reqDTO.getPaperId())
                .orElseThrow(() -> new Exception404("시험지를 찾을 수 없어요"));

        Student student = studentRepository.findByUserId(sessionUser.getId())
                .orElseThrow(() -> new Exception404("학생을 찾을 수 없어요"));

        // 2. 재평가라면. 본평가를 찾아서 사용안함이라고 업데이트 해주기
        if (paper.isReTest()) {
            Long subjectId = paper.getSubject().getId();
            Long studentId = student.getId();

            Exam originalExam = examRepository.findBySubjectIdAndStudentIdAndIsUse(subjectId, studentId, true)
                    .orElseThrow(() -> new Exception404("기존 본평가 시험을 찾을 수 없습니다."));

            originalExam.deactivate();
        }

        // 3. 정답지 가져오기
        List<Question> questionList = questionRepository.findAllByPaperId(reqDTO.getPaperId());

        // 4. Exam과 ExamAnswer 비영속 객체 생성
        Exam exam = reqDTO.toEntityWithAnswers(student, paper, questionList);

        // 5. 학생 제출 답안 저장하기 (Exam, ExamAnswer, ExamResult)
        examRepository.save(exam);
    }

    /// 1. 강사가 객관식을 채점한다.
    /// (채점시에도, 채점 업데이트시에도 사용한다)
    /// ExamResult, Exam에 점수 반영, Exam에 teacherComment 반영
    @Transactional
    public void 강사객관식채점하기(Long examId, ExamRequest.GradeMcq reqDTO) {
        // 1. 시험 찾기
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new Exception404("응시한 시험이 존재하지 않아요"));

        // 2. 기존 시험과 시험 답변 업데이트 및 채점하기
        exam.applyMcqGrading(reqDTO.getAnswers(), reqDTO.getTeacherComment());
    }

    /// 1. 강사가 루브릭을 채점한다.
    /// (채점시에도, 채점 업데이트시에도 사용한다)
    /// ExamResult, Exam에 점수 반영, Exam에 teacherComment 반영
    @Transactional
    public void 강사루브릭채점하기(Long examId, ExamRequest.GradeRubric reqDTO) {
        // 1. 시험 찾기
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new Exception404("응시한 시험이 존재하지 않아요"));

        // 2. 기존 시험과 시험 답변 업데이트 및 채점하기
        exam.applyRubricGrading(reqDTO.getAnswers(), reqDTO.getTeacherComment());
    }

    @Transactional
    public void 강사미응시이유처리(ExamRequest.NotTakenReason reqDTO) {
        // 1. 학생/시험지 조회
        Student student = studentRepository.findById(reqDTO.getStudentId())
                .orElseThrow(() -> new Exception404("학생을 찾을 수 없습니다."));

        Paper paper = paperRepository.findById(reqDTO.getPaperId())
                .orElseThrow(() -> new Exception404("시험지를 찾을 수 없습니다."));

        // 2. 미응시 이유 확정
        Exam exam = Exam.createNotTakenExamWithReason(student, paper, reqDTO.getNotTakenReason());

        // 3. 저장
        examRepository.save(exam);
    }

    public List<ExamModel.Result> 강사교과목별시험결과(Long courseId, Long subjectId) {
        // 1. 시험지 조회 (여기서 subject도 접근 가능)
        Paper paper = paperRepository.findBySubjectIdAndPaperType(subjectId, PaperVersion.ORIGINAL)
                .orElseThrow(() -> new Exception404("본평가 시험지를 찾을 수 없습니다"));
        Subject subject = paper.getSubject();

        // 2. 전체 학생 조회
        List<Student> students = studentRepository.findAllByCourseId(courseId);

        // 3. 해당 학생들의 시험 조회
        List<Long> studentIds = students.stream()
                .map(Student::getId)
                .toList();

        List<Exam> exams = examRepository.findByStudentIdInAndSubjectId(studentIds, subjectId);

        // 4. 시험 Map 생성 (studentId → Exam)
        Map<Long, Exam> examMap = exams.stream()
                .collect(Collectors.toMap(e -> e.getStudent().getId(), e -> e));

        // 5. 결과 매핑
        return students.stream()
                .map(student -> {
                    Exam exam = examMap.get(student.getId());
                    return (exam != null)
                            ? ExamModel.Result.fromExam(exam)
                            : ExamModel.Result.createNotTakenTemplate(student, subject, paper);
                })
                .toList();
    }


    public ExamModel.ExamItems 학생시험결과목록(User sessionUser) {
        if (sessionUser.getStudent() == null) throw new Exception403("당신은 학생이 아니에요 : 관리자에게 문의하세요");
        List<Exam> examListPS = examRepository.findByStudentId(sessionUser.getStudent().getId());

        return new ExamModel.ExamItems(examListPS);
    }

    public ExamModel.PaperItems 학생응시가능한시험지목록(User sessionUser) {
        Long courseId = sessionUser.getStudent().getCourse().getId();
        Long studentId = sessionUser.getStudent().getId();

        // 1. 과정 내 전체 시험지 조회
        List<Paper> allPapers = paperRepository.findAllByCourseId(courseId);

        // 2. 해당 학생이 응시한 시험 전체 조회
        List<Exam> myExams = examRepository.findByStudentId(studentId);

        // 3. 재응시 허용 대상 과목 subjectId 수집
        Set<Long> reTestableSubjectIds = myExams.stream()
                .filter(exam -> !exam.getPaper().isReTest())
                .filter(exam -> {
                    ExamResultStatus status = exam.getResultStatus();
                    return status == ExamResultStatus.FAIL
                            || status == ExamResultStatus.NOT_TAKEN;
                })
                .map(exam -> exam.getSubject().getId())
                .collect(Collectors.toSet());

        // 4. 응시 가능한 시험지만 필터링 (본평가는 항상, 재평가는 조건 충족 시)
        List<Paper> availablePapers = allPapers.stream()
                .filter(paper -> {
                    if (!paper.isReTest()) return true;
                    Long subjectId = paper.getSubject().getId();
                    return reTestableSubjectIds.contains(subjectId);
                })
                .toList();

        // 5. 응시 여부 매핑
        Map<Long, Boolean> attendanceMap = myExams.stream()
                .map(exam -> exam.getPaper().getId())
                .distinct()
                .collect(Collectors.toMap(paperId -> paperId, paperId -> true));

        return new ExamModel.PaperItems(studentId, availablePapers, attendanceMap);
    }

    public ExamModel.Start 학생시험시작정보(User sessionUser, Long paperId) {
        // 1. 시험지 조회
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new Exception404("시험지를 찾을 수 없습니다."));

        // 2. 과목 요소 조회
        List<SubjectElement> elements = elementRepository.findAllBySubjectId(paper.getSubject().getId());

        // 3. 수험생 이름 조회
        Student student = studentRepository.findByUserId(sessionUser.getId())
                .orElseThrow(() -> new Exception404("학생을 찾을 수 없어요"));

        // 4. 문항 목록 조회
        List<Question> questions = questionRepository.findAllByPaperId(paperId);

        // 5. 모델 조립
        return new ExamModel.Start(paper, student.getName(), elements, questions);
    }

    @Transactional
    public void 학생사인저장(StudentExamRequest.SignDTO reqDTO) {
        Exam examPS = examRepository.findById(reqDTO.getExamId())
                .orElseThrow(() -> new Exception404("응시한 시험이 존재하지 않아요"));

        examPS.updateStudentSign(reqDTO.getSign());
    }

    // 시험결과목록 -> 시험결과들상세 (프론트에서 시험상세 결과들중 선택된 examId로 pageview에서 보여줌)
    public ExamModel.ResultDetails 시험상세결과들(Long examId) {
        // 1. 시험 조회
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new Exception404("시험 기록이 존재하지 않습니다."));

        // 2. 동일 교과목의 활성 시험 전체 조회 (학생 이름순 정렬)
        Long subjectId = exam.getPaper().getSubject().getId();
        List<Exam> exams = examRepository.findBySubjectIdAndIsUseOrderByStudentNameAsc(subjectId);

        // 3. 교과목 요소 및 교사 정보 조회
        List<SubjectElement> elements = elementRepository.findAllBySubjectId(subjectId);
        Teacher teacher = teacherRepository.findById(exam.getTeacher().getId())
                .orElseThrow(() -> new Exception404("해당 시험의 교사를 찾을 수 없습니다."));


        return new ExamModel.ResultDetails(exam.getPaper().getEvaluationWay(),
                exams, elements, teacher
        );
    }

    // 시험결과목록 -> 시험결과들상세 (프론트에서 시험상세 결과들중 선택된 examId로 pageview에서 보여줌)
    public ExamModel.ResultDetail 시험상세결과(Long examId) {
        // 1. 시험 조회
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new Exception404("시험 기록이 존재하지 않습니다."));

        // 2. 동일 교과목의 활성 시험 전체 조회 (학생 이름순 정렬)
        Long subjectId = exam.getPaper().getSubject().getId();


        // 3. 교과목 요소 및 교사 정보 조회
        List<SubjectElement> elements = elementRepository.findAllBySubjectId(subjectId);
        Teacher teacher = teacherRepository.findById(exam.getTeacher().getId())
                .orElseThrow(() -> new Exception404("해당 시험의 교사를 찾을 수 없습니다."));

        return new ExamModel.ResultDetail(exam.getPaper().getEvaluationWay(),
                exam, elements, teacher
        );
    }


}
