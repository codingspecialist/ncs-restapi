package shop.mtcoding.blog.exam.application.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.blog._core.errors.exception.api.Exception403;
import shop.mtcoding.blog._core.errors.exception.api.Exception404;
import shop.mtcoding.blog._core.utils.Base64Util;
import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog._core.utils.Resp;
import shop.mtcoding.blog._core.utils.Script;
import shop.mtcoding.blog._core.utils.PasswordUtil;
import shop.mtcoding.blog._core.utils.JwtUtil;

import shop.mtcoding.blog.course.application.domain.Course;
import shop.mtcoding.blog.course.application.domain.CourseStudent;
import shop.mtcoding.blog.course.application.domain.CourseTeacher;
import shop.mtcoding.blog.course.application.domain.Subject;
import shop.mtcoding.blog.course.application.domain.SubjectElement;
import shop.mtcoding.blog.course.application.repository.CourseRepository;
import shop.mtcoding.blog.course.application.repository.CourseStudentRepository;
import shop.mtcoding.blog.course.application.repository.SubjectElementRepository;
import shop.mtcoding.blog.course.application.repository.SubjectRepository;
import shop.mtcoding.blog.exam.application.domain.Exam;
import shop.mtcoding.blog.exam.application.domain.ExamAnswer;
import shop.mtcoding.blog.exam.application.domain.ExamResult;
import shop.mtcoding.blog.exam.application.domain.Paper;
import shop.mtcoding.blog.exam.application.domain.Question;
import shop.mtcoding.blog.exam.application.domain.QuestionOption;
import shop.mtcoding.blog.exam.application.domain.enums.ExamNotTakenReason;
import shop.mtcoding.blog.exam.application.domain.enums.ExamResultStatus;
import shop.mtcoding.blog.exam.application.domain.enums.ExamTakingStatus;
import shop.mtcoding.blog.exam.application.domain.enums.PaperVersion;
import shop.mtcoding.blog.exam.application.domain.enums.EvaluationWay;
import shop.mtcoding.blog.exam.application.repository.ExamRepository;
import shop.mtcoding.blog.exam.application.repository.ExamAnswerRepository;
import shop.mtcoding.blog.exam.application.repository.ExamResultRepository;
import shop.mtcoding.blog.exam.application.repository.PaperRepository;
import shop.mtcoding.blog.exam.application.repository.QuestionRepository;
import shop.mtcoding.blog.exam.application.repository.QuestionOptionRepository;
import shop.mtcoding.blog.exam.application.repository.QuestionQueryRepository;
import shop.mtcoding.blog.exam.web.dto.ExamStudentRequest;
import shop.mtcoding.blog.exam.web.dto.ExamStudentResponse;
import shop.mtcoding.blog.exam.web.dto.ExamTeacherRequest;
import shop.mtcoding.blog.user.application.domain.Student;
import shop.mtcoding.blog.user.application.domain.Teacher;
import shop.mtcoding.blog.user.application.domain.User;
import shop.mtcoding.blog.user.application.repository.UserRepository;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ExamService {
        private final ExamRepository examRepository;
        private final PaperRepository paperRepository;
        private final QuestionRepository questionRepository;

        // 어뎁터로 가져와야함
        private final SubjectElementRepository subjectElementRepository;
        private final UserRepository userRepository;
        private final CourseStudentRepository courseStudentRepository;

        /// (객관식 -> Exam, ExamAnswer)
        @Transactional
        public void 학생객관식시험응시(ExamStudentRequest.McqSave reqDTO, User sessionUser) {
                // 1. 조회
                Paper paper = paperRepository.findById(reqDTO.getPaperId())
                                .orElseThrow(() -> new Exception404("시험지를 찾을 수 없어요"));

                User user = userRepository.findById(sessionUser.getId())
                                .orElseThrow(() -> new Exception404("학생을 찾을 수 없어요"));

                // 2. CourseStudent 객체 조회 (추가된 로직)
                CourseStudent courseStudent = courseStudentRepository.findByStudentId(user.getStudent().getId())
                                .orElseThrow(() -> new Exception404("해당 학생의 수강 정보를 찾을 수 없어요."));

                // 3. 재평가라면. 본평가를 찾아서 사용안함이라고 업데이트 해주기
                if (paper.isReTest()) {
                        Long subjectId = paper.getSubject().getId();
                        Long studentId = user.getStudent().getId();

                        Exam originalExam = examRepository
                                        .findBySubjectIdAndStudentIdAndIsUse(subjectId, studentId, true)
                                        .orElseThrow(() -> new Exception404("기존 본평가 시험을 찾을 수 없습니다."));

                        originalExam.deactivate();
                }

                // 4. 정답지 가져오기
                List<Question> questionList = questionRepository.findAllByPaperId(reqDTO.getPaperId());

                // 5. Exam과 ExamAnswer 비영속 객체 생성 (courseStudent를 전달)
                Exam exam = Exam.createMcqExam(courseStudent, paper);
                for (ExamStudentRequest.McqSave.AnswerDTO dto : reqDTO.getAnswers()) {
                        Question question = questionList.stream()
                                        .filter(q -> q.getNo().equals(dto.getQuestionNo()))
                                        .findFirst()
                                        .orElseThrow(() -> new Exception404(
                                                        "해당 questionNo 없음: " + dto.getQuestionNo()));
                        exam.addAnswer(dto.toEntity(exam, question));
                }

                // 6. 학생 제출 답안 저장하기
                examRepository.save(exam);
        }

        /// (객관식 -> Exam, ExamAnswer)
        @Transactional
        public void 학생루브릭시험응시(ExamStudentRequest.RubricSave reqDTO, User sessionUser) {
                // 1. 조회
                Paper paper = paperRepository.findById(reqDTO.getPaperId())
                                .orElseThrow(() -> new Exception404("시험지를 찾을 수 없어요"));

                User user = userRepository.findById(sessionUser.getId())
                                .orElseThrow(() -> new Exception404("학생을 찾을 수 없어요"));

                // 2. CourseStudent 객체 조회 (추가된 로직)
                CourseStudent courseStudent = courseStudentRepository.findByStudentId(user.getStudent().getId())
                                .orElseThrow(() -> new Exception404("해당 학생의 수강 정보를 찾을 수 없어요."));

                // 3. 재평가라면. 본평가를 찾아서 사용안함이라고 업데이트 해주기
                if (paper.isReTest()) {
                        Long subjectId = paper.getSubject().getId();
                        Long studentId = user.getStudent().getId();

                        Exam originalExam = examRepository
                                        .findBySubjectIdAndStudentIdAndIsUse(subjectId, studentId, true)
                                        .orElseThrow(() -> new Exception404("기존 본평가 시험을 찾을 수 없습니다."));

                        originalExam.deactivate();
                }

                // 4. 정답지 가져오기
                List<Question> questionList = questionRepository.findAllByPaperId(reqDTO.getPaperId());

                // 5. Exam과 ExamAnswer 비영속 객체 생성 (courseStudent를 전달)
                Exam exam = Exam.createRubricExam(courseStudent, paper, reqDTO.getRubricSubmitLink());
                for (ExamStudentRequest.RubricSave.Answer answer : reqDTO.getAnswers()) {
                        Question question = questionList.stream()
                                        .filter(q -> q.getNo().equals(answer.getQuestionNo()))
                                        .findFirst()
                                        .orElseThrow(() -> new Exception404(
                                                        "해당 questionNo 없음: " + answer.getQuestionNo()));
                        exam.addAnswer(answer.toEntity(exam, question));
                }

                // 6. 학생 제출 답안 저장하기
                examRepository.save(exam);
        }

        /// 1. 강사가 객관식을 채점한다.
        /// (채점시에도, 채점 업데이트시에도 사용한다)
        /// ExamResult, Exam에 점수 반영, Exam에 teacherComment 반영
        @Transactional
        public void 강사객관식채점하기(Long examId, ExamTeacherRequest.GradeMcq reqDTO) {
                // 1. 시험 찾기
                Exam exam = examRepository.findById(examId)
                                .orElseThrow(() -> new Exception404("응시한 시험이 존재하지 않아요"));

                // 2. 기존 시험과 시험 답변 업데이트 및 채점하기
                exam.applyMcqGrading(reqDTO.answers(), reqDTO.teacherComment());
        }

        /// 1. 강사가 루브릭을 채점한다.
        /// (채점시에도, 채점 업데이트시에도 사용한다)
        /// ExamResult, Exam에 점수 반영, Exam에 teacherComment 반영
        @Transactional
        public void 강사루브릭채점하기(Long examId, ExamTeacherRequest.GradeRubric reqDTO) {
                // 1. 시험 찾기
                Exam exam = examRepository.findById(examId)
                                .orElseThrow(() -> new Exception404("응시한 시험이 존재하지 않아요"));

                // 2. 기존 시험과 시험 답변 업데이트 및 채점하기
                exam.applyRubricGrading(reqDTO.answers(), reqDTO.teacherComment());
        }

        @Transactional
        public void 강사미응시이유처리(ExamTeacherRequest.NotTakenReason reqDTO) {
                // 1. 학생/시험지 조회
                User user = userRepository.findById(reqDTO.studentId())
                                .orElseThrow(() -> new Exception404("학생을 찾을 수 없습니다."));

                Paper paper = paperRepository.findById(reqDTO.paperId())
                                .orElseThrow(() -> new Exception404("시험지를 찾을 수 없습니다."));

                // 2. CourseStudent 객체 조회 (추가된 로직)
                CourseStudent courseStudent = courseStudentRepository.findByStudentId(user.getStudent().getId())
                                .orElseThrow(() -> new Exception404("해당 학생의 수강 정보를 찾을 수 없습니다."));

                // 3. 미응시 이유 확정
                Exam exam = Exam.createNotTakenExamWithReason(courseStudent, paper, reqDTO.notTakenReason());

                // 4. 저장
                examRepository.save(exam);
        }

        public List<ExamStudentResponse.Result> 강사교과목별시험결과(Long courseId, Long subjectId) {
                // 1. 시험지 조회 (여기서 subject도 접근 가능)
                Paper paper = paperRepository.findBySubjectIdAndPaperVersion(subjectId, PaperVersion.ORIGINAL)
                                .orElseThrow(() -> new Exception404("본평가 시험지를 찾을 수 없습니다"));

                // 2. 전체 학생 조회
                List<CourseStudent> students = courseStudentRepository.findAllByCourseId(courseId);

                // 3. 해당 학생들의 시험 조회
                List<Exam> exams = examRepository.findByCourseStudentInAndSubjectId(students, subjectId);

                // 4. 시험 Map 생성 (courseStudentId → Exam)
                Map<Long, Exam> examMap = exams.stream()
                                .collect(Collectors.toMap(e -> e.getCourseStudent().getId(), e -> e));

                // 5. 결과 매핑
                return students.stream()
                                .map(courseStudent -> {
                                        Exam exam = examMap.get(courseStudent.getId());
                                        return (exam != null)
                                                        ? ExamStudentResponse.Result.fromExam(exam)
                                                        : ExamStudentResponse.Result.createNotTakenTemplate(
                                                                        courseStudent.getStudent(), paper.getSubject(),
                                                                        paper);
                                })
                                .toList();
        }

        public ExamStudentResponse.ExamItems 학생시험결과목록(User sessionUser) {
                if (sessionUser.getStudent() == null)
                        throw new Exception403("당신은 학생이 아니에요 : 관리자에게 문의하세요");
                List<Exam> examListPS = examRepository.findAllByStudentId(sessionUser.getStudent().getId());

                return new ExamStudentResponse.ExamItems(examListPS);
        }

        public ExamStudentResponse.MyPaperItems 학생응시가능한시험지목록(User sessionUser) {
                // 1. 학생의 수강 정보 조회 (CourseStudent)
                // CourseStudent가 Student 엔티티를 가지고 있으므로, StudentId로 CourseStudent를 찾습니다.
                CourseStudent myCourseStudent = courseStudentRepository
                                .findByStudentId(sessionUser.getStudent().getId())
                                .orElseThrow(() -> new Exception404("해당 학생의 수강 정보를 찾을 수 없습니다."));

                // 2. 전체 시험지 조회
                // Paper가 Course를 가지고 있으므로, CourseId를 기준으로 모든 시험지를 찾습니다.
                List<Paper> allPapersInCourse = paperRepository.findAllByCourseId(myCourseStudent.getCourse().getId());

                // 3. 학생의 모든 응시 기록 조회 (myCourseStudent를 사용)
                List<Exam> myAllExams = examRepository.findAllByCourseStudentId(myCourseStudent.getId());

                // 4. 빠른 조회를 위해 응시 기록을 Map으로 변환
                Map<Long, Exam> myExamMap = myAllExams.stream()
                                .collect(Collectors.toMap(exam -> exam.getPaper().getId(), exam -> exam));

                // 5. 상태 계산 및 최종 목록 생성
                List<ExamStudentResponse.PaperItem> finalPaperList = allPapersInCourse.stream()
                                .map(paper -> {
                                        ExamTakingStatus status;

                                        if (myExamMap.containsKey(paper.getId())) {
                                                status = ExamTakingStatus.TAKEN;
                                        } else if (!paper.isReTest()) {
                                                status = ExamTakingStatus.AVAILABLE;
                                        } else {
                                                // 재평가 응시 가능 여부 판단
                                                boolean canTakeRetest = myAllExams.stream()
                                                                .filter(exam -> exam.getPaper().getSubject().getId()
                                                                                .equals(paper.getSubject().getId())
                                                                                && !exam.getPaper().isReTest())
                                                                .findFirst()
                                                                .map(mainExam -> mainExam
                                                                                .getResultStatus() == ExamResultStatus.FAIL
                                                                                || mainExam.getResultStatus() == ExamResultStatus.NOT_TAKEN)
                                                                .orElse(false);

                                                status = canTakeRetest ? ExamTakingStatus.AVAILABLE
                                                                : ExamTakingStatus.NOT_AVAILABLE;
                                        }
                                        return new ExamStudentResponse.PaperItem(paper, status);
                                })
                                .toList();

                // 6. 결과 반환
                return new ExamStudentResponse.MyPaperItems(sessionUser.getStudent().getId(), finalPaperList);
        }

        public ExamStudentResponse.Start 학생시험시작정보(User sessionUser, Long paperId) {
                // 1. 시험지 조회
                Paper paper = paperRepository.findById(paperId)
                                .orElseThrow(() -> new Exception404("시험지를 찾을 수 없습니다."));

                // 2. 과목 요소 조회
                List<SubjectElement> elements = subjectElementRepository.findAllBySubjectId(paper.getSubject().getId());

                // 3. 수험생 이름 조회
                User user = userRepository.findById(sessionUser.getId())
                                .orElseThrow(() -> new Exception404("학생을 찾을 수 없어요"));

                // 4. 문항 목록 조회
                List<Question> questions = questionRepository.findAllByPaperId(paperId);

                // 5. 모델 조립
                return new ExamStudentResponse.Start(paper, user.getStudent().getName(), elements, questions);
        }

        @Transactional
        public void 학생사인저장(ExamStudentRequest.SignDTO reqDTO) {
                Exam examPS = examRepository.findById(reqDTO.getExamId())
                                .orElseThrow(() -> new Exception404("응시한 시험이 존재하지 않아요"));

                examPS.updateStudentSign(reqDTO.getSign());
        }

        // 시험결과목록 -> 시험결과들상세 (프론트에서 시험상세 결과들중 선택된 examId로 pageview에서 보여줌)
        public ExamStudentResponse.ResultDetails 시험상세결과들(Long examId) {
                // 1. 시험 조회
                Exam exam = examRepository.findById(examId)
                                .orElseThrow(() -> new Exception404("시험 기록이 존재하지 않습니다."));

                // 2. 동일 교과목의 활성 시험 전체 조회 (학생 이름순 정렬)
                Long subjectId = exam.getPaper().getSubject().getId();
                List<Exam> exams = examRepository.findBySubjectIdAndIsUseOrderByStudentNameAsc(subjectId);

                // 3. 교과목 요소 및 교사 정보 조회
                List<SubjectElement> elements = subjectElementRepository.findAllBySubjectId(subjectId);

                return new ExamStudentResponse.ResultDetails(exam.getPaper().getEvaluationWay(),
                                exams, elements, exam.getCourseTeacher().getTeacher());
        }

        // 시험결과목록 -> 시험결과들상세 (프론트에서 시험상세 결과들중 선택된 examId로 pageview에서 보여줌)
        public ExamStudentResponse.ResultDetail 시험상세결과(Long examId) {
                // 1. 시험 조회
                Exam exam = examRepository.findById(examId)
                                .orElseThrow(() -> new Exception404("시험 기록이 존재하지 않습니다."));

                // 2. 동일 교과목의 활성 시험 전체 조회 (학생 이름순 정렬)
                Long subjectId = exam.getPaper().getSubject().getId();

                // 3. 교과목 요소 및 교사 정보 조회
                List<SubjectElement> elements = subjectElementRepository.findAllBySubjectId(subjectId);

                return new ExamStudentResponse.ResultDetail(exam.getPaper().getEvaluationWay(),
                                exam, elements, exam.getCourseTeacher().getTeacher());
        }

}
