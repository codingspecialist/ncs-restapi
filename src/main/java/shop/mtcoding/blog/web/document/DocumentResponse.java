package shop.mtcoding.blog.web.document;

import lombok.Data;
import shop.mtcoding.blog.core.utils.MyUtil;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.course.exam.Exam;
import shop.mtcoding.blog.domain.course.exam.ExamResultStatus;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.QuestionOption;
import shop.mtcoding.blog.domain.user.teacher.Teacher;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DocumentResponse {

    @Data
    public static class No5DTO {
        private String draftingTeam; // 교육운영팀
        private String docNumber; // 문서번호
        private String writingDate;
        private String requestDate;
        private String retentionPeriod; // 5년
        private String author; // 작성자
        private String recipient; // 내부결재
        private String title;

        private String ga; // course.title
        private String na; // course.startDate + endDate
        private String da; // subject.title (subject.evaluationWay)
        private String ra; // course.mainTeacherName
        private String ma; // paper.evaluationDate
        private String ba; // List<Student>.size() (과정ID로 조회)
        private String sa; // 교과목ID로 paper를 조회하는데, isUse가 true걸로, 그 중에 isReEvaluation이 true인것들의 count값
        private String ah; // 해당 사항 없음!!
        private String ja; // 교과목ID로 paper를 조회하는데, isUse가 true걸로, 그 중에 isReEvaluation이 true인것들의 평가일자 0번지하나만!!

        private String sign;

        public No5DTO(List<Exam> exams, List<Exam> reExams, Teacher teacher) {
            Paper paper = exams.stream().map(Exam::getPaper).findFirst().orElse(null);
            Paper rePaper = reExams.stream().map(Exam::getPaper).findFirst().orElse(null);
            this.draftingTeam = "교육운영팀";
            this.docNumber = "부산 " + paper.getEvaluationDate().toString().substring(2);
            this.writingDate = paper.getEvaluationDate().toString();
            this.requestDate = paper.getEvaluationDate().toString();
            this.retentionPeriod = "5년";
            this.author = paper.getSubject().getTeacher().getName();
            this.recipient = "내부결재";
            this.title = paper.getSubject().getCourse().getTitle() + " 평가 실시보고";
            this.ga = paper.getSubject().getCourse().getTitle();
            this.na = paper.getSubject().getCourse().getStartDate() + " ~ " + paper.getSubject().getCourse().getEndDate() + " (" + paper.getSubject().getCourse().getTotalTime() + "시간)";
            this.da = paper.getSubject().getTitle() + " (" + paper.getEvaluationWay().toKorean() + ")";
            this.ra = paper.getSubject().getTeacher().getName();
            this.ma = paper.getEvaluationDate().toString();

            // 중탈안한 총 학생수
            long allStudentCount = paper.getSubject().getCourse().getStudents().stream()
                    .filter(student ->
                            student.getDropOutDate() == null || student.getDropOutDate().isAfter(paper.getEvaluationDate())
                    )
                    .count();
            // 1. 본평가 응시자 수 (시험을 실제로 사용하고, 재시험 아님) - 결석자 제외
            long notTakenCount = exams.stream()
                    .filter(exam -> exam.getResultStatus() == ExamResultStatus.NOT_TAKEN)
                    .count();

            long reTestCount = exams.stream()
                    .filter(exam -> exam.getResultStatus() == ExamResultStatus.FAIL)
                    .count();

            long doExamCount = exams.size() - notTakenCount;

            // 3. 결과 표시 (결석 1명, 재평가대상 1명 이런식으로 표시해야함)
            String temp1 = "";
            if (reTestCount > 0) {
                temp1 += " (미이수 " + reTestCount + "명)";
            }

            String temp2 = "";
            if (notTakenCount > 0) {
                temp2 += " / 미응시 " + notTakenCount + "명";
            }

            this.ba = "재적 " + allStudentCount + "명 / 실시 " + doExamCount + "명" + temp1 + temp2;

            // 재평가 실시 인원
            this.sa = reExams.size() + "명";

            this.ah = "해당사항 없음 (추후수정필요)";
            this.ja = rePaper == null ? "해당사항 없음" : rePaper.getEvaluationDate().toString();
            this.sign = teacher.getSign();
        }
    }

    @Data
    public static class No2McqDTO {
        private String subjectPurpose; // 훈련목표
        private String subjectTitle; // 교과목명
        private List<String> subjectElements; // 세부내용들
        private List<QuestionDTO> questions;

        public No2McqDTO(Subject subject, List<Question> questions) {
            this.subjectPurpose = subject.getPurpose();
            this.subjectTitle = subject.getTitle();
            this.subjectElements = subject.getElements().stream().map(SubjectElement::getTitle).toList();
            this.questions = questions.stream().map(question -> new QuestionDTO(question)).toList();
        }

        @Data
        class QuestionDTO {
            private String questionCriterion; // 평가기준
            private Integer correctPoint; // 점수

            public QuestionDTO(Question question) {
                this.questionCriterion = question.getSubjectElement().getCriterion();
                this.correctPoint = question.getCorrectOption().getPoint();
            }
        }
    }

    @Data
    public static class No2RubricDTO {
        private String subjectPurpose; // 훈련목표
        private String subjectTitle; // 교과목명
        private List<String> subjectElements; // 세부내용들
        private List<QuestionDTO> questions;

        public No2RubricDTO(Subject subject, List<Question> questions) {
            this.subjectPurpose = subject.getPurpose();
            this.subjectTitle = subject.getTitle();
            this.subjectElements = subject.getElements().stream().map(SubjectElement::getTitle).toList();
            this.questions = questions.stream().map(question -> new QuestionDTO(question)).toList();
        }

        @Data
        class QuestionDTO {
            private String criterion; // 평가기준
            private List<RubricDTO> rubrics; // 평가기준에 대한 루브릭들

            public QuestionDTO(Question question) {
                this.criterion = question.getSubjectElement().getCriterion();
                this.rubrics = question.getQuestionOptions().stream()
                        .map(questionOption -> new RubricDTO(questionOption)).toList();
            }

            @Data
            class RubricDTO {
                private String optionContent;
                private Integer optionPoint;

                public RubricDTO(QuestionOption option) {
                    this.optionContent = option.getContent();
                    this.optionPoint = option.getPoint();
                }
            }
        }
    }

    @Data
    public static class CourseDTO {
        private Long courseId;
        private String courseTitle;
        private Integer courseRound;
        private String courseStartDate;

        public CourseDTO(Course course) {
            this.courseId = course.getId();
            this.courseTitle = course.getTitle();
            this.courseRound = course.getRound();
            this.courseStartDate = MyUtil.localDateToString(course.getStartDate());
        }
    }

    @Data
    public static class SubjectDTO {
        private Long subjectId;
        private Integer subjectNo; // 화면에 사용될 번호
        private String subjectTitle;

        public SubjectDTO(Subject subject) {
            this.subjectId = subject.getId();
            this.subjectNo = subject.getNo();
            this.subjectTitle = subject.getTitle();
        }
    }

    @Data
    public static class No1RubricDTO {
        private String courseTitle;
        private Integer courseRound;
        private String courseStartDate;
        private String courseEndDate;
        private String subjectTitle;
        private String subjectEvaluationWay;
        private String subjectTeacherName;
        private String subjectEvaluationDate; // 채점일시 == 평가일시 == 제출기한

        private String evaluationDevice;
        private String evaluationRoom;

        private String rubricScenarioGuideLink;
        private List<String> optionScenarios; // 가이드 요약본
        private List<String> rubricSubmitFormats; // 제출항목 (notion)

        private List<String> scorePolicies;
        private List<String> subjectElements;
        private List<QuestionDTO> questions;
        private String sign;

        // 교과목 조회, 교과목의 본평가 시험지로 출제된 문제 목록 조회
        public No1RubricDTO(Subject subject, List<Question> questions, String sign, Paper paper) {
            this.courseTitle = subject.getCourse().getTitle();
            this.courseRound = subject.getCourse().getRound();
            this.courseStartDate = subject.getCourse().getStartDate().toString();
            this.courseEndDate = MyUtil.localDateToString(subject.getCourse().getEndDate());
            this.subjectTitle = subject.getTitle();
            this.subjectEvaluationWay = paper.getEvaluationWay().toKorean();
            this.subjectTeacherName = subject.getTeacher().getName();
            this.subjectEvaluationDate = MyUtil.localDateToString(paper.getEvaluationDate());

            this.evaluationDevice = paper.getEvaluationDevice();
            this.evaluationRoom = paper.getEvaluationRoom();

            this.rubricScenarioGuideLink = paper.getTaskScenarioGuideLink();
            this.optionScenarios = paper.getQuestions().stream()
                    .map(question -> MyUtil.extractFirstLine(question.getExScenario()))
                    .toList();
            this.rubricSubmitFormats = MyUtil.parseMultilineWithoutHyphen(paper.getTaskSubmitFormat());

            // TODO (결시자, 재평가자야 0.9 프로 Subject에서 받기)
            this.scorePolicies = Arrays.asList("본평가 배점 : 평가점수 X 1.0", "재평가 배점 : 평가점수 X 0.9", "결시자 배점 : 평가점수 X 0.9");

            this.subjectElements = subject.getElements().stream().map(SubjectElement::getTitle).toList();
            this.questions = questions.stream().map(QuestionDTO::new).toList();
            this.sign = sign;
        }

        @Data
        class QuestionDTO {
            private Long questionId;
            private Integer no;
            private String title;
            private Integer point;
            private Integer answerNumber;
            private String questionPurpose;
            private List<OptionDTO> options;

            public QuestionDTO(Question question) {
                this.questionId = question.getId();
                this.no = question.getNo();
                this.title = question.getTitle();
                QuestionOption _option = question.getQuestionOptions().stream()
                        .max(Comparator.comparingInt(QuestionOption::getPoint))
                        .orElse(null);
                this.point = _option.getPoint();
                this.answerNumber = _option.getNo();
                this.questionPurpose = question.getSubjectElement().getCriterion();
                this.options = question.getQuestionOptions().stream().map(OptionDTO::new).toList();
            }

            @Data
            class OptionDTO {
                private Integer no;
                private String content;
                private Boolean isRight;

                public OptionDTO(QuestionOption option) {
                    this.no = option.getNo();
                    this.content = option.getContent();
                    this.isRight = option.getPoint().equals(point);
                }
            }
        }
    }


    @Data
    public static class No1McqDTO {
        private String courseTitle;
        private Integer courseRound;
        private String courseStartDate;
        private String courseEndDate;
        private String subjectTitle;
        private String subjectEvaluationWay;
        private String subjectTeacherName;
        private String subjectEvaluationDate; // 채점일시 == 평가일시 == 제출기한

        private String evaluationDevice;
        private String evaluationRoom;
        private List<String> submissionFormats;
        private List<String> scorePolicies;

        private List<String> subjectElements;
        private List<QuestionDTO> questions;
        private String sign;

        // 교과목 조회, 교과목의 본평가 시험지로 출제된 문제 목록 조회
        public No1McqDTO(Subject subject, List<Question> questions, String sign, Paper paper) {
            this.courseTitle = subject.getCourse().getTitle();
            this.courseRound = subject.getCourse().getRound();
            this.courseStartDate = subject.getCourse().getStartDate().toString();
            this.courseEndDate = MyUtil.localDateToString(subject.getCourse().getEndDate());
            this.subjectTitle = subject.getTitle();
            this.subjectEvaluationWay = paper.getEvaluationWay().toKorean();
            this.subjectTeacherName = subject.getTeacher().getName();
            this.subjectEvaluationDate = MyUtil.localDateToString(paper.getEvaluationDate());

            this.evaluationDevice = paper.getEvaluationDevice();
            this.evaluationRoom = paper.getEvaluationRoom();
            //this.submissionFormats = MyUtil.parseMultilineWithoutHyphen(paper.getSubmissionFormat());
            // TODO (결시자, 재평가자야 0.9 프로 Subject에서 받기)
            double scorePolicy = paper.getSubject().getScorePolicy();
            this.scorePolicies = Arrays.asList("본평가 배점 : 평가점수 X 1.0", "재평가 배점 : 평가점수 X " + scorePolicy, "결시자 배점 : 평가점수 X " + scorePolicy);

            this.subjectElements = subject.getElements().stream().map(subjectElement -> subjectElement.getTitle()).toList();
            this.questions = questions.stream().map(QuestionDTO::new).toList();
            this.sign = sign;
        }

        @Data
        class QuestionDTO {
            private Long questionId;
            private Integer no;
            private String title;
            private Integer point;
            private Integer answerNumber;
            private String questionPurpose;
            private List<OptionDTO> options;

            public QuestionDTO(Question question) {
                this.questionId = question.getId();
                this.no = question.getNo();
                this.title = question.getTitle();
                QuestionOption _option = question.getQuestionOptions().stream()
                        .max(Comparator.comparingInt(QuestionOption::getPoint))
                        .orElse(null);
                this.point = _option.getPoint();
                this.answerNumber = _option.getNo();
                this.questionPurpose = question.getSubjectElement().getCriterion();
                this.options = question.getQuestionOptions().stream().map(OptionDTO::new).toList();
            }

            @Data
            class OptionDTO {
                private Integer no;
                private String content;
                private Boolean isRight;

                public OptionDTO(QuestionOption option) {
                    this.no = option.getNo();
                    this.content = option.getContent();
                    this.isRight = option.getPoint() > 0 ? true : false; // 객관식일때만 의미가 있음
                }
            }
        }
    }

    @Data
    public static class No3RubricDTO {
        private String teacherName;
        private String evaluationDate; // 평가일 (subject)
        private String evaluationRoom; // 평가장소 (임시)
        private String evaluationDevice; // 평가장비 (임시)
        private String subjectTitle; // 교과목 (subject)
        private Integer questionCount;

        private String rubricTitle;
        private String rubricScenario;
        private String rubricScenarioGuideLink;
        private List<String> rubricSubmitFormats; // 제출항목 (notion)
        private String rubricSubmitTemplateLink; // 제출항목 복제 템플릿 (선택)
        private List<String> rubricChallenges; // 도전과제

        private String teacherSign;
        private Integer grade;
        private List<QuestionDTO> questions;

        public No3RubricDTO(Paper paper, List<Question> questions, Teacher teacher) {
            this.teacherName = paper.getSubject().getTeacher().getName();
            this.evaluationDate = paper.getEvaluationDate().toString();
            this.evaluationRoom = paper.getEvaluationRoom();
            this.evaluationDevice = paper.getEvaluationDevice();
            this.subjectTitle = paper.getSubject().getTitle();
            this.questionCount = questions.size();

            this.rubricTitle = paper.getTaskTitle();
            this.rubricScenario = paper.getTaskScenario();
            this.rubricScenarioGuideLink = paper.getTaskScenarioGuideLink();
            this.rubricSubmitFormats = MyUtil.parseMultilineWithoutHyphen(paper.getTaskSubmitFormat());
            this.rubricSubmitTemplateLink = paper.getTaskSubmitTemplateLink();
            this.rubricChallenges = MyUtil.parseMultilineWithoutHyphen(paper.getTaskChallenge());
            this.teacherSign = teacher.getSign();
            this.grade = paper.getSubject().getGrade();
            this.questions = questions.stream().map(QuestionDTO::new).toList();
        }

        @Data
        class QuestionDTO {
            private Long questionId;
            private Integer no;
            private String title;
            private List<String> scenarios; // 가이드 요약본
            private List<OptionDTO> options;

            public QuestionDTO(Question question) {
                this.questionId = question.getId();
                this.no = question.getNo();
                this.title = question.getTitle();
                this.scenarios = MyUtil.parseMultiline(question.getExScenario());
                this.options = question.getQuestionOptions().stream().map(QuestionDTO.OptionDTO::new).toList();
            }

            @Data
            class OptionDTO {
                private Long optionId;
                private Integer no;
                private String rubricItem;
                private Integer point;

                public OptionDTO(QuestionOption option) {
                    this.optionId = option.getId();
                    this.no = option.getNo();
                    this.rubricItem = option.getContent();
                    this.point = option.getPoint();
                }
            }
        }
    }


    @Data
    public static class No3McqDTO {
        private String teacherName;
        private String evaluationDate; // 평가일 (subject)
        private String evaluationRoom; // 평가장소 (임시)
        private String evaluationDevice; // 평가장비 (임시)
        private String subjectTitle; // 교과목 (subject)
        private List<String> subjectElements;
        private Integer questionCount;
        private String teacherSign;
        private Integer grade;
        private List<QuestionDTO> questions;

        public No3McqDTO(Paper paper, List<SubjectElement> subjectElements, List<Question> questions, Teacher teacher) {
            this.teacherName = paper.getSubject().getTeacher().getName();
            this.evaluationDate = paper.getEvaluationDate().toString();
            this.evaluationRoom = paper.getEvaluationRoom();
            this.evaluationDevice = paper.getEvaluationDevice();
            this.subjectTitle = paper.getSubject().getTitle();
            this.subjectElements = subjectElements.stream().map(se -> se.getTitle()).toList();
            this.questionCount = paper.getQuestions().size();
            this.teacherSign = teacher.getSign();
            this.grade = paper.getSubject().getGrade();
            this.questions = questions.stream().map(QuestionDTO::new).toList();
        }

        @Data
        class QuestionDTO {
            private Long questionId;
            private Integer no;
            private String title;
            private String exContent;
            private Integer totalPoint; // 배점
            private List<OptionDTO> options;

            public QuestionDTO(Question question) {
                this.questionId = question.getId();
                this.no = question.getNo();
                this.title = question.getTitle();
                this.exContent = question.getExContent();
                this.totalPoint = question.getQuestionOptions()
                        .stream()
                        .mapToInt(o -> o.getPoint())
                        .max()
                        .orElse(0);
                this.options = question.getQuestionOptions().stream().map(QuestionDTO.OptionDTO::new).toList();
            }

            @Data
            class OptionDTO {
                private Long optionId;
                private Integer no;
                private String content;
                private Integer point;
                private Boolean isRight;

                public OptionDTO(QuestionOption option) {
                    this.optionId = option.getId();
                    this.no = option.getNo();
                    this.content = option.getContent();
                    this.point = option.getPoint();
                    this.isRight = option.getPoint() > 0;
                }
            }
        }
    }

    @Data
    public static class No4McqDTO {
        private Long subjectId;
        private Long courseId;
        private Long examId;
        private Long paperId;
        private String studentName;
        private String teacherName;
        private String evaluationDate; // 평가일 (subject)
        private String evaluationDevice; // 장비 (subject)
        private String evaluationRoom; // 장소 (subject)
        private String subjectTitle; // 교과목 (subject)
        private List<String> subjectElements;
        private List<AnswerDTO> answers;
        private Integer questionCount;
        private String resultStatus;
        private String notTakenReason;
        private Double rawScore;
        private Double maxScore;
        private Double totalScore;
        private Double totalScorePercent;
        private String teacherComment;
        private Integer gradeLevel;
        private String teacherSign;
        private String studentSign;

        public No4McqDTO(Exam exam, List<SubjectElement> subjectElements, Teacher teacher, Integer prevIndex, Integer nextIndex, Integer currentIndex) {
            this.subjectId = exam.getPaper().getSubject().getId();
            this.courseId = exam.getPaper().getSubject().getCourse().getId();
            this.examId = exam.getId();
            this.paperId = exam.getPaper().getId();
            this.studentName = exam.getStudent().getName();
            this.teacherName = exam.getTeacher().getName();
            this.evaluationDate = exam.getPaper().getEvaluationDate().toString();
            this.evaluationDevice = exam.getPaper().getEvaluationDevice();
            this.evaluationRoom = exam.getPaper().getEvaluationRoom();
            this.subjectTitle = exam.getPaper().getSubject().getTitle();
            this.subjectElements = subjectElements.stream().map(se -> se.getTitle()).toList();
            this.answers = exam.getExamAnswers().stream().map(AnswerDTO::new).toList();
            this.questionCount = exam.getPaper().getQuestions().size();

            this.resultStatus = exam.getResultStatus().toKorean();
            this.notTakenReason = exam.getNotTakenReason().toKorean();
            this.rawScore = exam.getRawScore();
            this.maxScore = exam.getCopiedMaxScore();
            this.totalScore = exam.getTotalScore();
            this.totalScorePercent = exam.getTotalScorePercent();
            this.teacherComment = exam.getTeacherComment();
            this.gradeLevel = exam.getGradeLevel();

            this.teacherSign = teacher.getSign();
            this.studentSign = exam.getStudentSign();
        }

        @Data
        class AnswerDTO {
            private Long answerId;
            private Long questionId;
            private Integer no;
            private String title;
            private Integer totalPoint; // 배점
            private Integer answerNumber; // 정답 번호
            private Integer selectedOptionNo; // 학생 선택 번호
            private Double studentPoint;
            private String exContent;
            private List<OptionDTO> options;

            public AnswerDTO(ExamAnswer answer) {
                this.answerId = answer.getId();
                this.questionId = answer.getQuestion().getId();
                this.no = answer.getQuestion().getNo();
                this.title = answer.getQuestion().getTitle();
                QuestionOption _option = answer.getQuestion().getQuestionOptions().stream()
                        .max(Comparator.comparingInt(QuestionOption::getPoint))
                        .orElse(null);
                this.totalPoint = _option.getPoint();
                this.answerNumber = _option.getNo();
                this.selectedOptionNo = answer.getSelectedOptionNo();
                this.studentPoint = answer.getExamResult().getScoredPoint();
                this.exContent = answer.getQuestion().getExContent();
                this.options = answer.getQuestion().getQuestionOptions().stream().map(option -> new AnswerDTO.OptionDTO(option, selectedOptionNo)).toList();
            }

            @Data
            class OptionDTO {
                private Long optionId;
                private Integer no;
                private String content;
                private Boolean isSelect; // 해당 옵션이 선택되었는지 여부

                public OptionDTO(QuestionOption option, Integer selectedOptionNo) {
                    this.optionId = option.getId();
                    this.no = option.getNo();
                    this.content = option.getContent();
                    this.isSelect = no == selectedOptionNo ? true : false;
                }
            }
        }
    }

    @Data
    public static class No4RubricDTO {
        private Long subjectId;
        private Long courseId;
        private Long examId;
        private Long paperId;
        private String studentName;
        private String teacherName;
        private String evaluationDate; // 평가일 (subject)
        private String evaluationRoom; // 평가일 (subject)
        private String evaluationDevice; // 장비 (subject)
        private String subjectTitle; // 교과목 (subject)
        private List<String> subjectElements;
        private List<AnswerDTO> answers;
        private Integer questionCount;

        private String resultStatus;
        private String notTakenReason;
        private Double rawScore;
        private Double maxScore;
        private Double totalScore;
        private Double totalScorePercent;
        private String teacherComment;
        private Integer gradeLevel;

        private String teacherSign;
        private String studentSign;
        private String submitLink;

        public No4RubricDTO(Exam exam, List<SubjectElement> subjectElements, Teacher teacher, Integer prevIndex, Integer nextIndex, Integer currentIndex) {
            this.subjectId = exam.getPaper().getSubject().getId();
            this.courseId = exam.getPaper().getSubject().getCourse().getId();
            this.examId = exam.getId();
            this.paperId = exam.getPaper().getId();
            this.studentName = exam.getStudent().getName();
            this.teacherName = exam.getTeacher().getName();
            this.evaluationDate = exam.getPaper().getEvaluationDate().toString();
            this.evaluationDevice = exam.getPaper().getEvaluationDevice();
            this.evaluationRoom = exam.getPaper().getEvaluationRoom();
            this.subjectTitle = exam.getPaper().getSubject().getTitle();
            this.subjectElements = subjectElements.stream().map(se -> se.getTitle()).toList();
            this.answers = exam.getExamAnswers().stream().map(AnswerDTO::new).toList();
            this.questionCount = exam.getPaper().getQuestions().size();

            this.resultStatus = exam.getResultStatus().toKorean();
            this.notTakenReason = exam.getNotTakenReason().toKorean();
            this.rawScore = exam.getRawScore();
            this.maxScore = exam.getCopiedMaxScore();
            this.totalScore = exam.getTotalScore();
            this.totalScorePercent = exam.getTotalScorePercent();
            this.teacherComment = exam.getTeacherComment();
            this.gradeLevel = exam.getGradeLevel();

            this.teacherSign = teacher.getSign();
            this.studentSign = exam.getStudentSign();
            this.submitLink = exam.getRubricSubmitLink();
        }

        @Data
        class AnswerDTO {
            private Long answerId;
            private Long questionId;
            private Integer no;
            private String title;
            private Integer totalPoint; // 배점
            private Integer selectedOptionNo; // 학생 선택 번호
            private Double studentPoint;
            private String codeReviewLink;
            private String codeReviewPRLink;
            private List<String> scenarios;
            private List<OptionDTO> options;

            public AnswerDTO(ExamAnswer answer) {
                this.answerId = answer.getId();
                this.questionId = answer.getQuestion().getId();
                this.no = answer.getQuestion().getNo();
                this.title = answer.getQuestion().getTitle();
                QuestionOption _option = answer.getQuestion().getQuestionOptions().stream()
                        .max(Comparator.comparingInt(QuestionOption::getPoint))
                        .orElse(null);
                this.totalPoint = _option.getPoint();
                this.selectedOptionNo = answer.getSelectedOptionNo(); // 기본적으로 1번에 체크되게 하기
                this.studentPoint = answer.getExamResult().getScoredPoint();
                this.codeReviewLink = answer.getCodeReviewRequestLink();
                this.codeReviewPRLink = answer.getExamResult().getCodeReviewFeedbackPRLink();
                this.scenarios = MyUtil.parseMultilineWithoutHyphen(answer.getQuestion().getExScenario());
                this.options = answer.getQuestion().getQuestionOptions().stream().map(option -> new AnswerDTO.OptionDTO(option, selectedOptionNo)).toList();
            }

            @Data
            class OptionDTO {
                private Long optionId;
                private Integer no;
                private String content;
                private Boolean isSelect; // 해당 옵션이 선택되었는지 여부
                private Integer point;

                public OptionDTO(QuestionOption option, Integer selectedOptionNo) {
                    this.optionId = option.getId();
                    this.no = option.getNo();
                    this.content = option.getContent();
                    this.isSelect = no.equals(selectedOptionNo);
                    this.point = option.getPoint();
                }
            }
        }
    }

}
