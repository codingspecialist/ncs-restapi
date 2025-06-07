package shop.mtcoding.blog.domain.course.exam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.core.errors.exception.Exception403;
import shop.mtcoding.blog.core.errors.exception.Exception404;
import shop.mtcoding.blog.core.errors.exception.Exception500;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswerRepository;
import shop.mtcoding.blog.domain.course.student.Student;
import shop.mtcoding.blog.domain.course.student.StudentRepository;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElementRepository;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.PaperRepository;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.QuestionRepository;
import shop.mtcoding.blog.domain.user.User;
import shop.mtcoding.blog.domain.user.UserType;
import shop.mtcoding.blog.domain.user.teacher.Teacher;
import shop.mtcoding.blog.domain.user.teacher.TeacherRepository;
import shop.mtcoding.blog.web.exam.ExamResponse;

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


    @Transactional
    public void 결석입력(ExamRequest.AbsentDTO reqDTO, User sessionUser) {
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
        Exam exam = Exam.createAbsentExam(student, paper);

        // 4. 저장
        examRepository.save(exam);
    }


    // 총평 수정하면서, 결과 점수도 같이 수정한다.
    @Transactional
    public void 총평남기기(Long examId, ExamRequest.UpdateDTO reqDTO) {
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

        // 4. 시험점수, 수준, 통과여부 업데이트 하기
        double score = examAnswers.stream().mapToInt(value -> value.getIsCorrect() ? value.getQuestion().getPoint() : 0).sum();

        // 5. 재평가지로 시험쳤으면 10%
        if (examPS.getPaper().isReEvaluation()) {
            score = score * 0.9;
        }

        // 6. 점수 입력 수준 입력
        examPS.updatePointAndGrade(score);

        // 7. 코멘트 수정 (총평 남기기, 총평 남긴 시간 남기기)
        examPS.updateTeacherComment(reqDTO.getTeacherComment());
    }

    public ExamResponse.ResultDetailDTO 시험결과상세(Long examId) {
        // 1. 시험 결과 찾기
        Exam examPS = examRepository.findById(examId)
                .orElseThrow(() -> new Exception404("시험친 기록이 없어요"));

        // 2. 본평가, 재평가중에 사용중인 평가들을 학생 이름순으로 조회 (가이름0, 나이름1, 다이름2)
        Long subjectId = examPS.getPaper().getSubject().getId();
        List<Exam> examListPS = examRepository.findBySubjectIdAndIsUseOrderByStudentNameAsc(subjectId);

        // 3. 현재 시험의 인덱스를 찾고, prev/next Id를 저장
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
        List<SubjectElement> subjectElementList = elementRepository.findBySubjectId(subjectId);
        Teacher teacher = teacherRepository.findByName(examPS.getTeacherName())
                .orElseThrow(() -> new Exception404("해당 시험에 선생님이 존재하지 않아서 사인을 찾을 수 없어요"));

        // 5. 본평가 ID 찾기 (내가 재평가일 때)
        Long originExamId = null;
        Long studentId = examPS.getStudent().getId();
        if (examPS.getExamState().equals("재평가")) {
            Exam reExamPS = examRepository.findBySubjectIdAndStudentIdAndIsNotUse(subjectId, studentId, false)
                    .orElseThrow(() -> new Exception500("재평가 본평가 저장 프로세스 오류 : 관리자 문의"));
            originExamId = reExamPS.getId();
        }

        return new ExamResponse.ResultDetailDTO(examPS, subjectElementList, teacher, prevExamId, nextExamId, currentIndex, originExamId);
    }

    public ExamResponse.ResultDetailDTO 미이수시험친결과상세보기(Long examId) {
        Exam examPS = examRepository.findById(examId)
                .orElseThrow(() -> new Exception404("응시한 시험이 존재하지 않아요"));

        Long subjectId = examPS.getPaper().getSubject().getId();

        List<SubjectElement> subjectElementListPS =
                elementRepository.findBySubjectId(subjectId);

        Teacher teacher = teacherRepository.findByName(examPS.getTeacherName())
                .orElseThrow(() -> new Exception404("해당 시험에 선생님이 존재하지 않아서 사인을 찾을 수 없어요"));

        return new ExamResponse.ResultDetailDTO(examPS, subjectElementListPS, teacher, null, null, null, null);
    }

    public ExamResponse.StartDTO 시험지(User sessionUser, Long paperId) {
        Paper paperPS = paperRepository.findById(paperId)
                .orElseThrow(() -> new Exception404("시험지가 존재하지 않아요"));


        List<SubjectElement> subjectElementListPS =
                elementRepository.findBySubjectId(paperPS.getSubject().getId());

        Student studentPS = studentRepository.findByUserId(sessionUser.getId());

        String studentName = studentPS.getName();

        List<Question> questionListPS = questionRepository.findByPaperId(paperId);

        // 시험일, 시험장소, 교과목명, 훈련교사명, 학생명, 문항수, 평가요소(elements), 시험문제들(문항점수포함)
        return new ExamResponse.StartDTO(paperPS, studentName, subjectElementListPS, questionListPS);
    }

    public ExamResponse.MyPaperListDTO 시험지목록(User sessionUser) {
        Long courseId = sessionUser.getStudent().getCourse().getId();
        Long studentId = sessionUser.getStudent().getId();

        // 1. 과정 내 모든 시험지 가져오기
        List<Paper> allPapers = paperRepository.findByCourseId(courseId);

        // 2. 해당 학생이 응시한 모든 시험
        List<Exam> myExams = examRepository.findByStudentId(studentId);

        // 3. 재평가 허용 대상 subjectId 추출
        Set<Long> eligibleRetestSubjectIds = myExams.stream()
                .filter(exam -> !exam.getPaper().isReEvaluation())
                .filter(exam -> {
                    boolean lowScore = exam.getScore() != null && exam.getScore() < 60;
                    boolean badReason = "미통과".equals(exam.getReExamReason()) || "결석".equals(exam.getReExamReason());
                    return lowScore || badReason;
                })
                .map(exam -> exam.getPaper().getSubject().getId())
                .collect(Collectors.toSet());

        // 4. 시험지 필터링
        List<Paper> filteredPapers = allPapers.stream()
                .filter(paper -> {
                    if (!paper.isReEvaluation()) {
                        return true; // 본평가는 항상 노출
                    } else if (paper.isReEvaluation()) {
                        Long subjectId = paper.getSubject().getId();
                        return eligibleRetestSubjectIds.contains(subjectId); // 조건 충족 시에만 노출
                    }
                    return false;
                })
                .toList();

        // 5. 응시 여부 매핑
        Map<Long, Boolean> attendanceMap = new HashMap<>();
        myExams.forEach(exam -> attendanceMap.put(exam.getPaper().getId(), true));

        return new ExamResponse.MyPaperListDTO(studentId, filteredPapers, attendanceMap);
    }


    @Transactional
    public void 시험응시(ExamRequest.SaveDTO reqDTO, User sessionUser) {
        // 1. Exam 저장
        Paper paper = paperRepository.findById(reqDTO.getPaperId())
                .orElseThrow(() -> new Exception404("시험지를 찾을 수 없어요"));

        Student student = studentRepository.findByUserId(sessionUser.getId());

        // 2. 재평가인데, 이전 시험(Exam)이 있으면 이전 시험 isNotUse로 변경
        // 재평가를 10번 해도, 모든 이전 재평가, 본평가는 isNotUse가 true가 됨
        if (paper.isReEvaluation()) {
            Optional<Exam> examOP = examRepository.findBySubjectIdAndStudentIdAndIsNotUse(paper.getSubject().getId(), student.getId(), true);

            if (examOP.isPresent()) {
                // 1. 새로운 평가가 저장되면, 기존 사용중인 평가를 사용안함으로 변경
                examOP.get().setNotUse();
            }
        }


        Exam exam = reqDTO.toEntity(paper, student, paper.getPaperType().toKorean(), 0.0, 0, "");
        Exam examPS = examRepository.save(exam);

        // 2. 정답지 가져오기
        List<Question> questionList = questionRepository.findByPaperId(reqDTO.getPaperId());

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
        double score = examAnswerList.stream().mapToInt(value -> value.getIsCorrect() ? value.getQuestion().getPoint() : 0).sum();

        // 5. 재평가지로 시험쳤으면 10%
        if (paper.isReEvaluation()) {
            score = score * 0.9;
        }

        // 6. 점수 입력 수준 입력
        examPS.updatePointAndGrade(score);


        // 7. 총평 자동화
        String teacherGoodComment = "";
        String teacherBadComment = "";

        for (ExamAnswer examAnswer : examAnswerList) {
            if (examAnswer.getIsCorrect()) {
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


    // 교과목 연번으로 정렬
    public List<ExamResponse.ResultDTO> 시험결과목록(User sessionUser) {
        if (sessionUser.getStudent() == null) throw new Exception403("당신은 학생이 아니에요 : 관리자에게 문의하세요");
        List<Exam> examListPS = examRepository.findByStudentId(sessionUser.getStudent().getId());

        return examListPS.stream().map(ExamResponse.ResultDTO::new).toList();
    }

    // 학생 연번으로 정렬
    public List<ExamResponse.ResultDTO> 교과목별시험결과(Long subjectId) {
        // 1. 시험지 목록 가져오기
        List<Paper> paperList = paperRepository.findBySubjectId(subjectId);
        if (paperList.isEmpty()) return List.of();

        // 2. 본평가 시험지만 추출
        Paper mainPaper = paperList.stream()
                .filter(p -> !p.isReEvaluation())
                .findFirst()
                .orElseThrow(() -> new Exception404("본평가 시험지가 없습니다."));

        // 3. 과정에 속한 수강생 모두 조회
        Long courseId = mainPaper.getSubject().getCourse().getId();
        List<Student> students = studentRepository.findByCourseId(courseId);

        // 4. 해당 과목의 모든 시험 응시 기록
        List<Exam> allExams = examRepository.findBySubjectId(subjectId);

        List<ExamResponse.ResultDTO> resultList = new ArrayList<>();

        for (Student student : students) {
            // 4-1. 그 학생이 응시한 시험 (isUse=true인 것)
            Optional<Exam> activeExamOP = allExams.stream()
                    .filter(e -> e.getStudent().getId().equals(student.getId()))
                    .filter(Exam::getIsUse)
                    .findFirst();

            if (activeExamOP.isPresent()) {
                resultList.add(new ExamResponse.ResultDTO(activeExamOP.get()));
            } else {
                // 시험을 안 친 경우 → 비영속 ResultDTO 생성
                ExamResponse.ResultDTO dto = new ExamResponse.ResultDTO();
                dto.setExamId(0L);
                dto.setPaperId(mainPaper.getId());
                dto.setStudentNo(99);
                dto.setSubjectNo(0);
                dto.setCourseNameAndRound(student.getCourse().getTitle() + "/" + student.getCourse().getRound() + "회차");
                dto.setSubjectTitle(mainPaper.getSubject().getTitle());
                dto.setExamState("본평가");
                dto.setStudentName(student.getName());
                dto.setTeacherName(mainPaper.getSubject().getTeacherName());
                dto.setExamScore(0.0);
                dto.setExamPassState("미응시");
                dto.setReExamReason("");
                dto.setIsNotPass(true);
                dto.setGrade(0);
                dto.setIsUse(false);
                dto.setIsNotStart(true);
                dto.setStudentId(student.getId());
                resultList.add(dto);
            }
        }

        resultList.sort(Comparator.comparing(ExamResponse.ResultDTO::getStudentNo));
        return resultList;
    }

    @Transactional
    public void 학생사인저장(ExamRequest.StudentSignDTO reqDTO, User sessionUser) {
        Exam examPS = examRepository.findById(reqDTO.getExamId())
                .orElseThrow(() -> new Exception404("응시한 시험이 존재하지 않아요"));

        examPS.updateSign(reqDTO.getSign());
    }
}
