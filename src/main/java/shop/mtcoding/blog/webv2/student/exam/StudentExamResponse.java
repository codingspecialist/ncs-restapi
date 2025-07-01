package shop.mtcoding.blog.webv2.student.exam;

import lombok.Data;
import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog.domainv2222222.course.exam.Exam;
import shop.mtcoding.blog.domainv2222222.course.exam.ExamModel;
import shop.mtcoding.blog.domainv2222222.course.exam.ExamTakingStatus;
import shop.mtcoding.blog.domainv2222222.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.domainv2222222.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domainv2222222.course.subject.paper.Paper;
import shop.mtcoding.blog.domainv2222222.course.subject.paper.question.Question;
import shop.mtcoding.blog.domainv2222222.course.subject.paper.question.QuestionOption;
import shop.mtcoding.blog.domainv2222222.user.teacher.Teacher;

import java.util.Comparator;
import java.util.List;

public class StudentExamResponse {

    @Data
    public static class MyPaperItems {

        private Long studentId;
        private List<PaperInfo> papers; // 내부 클래스 타입으로 변경

        /**
         * 서비스 계층에서 계산된 PaperItem 리스트(Paper+Status)를 받아
         * 최종 응답 DTO 리스트로 변환합니다.
         */
        public MyPaperItems(Long studentId, List<ExamModel.PaperItem> paperItems) {
            this.studentId = studentId;
            this.papers = paperItems.stream()
                    .map(item -> new PaperInfo(item.paper(), item.status())) // paper와 status를 함께 전달
                    .toList();
        }

        /**
         * 실제 API 응답에 포함될 개별 시험지 정보 DTO
         */
        @Data
        class PaperInfo {
            // 기존 필드
            private Long paperId;
            private String courseTitle;
            private Integer courseRound;
            private Long subjectId;
            private String subjectTitle;
            private Integer questionCount;
            private String paperVersion;
            private String teacherName;

            // 새로 추가된 필드
            private String status; // 학생의 응시 상태 (ex: "응시 가능", "응시 완료")

            public PaperInfo(Paper paper, ExamTakingStatus status) {
                // 기존 매핑
                this.paperId = paper.getId();
                this.courseTitle = paper.getSubject().getCourse().getTitle();
                this.courseRound = paper.getSubject().getCourse().getRound();
                this.subjectId = paper.getSubject().getId();
                this.subjectTitle = paper.getSubject().getTitle();
                this.questionCount = paper.getQuestions().size();
                this.paperVersion = paper.getPaperVersion().toKorean();
                this.teacherName = paper.getSubject().getTeacher().getName();

                // Enum으로 받은 상태를 클라이언트가 보기 좋은 한글 문자열로 변환
                this.status = switch (status) {
                    case TAKEN -> "응시 완료";
                    case AVAILABLE -> "응시 가능";
                    case NOT_AVAILABLE -> "응시 불가";
                };
            }
        }
    }

    @Data
    public static class McqStartDTO {
        private Long paperId;
        private String studentName;
        private String teacherName;
        private String evaluationDate; // 평가일 (subject)
        private String evaluationDevice; // 평가장비 (임시)
        private String evaluationRoom; // 평가장소 (임시)
        private String subjectTitle; // 교과목 (subject)
        private List<String> subjectElements;
        private List<QuestionItem> questions;
        private Integer questionCount;

        public McqStartDTO(Paper paper, String studentName, List<SubjectElement> subjectElements, List<Question> questions) {
            this.paperId = paper.getId();
            this.studentName = studentName;
            this.teacherName = paper.getSubject().getTeacher().getName();
            this.evaluationDate = paper.getEvaluationDate().toString();
            this.evaluationDevice = paper.getEvaluationDevice();
            this.evaluationRoom = paper.getEvaluationRoom();
            this.subjectTitle = paper.getSubject().getTitle();
            this.subjectElements = subjectElements.stream().map(se -> se.getTitle()).toList();
            this.questions = questions.stream().map(QuestionItem::new).toList();
            this.questionCount = questions.size();
        }

        @Data
        class QuestionItem {
            private Long questionId;
            private Integer questionNo;
            private String questionTitle;
            private Integer collectOptionScore;
            private String questionSummary;
            private List<Option> options;

            public QuestionItem(Question question) {
                this.questionId = question.getId();
                this.questionNo = question.getNo();
                this.questionTitle = question.getTitle();
                this.collectOptionScore = question.getCorrectOption().getPoint();
                this.questionSummary = question.getSummary();
                this.options = question.getQuestionOptions().stream().map(Option::new).toList();
            }

            @Data
            class Option {
                private Long optionId;
                private Integer optionNo;
                private String optionContent;

