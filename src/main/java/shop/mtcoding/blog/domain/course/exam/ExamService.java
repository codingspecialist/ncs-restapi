package shop.mtcoding.blog.domain.course.exam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.core.errors.exception.api.Exception403;
import shop.mtcoding.blog.core.errors.exception.api.Exception404;
import shop.mtcoding.blog.core.errors.exception.api.Exception500;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswerRepository;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElementRepository;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.PaperRepository;
import shop.mtcoding.blog.domain.course.subject.paper.PaperType;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.QuestionRepository;
import shop.mtcoding.blog.domain.user.User;
import shop.mtcoding.blog.domain.user.UserType;
import shop.mtcoding.blog.domain.user.student.Student;
import shop.mtcoding.blog.domain.user.student.StudentRepository;
import shop.mtcoding.blog.domain.user.teacher.Teacher;
import shop.mtcoding.blog.domain.user.teacher.TeacherRepository;
import shop.mtcoding.blog.web.exam.ExamRequest;
import shop.mtcoding.blog.web.student.exam.StudentExamRequest;

import java.util.*;
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
                    boolean reTestReason = exam.getResultState() == ExamResultState.FAIL
                            || exam.getResultState() == ExamResultState.ABSENT
                            || exam.getResultState() == ExamResultState.NOT_TAKEN;

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

    @Transactional
    public void 강사_결석처리(ExamRequest.AbsentDTO reqDTO, User sessionUser) {
        // 1. 유저가 선생님인지 검증
        if (UserType.STUDENT.equals(sessionUser.getRole())) {
            throw new Exception403("권한이 없습니다.");
        }

        // 2. 학생/시험지 조회
        Student student = studentRepository.findById(reqDTO.getStudentId())
                .orElseThrow(() -> new Exception404("학생을 찾을 수 없습니다."));

        Paper paper = paperRepository.findById(reqDTO.getPaperId())
                .orElseThrow(() -> new Exception404("시험지를 찾을 수 없습니다."));

        // 3. 결석 시험 생성
        Exam exam = Exam.createBlankExam(student, paper, ExamResultState.ABSENT);

        // 4. 저장
        examRepository.save(exam);
    }

    // 총평 남기기 or 루브릭 채점하기
    @Transactional
    public void 강사_총평남기기(Long examId, ExamRequest.UpdateDTO reqDTO) {

        System.out.println("받은 점수표 : " + reqDTO);
        Exam examPS = examRepository.findById(examId)
                .orElseThrow(() -> new Exception404("응시한 시험이 존재하지 않아요"));

        List<ExamAnswer> examAnswers = examPS.getExamAnswers();

        examAnswers.forEach(answer -> {
            reqDTO.getAnswers().forEach(answerDTO -> {
                if (answerDTO.getAnswerId().longValue() == answer.getId().longValue()) {
                    answerDTO.update(answer.getQuestion(), answer);
                }
            });
        });

        // 4. 시험점수, 수준, 통과여부 업데이트 하기 (이 부분이 주관식 업데이트할때 오류남 - 정답이 여러개니까 로직 다시 짜기)
        double score = examAnswers.stream()
                .mapToInt(answer -> {
                    Integer selectedOptionNo = answer.getSelectedOptionNo(); // 사용자가 고른 번호
                    List<QuestionOption> options = answer.getQuestion().getQuestionOptions();

                    return options.stream()
                            .filter(opt -> opt.getNo().equals(selectedOptionNo))
                            .mapToInt(QuestionOption::getPoint)
                            .findFirst()
                            .orElse(0);
                })
                .sum();

        // 5. 재평가지로 시험쳤으면 10%, 20% 등등
        if (examPS.getPaper().isReEvaluation()) {
            score = score * examPS.getSubject().getScorePolicy();
        }

        System.out.println("시험지 총점 15점 나와야함 : " + examPS.getPaper().sumQuestionPoints());

        // 6. 점수 입력 수준 입력
        examPS.updatePointAndGrade(score, examPS.getPaper().sumQuestionPoints());

        // 7. 코멘트 수정 (총평 남기기, 총평 남긴 시간 남기기)
        examPS.updateTeacherComment(reqDTO.getTeacherComment());
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

    @Transactional
    public void 학생_루브릭_시험응시(StudentExamRequest.RubricSaveDTO reqDTO, User sessionUser) {
        // 1. 시험지 찾기
        Paper paper = paperRepository.findById(reqDTO.getPaperId())
                .orElseThrow(() -> new Exception404("시험지가 존재하지 않아요"));

        Student student = studentRepository.findByUserId(sessionUser.getId());

        // 2. Exam 생성
//        private Long paperId;
//        private String teacherName;
//        private String submitLink;
//        private List<StudentExamRequest.RubricSaveDTO.AnswerDTO> answers;
        Exam exam = reqDTO.toEntity(paper, student, "채점중", 0.0, 0, "");

        Exam examPS = examRepository.save(exam);

        List<Question> questionList = questionRepository.findAllByPaperId(reqDTO.getPaperId());

        // 3. ExamAnswer 컬렉션 저장 (채점하기)
        List<ExamAnswer> examAnswerList = new ArrayList<>();

        questionList.forEach(question -> {
            reqDTO.getAnswers().forEach(answerDTO -> {
                if (answerDTO.getQuestionNo().equals(question.getNo())) {
                    examAnswerList.add(answerDTO.toEntity(question, examPS));
                }
            });
        });


        examAnswerRepository.saveAll(examAnswerList);
    }


    @Transactional
    public void 학생_객관식_시험응시(StudentExamRequest.McqSaveDTO reqDTO, User sessionUser) {
        // 1. Exam 저장
        Paper paper = paperRepository.findById(reqDTO.getPaperId())
                .orElseThrow(() -> new Exception404("시험지를 찾을 수 없어요"));

        Student student = studentRepository.findByUserId(sessionUser.getId());

        // 2. 재평가인데, 이전 시험(Exam)이 있으면 이전 시험 isNotUse로 변경
        // 재평가를 10번 해도, 모든 이전 재평가, 본평가는 isNotUse가 true가 됨
        if (paper.isReEvaluation()) {
            Optional<Exam> examOP = examRepository.findBySubjectIdAndStudentIdAndIsUse(paper.getSubject().getId(), student.getId(), true);

            if (examOP.isPresent()) {
                // 1. 새로운 평가가 저장되면, 기존 사용중인 평가를 사용안함으로 변경
                examOP.get().setNotUse();
            }
        }


        Exam exam = reqDTO.toEntity(paper, student, "채점중", 0.0, 0, "");
        Exam examPS = examRepository.save(exam);

        // 2. 정답지 가져오기
        List<Question> questionList = questionRepository.findAllByPaperId(reqDTO.getPaperId());

        // 3. ExamAnswer 컬렉션 저장 (채점하기)
        List<ExamAnswer> examAnswerList = new ArrayList<>();

        questionList.forEach(question -> {
            // 순회하면서 채점
            reqDTO.getAnswers().forEach(answerDTO -> {
                if (answerDTO.getQuestionNo() == question.getNo()) {
                    examAnswerList.add(answerDTO.toEntity(question, examPS));
                }
            });
        });

        // 4. 시험점수, 수준, 통과여부 업데이트 하기
        double score = examAnswerList.stream()
                .mapToInt(answer -> {
                    Integer selectedOptionNo = answer.getSelectedOptionNo(); // 사용자가 고른 번호
                    List<QuestionOption> options = answer.getQuestion().getQuestionOptions();

                    return options.stream()
                            .filter(opt -> opt.getNo().equals(selectedOptionNo))
                            .mapToInt(QuestionOption::getPoint)
                            .findFirst()
                            .orElse(0);
                })
                .sum();

        // 5. 재평가지로 시험쳤으면 10%
        if (paper.isReEvaluation()) {
            score = score * 0.9;
        }

        // 6. 점수 입력 수준 입력
        examPS.updatePointAndGrade(score, paper.sumQuestionPoints());


        // 7. 총평 자동화
        String teacherGoodComment = "";
        String teacherBadComment = "";

        for (ExamAnswer examAnswer : examAnswerList) {
            if (examAnswer.getIsRight()) {
                teacherGoodComment += examAnswer.getQuestion().getSubjectElement().getSubtitle() + ", ";
            } else {
                teacherBadComment += examAnswer.getQuestion().getSubjectElement().getSubtitle() + ", ";
            }
        }

        int goodIndex = teacherGoodComment.lastIndexOf(", ");
        int badIndex = teacherBadComment.lastIndexOf(", ");

        if (goodIndex != -1) teacherGoodComment = teacherGoodComment.substring(0, goodIndex);

        if (badIndex != -1) teacherBadComment = teacherBadComment.substring(0, badIndex);

        if (teacherGoodComment.length() > 0) {
            if (teacherBadComment.length() == 0) {
                teacherGoodComment += " 부분을 잘이해하고 있습니다.";
            } else {
                teacherGoodComment += " 부분을 잘이해하고 있고, ";
            }
        }

        if (teacherBadComment.length() > 0) {
            teacherBadComment += " 부분이 부족합니다.";
        }

        examPS.updateTeacherComment(teacherGoodComment + teacherBadComment);

        // 5. 학생 제출 답안 저장하기
        examAnswerRepository.saveAll(examAnswerList);
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
