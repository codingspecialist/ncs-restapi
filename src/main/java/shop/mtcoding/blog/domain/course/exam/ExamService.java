package shop.mtcoding.blog.domain.course.exam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.core.errors.exception.api.Exception403;
import shop.mtcoding.blog.core.errors.exception.api.Exception404;
import shop.mtcoding.blog.core.errors.exception.api.Exception500;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswerRepository;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElementRepository;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.PaperRepository;
import shop.mtcoding.blog.domain.course.subject.paper.PaperType;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.QuestionRepository;
import shop.mtcoding.blog.domain.user.User;
import shop.mtcoding.blog.domain.user.student.Student;
import shop.mtcoding.blog.domain.user.student.StudentRepository;
import shop.mtcoding.blog.domain.user.teacher.Teacher;
import shop.mtcoding.blog.domain.user.teacher.TeacherRepository;
import shop.mtcoding.blog.web.exam.ExamRequest;
import shop.mtcoding.blog.web.student.exam.StudentExamRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final ExamAnswerRepository examAnswerRepository;
    private final PaperRepository paperRepository;
    private final StudentRepository studentRepository;
    private final SubjectElementRepository elementRepository;
    private final QuestionRepository questionRepository;
    private final TeacherRepository teacherRepository;
    private final ExamQueryRepository examQueryRepository;


    /// (객관식 -> Exam, ExamAnswer)
    @Transactional
    public void 학생객관식시험응시(StudentExamRequest.McqSaveDTO reqDTO, User sessionUser) {
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

    public List<ExamModel.Result> 강사_교과목별시험결과(Long courseId, Long subjectId) {
        Paper paperPS = paperRepository.findBySubjectIdAndPaperType(subjectId, PaperType.ORIGINAL)
                .orElseThrow(() -> new Exception404("본평가 시험지가 존재하지 않아요"));

        List<ExamModel.Result> rawList = examQueryRepository.findExamResult(subjectId, courseId);

        List<ExamModel.Result> modelData = rawList.stream()
                .map(r -> r.examId() == null ?
                        new ExamModel.Result(
                                0L,
                                r.studentName(),
                                paperPS.getSubject().getTitle(),
                                "본평가",
                                paperPS.getSubject().getTeacher().getName(),
                                0.0,
                                1,
                                "미응시",
                                "",
                                r.studentId(),
                                paperPS.getId(),
                                r.studentStatus(),
                                true, true)
                        : r
                ).toList();

        return modelData;
    }

    public ExamModel.ExamItems 학생_시험결과목록(User sessionUser) {
        if (sessionUser.getStudent() == null) throw new Exception403("당신은 학생이 아니에요 : 관리자에게 문의하세요");
        List<Exam> examListPS = examRepository.findByStudentId(sessionUser.getStudent().getId());

        return new ExamModel.ExamItems(examListPS);
    }

    // boss 로그인 시나리오
    // 과정 2
    // 학생 9

    public ExamModel.PaperItems 학생_응시가능한시험지목록(User sessionUser) {
        Long courseId = sessionUser.getStudent().getCourse().getId();
        Long studentId = sessionUser.getStudent().getId();

        // 1. 과정 내 모든 시험지 가져오기
        List<Paper> allPapers = paperRepository.findAllByCourseId(courseId);

        // 2. 해당 학생이 응시한 모든 시험
        List<Exam> myExams = examRepository.findByStudentId(studentId);

        // 3. 재평가 허용 대상 subjectId 추출
        Set<Long> eligibleRetestSubjectIds = myExams.stream()
                .filter(exam -> !exam.getPaper().isReTest())
                .filter(exam -> {
                    boolean reTestReason = exam.getResultState() == ExamResultStatus.FAIL
                            || exam.getResultState() == ExamResultStatus.ABSENT
                            || exam.getResultState() == ExamResultStatus.NOT_TAKEN;

                    return reTestReason;
                })
                .map(exam -> exam.getPaper().getSubject().getId())
                .collect(Collectors.toSet());

        // 4. 시험지 필터링
        List<Paper> filteredPapers = allPapers.stream()
                .filter(paper -> {
                    if (!paper.isReTest()) {
                        return true; // 본평가는 항상 노출
                    } else if (paper.isReTest()) {
                        Long subjectId = paper.getSubject().getId();
                        return eligibleRetestSubjectIds.contains(subjectId); // 조건 충족 시에만 노출
                    }
                    return false;
                })
                .toList();

        // 5. 응시 여부 매핑
        Map<Long, Boolean> attendanceMap = new HashMap<>();
        myExams.forEach(exam -> attendanceMap.put(exam.getPaper().getId(), true));

        return new ExamModel.PaperItems(studentId, filteredPapers, attendanceMap);
    }

    public List<ExamModel.Result> 강사_교과목별시험결과(Long courseId, Long subjectId) {
        Paper paperPS = paperRepository.findBySubjectIdAndPaperType(subjectId, PaperType.ORIGINAL)
                .orElseThrow(() -> new Exception404("본평가 시험지가 존재하지 않아요"));

        List<ExamModel.Result> rawList = examQueryRepository.findExamResult(subjectId, courseId);

        List<ExamModel.Result> modelData = rawList.stream()
                .map(r -> r.examId() == null ?
                        new ExamModel.Result(
                                0L,
                                r.studentName(),
                                paperPS.getSubject().getTitle(),
                                "본평가",
                                paperPS.getSubject().getTeacher().getName(),
                                0.0,
                                1,
                                "미응시",
                                "",
                                r.studentId(),
                                paperPS.getId(),
                                r.studentStatus(),
                                true, true)
                        : r
                ).toList();

        return modelData;
    }

    public ExamModel.Start 학생_시험시작정보(User sessionUser, Long paperId) {
        Paper paperPS = paperRepository.findById(paperId)
                .orElseThrow(() -> new Exception404("시험지가 존재하지 않아요"));


        List<SubjectElement> subjectElementListPS =
                elementRepository.findAllBySubjectId(paperPS.getSubject().getId());

        Student studentPS = studentRepository.findByUserId(sessionUser.getId());

        String studentName = studentPS.getName();

        List<Question> questionListPS = questionRepository.findAllByPaperId(paperId);

        return new ExamModel.Start(paperPS, studentName, subjectElementListPS, questionListPS);
    }


    public ExamModel.ResultDetail 학생_시험결과상세(Long examId) {
        return _examResultDetail(examId);
    }

    @Transactional
    public void 학생_사인저장(StudentExamRequest.SignDTO reqDTO) {
        Exam examPS = examRepository.findById(reqDTO.getExamId())
                .orElseThrow(() -> new Exception404("응시한 시험이 존재하지 않아요"));

        examPS.updateSign(reqDTO.getSign());
    }

    public ExamModel.ResultDetail 강사_시험결과상세(Long examId) {
        return _examResultDetail(examId);
    }

    private ExamModel.ResultDetail _examResultDetail(Long examId) {
        // 1. 시험 결과 찾기
        Exam examPS = examRepository.findById(examId)
                .orElseThrow(() -> new Exception404("시험친 기록이 없어요"));

        // 2. 본평가, 재평가 중 사용중인 평가들을 학생 이름순으로 조회
        Long subjectId = examPS.getPaper().getSubject().getId();
        List<Exam> examListPS = examRepository.findBySubjectIdAndIsUseOrderByStudentNameAsc(subjectId);

        // 3. 현재 시험의 인덱스를 찾고, prev/next Id 저장
        Long prevExamId = null;
        Long nextExamId = null;
        Integer currentIndex = 0;
        for (int i = 0; i < examListPS.size(); i++) {
            if (examListPS.get(i).getId().equals(examId)) {
                currentIndex = i;
                if (i > 0) prevExamId = examListPS.get(i - 1).getId();
                if (i < examListPS.size() - 1) nextExamId = examListPS.get(i + 1).getId();
                break;
            }
        }

        // 4. 교과목 요소와 선생님 사인 조회
        List<SubjectElement> subjectElementList = elementRepository.findAllBySubjectId(subjectId);
        Teacher teacher = teacherRepository.findByName(examPS.getTeacherName())
                .orElseThrow(() -> new Exception404("해당 시험에 선생님이 존재하지 않아서 사인을 찾을 수 없어요"));

        // 5. 본평가 ID 찾기 (재평가일 경우)
        Long originExamId = null;
        Long studentId = examPS.getStudent().getId();
        if (examPS.getExamState().equals("재평가")) {
            Exam reExamPS = examRepository.findBySubjectIdAndStudentIdAndIsUse(subjectId, studentId, false)
                    .orElseThrow(() -> new Exception500("재평가 본평가 저장 프로세스 오류 : 관리자 문의"));
            originExamId = reExamPS.getId();
        }

        return new ExamModel.ResultDetail(
                examPS,
                subjectElementList,
                teacher,
                prevExamId,
                nextExamId,
                currentIndex,
                originExamId
        );
    }

}