                public Option(QuestionOption option) {
                    this.optionId = option.getId();
                    this.optionNo = option.getNo();
                    this.optionContent = option.getContent();
                }
            }
        }
    }

    @Data
    public static class RubricStartDTO {
        private Long paperId;
        private String studentName;
        private String teacherName;
        private String evaluationDate; // 평가일 (subject)
        private String evaluationDevice; // 평가장비 (임시)
        private String evaluationRoom; // 평가장소 (임시)
        private String subjectTitle; // 교과목 (subject)
        private List<String> subjectElements;
        private List<QuestionDTO> questions;
        private Integer questionCount;

        // -------------------- 객관식이 아닐때 받아야 할 목록
        private String pblTitle;
        private String pblScenario;
        private String pblScenarioGuideLink;
        private List<String> pblSubmitFormats; // 제출항목 (notion)
        private String pblSubmitTemplateLink; // 제출항목 복제 템플릿 (선택)
        private List<String> pblChallenges; // 도전과제


        public RubricStartDTO(Paper paper, String studentName, List<SubjectElement> subjectElements, List<Question> questions) {
            this.paperId = paper.getId();
            this.studentName = studentName;
            this.teacherName = paper.getSubject().getTeacher().getName();
            this.evaluationDate = paper.getEvaluationDate().toString();
            this.evaluationDevice = paper.getEvaluationDevice();
            this.evaluationRoom = paper.getEvaluationRoom();
            this.subjectTitle = paper.getSubject().getTitle();
            this.subjectElements = subjectElements.stream().map(se -> se.getTitle()).toList();
            this.questions = questions.stream().map(QuestionDTO::new).toList();
            this.questionCount = questions.size();
            this.pblTitle = paper.getTaskTitle();
            this.pblScenario = paper.getTaskScenario();
            this.pblScenarioGuideLink = paper.getTaskScenarioGuideLink();
            this.pblSubmitFormats = MyUtil.parseMultilineWithoutHyphen(paper.getTaskSubmitFormat());
            this.pblSubmitTemplateLink = paper.getTaskSubmitTemplateLink();
            this.pblChallenges = MyUtil.parseMultilineWithoutHyphen(paper.getTaskChallenge());
        }

        @Data
        class QuestionDTO {
            private Long questionId;
            private Integer no;
            private String title;
            private Integer totalPoint; // 이 문제의 총점
            private List<String> summaries; // 가이드 요약본
            private List<OptionDTO> options;

            public QuestionDTO(Question question) {
                this.questionId = question.getId();
                this.no = question.getNo();
                this.title = question.getTitle();
                this.totalPoint = question.getQuestionOptions().stream().mapToInt(QuestionOption::getPoint).max().getAsInt();
                this.summaries = MyUtil.parseMultiline(question.getSummary());
                this.options = question.getQuestionOptions().stream().map(OptionDTO::new).toList();
            }

            @Data
            class OptionDTO {
                private Long optionId;
                private Integer no;
                private String content;
                private Integer point; // 각 루브릭의 점수

                public OptionDTO(QuestionOption option) {
                    this.optionId = option.getId();
                    this.no = option.getNo();
                    this.content = option.getContent();
                    this.point = option.getPoint();
                }
            }
        }
    }


    @Data
    public static class ResultDTO {
        // 교과목 번호, 교과목 내 시험 순서
        private Long examId;
        private Long paperId; // 몇번째 시험지에 몇번 학생? (1,2)
        private Integer subjectNo;
        private String courseNameAndRound;
        private String subjectTitle;

        private String studentName;
        private String teacherName;

        private String resultStatus;
        private String notTakenReason;
        private Double rawScore;
        private Double maxScore;
        private Double totalScore;
        private Double totalScorePercent;
        private Integer gradeLevel;
        private Boolean isActive;

        private Long studentId;

        public ResultDTO(Exam exam) {
            this.studentId = exam.getStudent().getId();
            this.examId = exam.getId();
            this.paperId = exam.getPaper().getId();
            this.subjectNo = exam.getPaper().getSubject().getNo();
            this.courseNameAndRound = exam.getStudent().getCourse().getTitle() + "/" + exam.getStudent().getCourse().getRound() + "회차";
            this.subjectTitle = exam.getPaper().getSubject().getTitle();

            this.studentName = exam.getStudent().getName();
            this.teacherName = exam.getTeacher().getName();

            this.resultStatus = exam.getResultStatus().toKorean();
            this.notTakenReason = exam.getNotTakenReason().toKorean();
            this.rawScore = exam.getRawScore();
            this.maxScore = exam.getCopiedMaxScore();
            this.totalScore = exam.getTotalScore();
            this.totalScorePercent = exam.getTotalScorePercent();
            this.gradeLevel = exam.getGradeLevel();
            this.isActive = exam.getIsActive();

        }
    }


    @Data
    public static class RubricResultDetailDTO {
        private Long examId;
        private Long paperId;
        private String studentName;
        private String teacherName;
        private String evaluationDate; // 평가일 (subject)
        private String evaluationRoom;
        private String evaluationDevice;
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
        private Integer gradeLevel;
        private Boolean isActive;


        private String submitLink;
        private String teacherComment;
        private String teacherSign;
        private String studentSign;

        public RubricResultDetailDTO(Exam exam, List<SubjectElement> subjectElements, Teacher teacher) {
            this.examId = exam.getId();
            this.paperId = exam.getPaper().getId();
            this.studentName = exam.getStudent().getName();
            this.teacherName = exam.getTeacher().getName();
            this.evaluationDate = exam.getPaper().getEvaluationDate().toString();
            this.evaluationRoom = exam.getPaper().getEvaluationRoom();
            this.evaluationDevice = exam.getPaper().getEvaluationDevice();
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
            this.gradeLevel = exam.getGradeLevel();
            this.isActive = exam.getIsActive();


            this.submitLink = exam.getRubricSubmitLink();
            this.teacherComment = exam.getTeacherComment();
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
            private Integer selectedOptionNo; // 학생 선택 번호
            private Double studentPoint;
            private String codeReviewRequestLink;
            private String codeReviewFeedbackPrLink;
            private List<String> summaries;
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
                this.codeReviewRequestLink = answer.getCodeReviewRequestLink();
                this.codeReviewFeedbackPrLink = answer.getExamResult().getCodeReviewFeedbackPrLink();
                this.summaries = MyUtil.parseMultilineWithoutHyphen(answer.getQuestion().getSummary());
                this.selectedOptionNo = answer.getSelectedOptionNo();
                this.studentPoint = answer.getExamResult().getScoredPoint();
                this.options = answer.getQuestion().getQuestionOptions().stream().map(option -> new OptionDTO(option, selectedOptionNo)).toList();
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


    // 학생이 보는거니까, 연번, 이전, 다음, 미이수 결과 보는 번호 같은거 다 지우자.
    @Data
    public static class McqResultDetailDTO {
        private Long examId;
        private Long paperId;
        private String studentName;
        private String teacherName;
        private String evaluationDate; // 평가일 (subject)
        private String evaluationRoom;
        private String evaluationDevice;
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
        private Integer gradeLevel;
        private Boolean isActive;


        private String teacherComment;
        private String teacherSign;
        private String studentSign;

        public McqResultDetailDTO(Exam exam, List<SubjectElement> subjectElements, Teacher teacher) {
            this.examId = exam.getId();
            this.paperId = exam.getPaper().getId();
            this.studentName = exam.getStudent().getName();
            this.teacherName = exam.getTeacher().getName();
            this.evaluationDate = exam.getPaper().getEvaluationDate().toString();
            this.evaluationRoom = exam.getPaper().getEvaluationRoom();
            this.evaluationDevice = exam.getPaper().getEvaluationDevice();
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
            this.gradeLevel = exam.getGradeLevel();
            this.isActive = exam.getIsActive();


            this.teacherComment = exam.getTeacherComment();
            this.teacherSign = teacher.getSign();
            this.studentSign = exam.getStudentSign();
        }

        @Data
        class AnswerDTO {
            private Long answerId;
            private Long questionId;
            private Integer no;
            private String title;
            private Integer totalPoint;
            private Integer answerNumber; // 정답 번호
            private Integer selectedOptionNo; // 학생 선택 번호
            private Double studentPoint;
            private String questionSummary;
            private List<OptionDTO> options;

            public AnswerDTO(ExamAnswer answer) {
                this.answerId = answer.getId();
                this.questionId = answer.getQuestion().getId();
                this.no = answer.getQuestion().getNo();
                this.title = answer.getQuestion().getTitle();

                this.questionSummary = answer.getQuestion().getSummary();
                QuestionOption _option = answer.getQuestion().getQuestionOptions().stream()
                        .max(Comparator.comparingInt(QuestionOption::getPoint))
                        .orElse(null);
                this.totalPoint = _option.getPoint(); // 배점
                this.answerNumber = _option.getNo();
                this.selectedOptionNo = answer.getSelectedOptionNo();
                this.studentPoint = answer.getExamResult().getScoredPoint();
                this.options = answer.getQuestion().getQuestionOptions().stream().map(option -> new OptionDTO(option, selectedOptionNo)).toList();
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

}
