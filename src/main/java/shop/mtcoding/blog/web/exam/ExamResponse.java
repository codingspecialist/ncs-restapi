package shop.mtcoding.blog.web.exam;

import lombok.Data;
import org.springframework.data.domain.Page;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.course.exam.Exam;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.domain.course.exam.result.ExamResult;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.QuestionOption;
import shop.mtcoding.blog.domain.user.teacher.Teacher;

import java.time.LocalDate;
import java.util.List;

public class ExamResponse {

    @Data
    public static class CourseListDTO {
        private Integer totalPage; // 전체 페이지 수
        private Integer pageSize; // 페이지 별 아이템 개수
        private Integer pageNumber; // 현재 페이지 번호
        private Boolean isFirst; // 첫번째 페이지 여부
        private Boolean isLast; // 마지막 페이지 여부
        private List<DTO> courses;

        public CourseListDTO(Page<Course> paging) {
            this.totalPage = paging.getTotalPages();
            this.pageSize = paging.getSize();
            this.pageNumber = paging.getNumber();
            this.isFirst = paging.isFirst();
            this.isLast = paging.isLast();
            this.courses = paging.getContent().stream().map(DTO::new).toList();
        }

        @Data
        class DTO {
            private Long id;
            private String title;
            private String code;
            private Integer totalTime;
            private Integer totalDay;
            private Integer round;
            private LocalDate startDate;
            private LocalDate endDate;
            private String teacherName;
            private String courseStatus;

            public DTO(Course course) {
                this.id = course.getId();
                this.title = course.getTitle();
                this.code = course.getCode();
                this.totalTime = course.getTotalTime();
                this.totalDay = course.getTotalDay();
                this.round = course.getRound();
                this.startDate = course.getStartDate();
                this.endDate = course.getEndDate();
                this.teacherName = course.getMainTeacherName();
                this.courseStatus = course.getCourseStatus().toKorean();
            }
        }
    }

    @Data
    public static class SubjectDTO {
        private Long subjectId;
        private Integer no; // 과정에서 몇번째로 시작하는 교과목인지에 대한 연번
        private String code; // 능력단위 코드
        private String title;
        private String purpose;
        private String ncsType;
        private Integer grade;
        private Integer totalTime;
        private String learningWay; // 학습 방법
        private String evaluationWay; // 평가 방법
        private String evaluationDate; // 평가일
        private String revaluationDate; // 재평가일
        private LocalDate startDate; // 교과목 시작 날짜
        private LocalDate endDate; // 교과목 종료 날짜
        private Long courseId; // 과정 PK
        private String courseTitle;
        private Integer courseRound;

        public SubjectDTO(Subject subject) {
            Paper paper = subject.getPapers().stream().filter(p -> !p.isReTest()).findFirst().orElse(null);
            Paper rePaper = subject.getPapers().stream().filter(p -> p.isReTest()).findFirst().orElse(null);

            this.subjectId = subject.getId();
            this.code = subject.getCode();
            this.title = subject.getTitle();
            this.purpose = subject.getPurpose();
            this.ncsType = subject.getNcsType().toKorean();
            this.grade = subject.getGradeLevel();
            this.totalTime = subject.getTotalTime();
            this.no = subject.getNo();
            this.learningWay = subject.getLearningWay().toKorean();
            if (paper != null) {
                this.evaluationWay = paper.getEvaluationWay() != null ? paper.getEvaluationWay().toKorean() : "시험지없음"; // ✅ enum 변경
                this.evaluationDate = paper.getEvaluationDate() != null ? paper.getEvaluationDate().toString() : "시험지없음";
            } else {
                this.evaluationWay = "시험지없음";
                this.evaluationDate = "시험지없음";
            }

            this.revaluationDate = (rePaper != null && rePaper.getEvaluationDate() != null)
                    ? rePaper.getEvaluationDate().toString()
                    : "시험지없음";

            this.startDate = subject.getStartDate();
            this.endDate = subject.getEndDate();
            this.courseId = subject.getCourse().getId();
            this.courseTitle = subject.getCourse().getTitle();
            this.courseRound = subject.getCourse().getRound();
        }
    }

    @Data
    public static class ResultMcqDetails {
        private Long selectedExamId;
        private List<ResultMcqDetail> details;

        public ResultMcqDetails(Long examId, List<Exam> exams, List<SubjectElement> subjectElements, Teacher teacher) {
            this.selectedExamId = examId;
            this.details = exams.stream()
                    .map(exam -> new ResultMcqDetail(exam, subjectElements, teacher))
                    .toList();
        }
    }

    @Data
    public static class ResultRubricDetails {
        private Long selectedExamId;
        private List<ResultRubricDetail> details;

        public ResultRubricDetails(Long examId, List<Exam> exams, List<SubjectElement> subjectElements, Teacher teacher) {
            this.selectedExamId = examId;
            this.details = exams.stream()
                    .map(exam -> new ResultRubricDetail(exam, subjectElements, teacher))
                    .toList();
        }
    }

    @Data
    public static class ResultMcqDetail {
        private Long examId;
        private Long paperId;
        private String studentName;
        private String teacherName;
        private String evaluationDate;
        private String evaluationDevice;
        private String evaluationRoom;

        private String subjectTitle; // 교과목 (subject)
        private List<String> subjectElements;
        private List<ResultMcq> results;

        private Integer questionCount;
        private String examResultStatus;
        private String notTakenReason;
        private Double rawScore;
        private Double totalScore;
        private Double totalScorePercent;
        private String teacherComment;
        private Integer gradeLevel;
        private String teacherSign;
        private String studentSign;

        public ResultMcqDetail(Exam exam, List<SubjectElement> subjectElements, Teacher teacher) {
            this.examId = exam.getId();
            this.paperId = exam.getPaper().getId();
            this.studentName = exam.getStudent().getName();
            this.teacherName = exam.getTeacher().getName();
            this.evaluationDate = exam.getPaper().getEvaluationDate().toString();
            this.evaluationDevice = exam.getPaper().getEvaluationDevice();
            this.evaluationRoom = exam.getPaper().getEvaluationRoom();

            this.subjectTitle = exam.getPaper().getSubject().getTitle();
            this.subjectElements = subjectElements.stream()
                    .map(SubjectElement::getTitle)
                    .toList();
            this.results = exam.getExamAnswers().stream()
                    .map(ResultMcq::new)
                    .toList();

            this.questionCount = exam.getPaper().getQuestions().size();
            this.examResultStatus = exam.getResultStatus().toKorean(); // 정확한 이름 매핑
            this.notTakenReason = exam.getNotTakenReason().toKorean();
            this.rawScore = exam.getRawScore();
            this.totalScore = exam.getTotalScore();
            this.totalScorePercent = exam.getTotalScorePercent();
            this.teacherComment = exam.getTeacherComment();
            this.gradeLevel = exam.getGradeLevel();
            this.teacherSign = teacher.getSign();
            this.studentSign = exam.getStudentSign();
        }


        @Data
        class ResultMcq {
            private Long answerId;
            private Long questionId;
            private Integer questionNo;
            private String questionTitle;
            private String questionSummary;
            private Double maxScore;
            private Integer correctOptionNo;
            private Integer selectedOptionNo;
            private Double scoredPoint;
            private List<Option> options;

            public ResultMcq(ExamAnswer answer) {
                this.answerId = answer.getId();
                this.questionId = answer.getQuestion().getId();
                this.questionNo = answer.getQuestionNo(); // answer 기준
                this.questionTitle = answer.getQuestion().getTitle();
                this.questionSummary = answer.getQuestion().getSummary();

                // 가장 높은 점수를 가진 옵션
                QuestionOption correctOption = answer.getQuestion().getCorrectOption();

                this.maxScore = correctOption != null ? correctOption.getPoint().doubleValue() : 0.0;
                this.correctOptionNo = correctOption != null ? correctOption.getNo() : null;
                this.selectedOptionNo = answer.getSelectedOptionNo();

                // 💡 채점 결과는 ExamResult에서 가져옴
                ExamResult result = answer.getExamResult();
                this.scoredPoint = result != null ? result.getScoredPoint() : null;

                this.options = answer.getQuestion().getQuestionOptions().stream()
                        .map(option -> new Option(option, selectedOptionNo))
                        .toList();
            }

            @Data
            class Option {
                private Long optionId;
                private Integer optionNo;
                private String optionContent;
                private Boolean isSelected;

                public Option(QuestionOption option, Integer selectedOptionNo) {
                    this.optionId = option.getId();
                    this.optionNo = option.getNo();
                    this.optionContent = option.getContent();
                    this.isSelected = optionNo.equals(selectedOptionNo);
                }
            }
        }
    }

    @Data
    public static class ResultRubricDetail {
        private Long examId;
        private Long paperId;
        private String studentName;
        private String teacherName;
        private String evaluationDate;
        private String evaluationDevice;
        private String evaluationRoom;

        private String subjectTitle; // 교과목 (subject)
        private List<String> subjectElements;
        private List<ResultRubric> results;

        private Integer questionCount;
        private String examResultStatus; // 통과, 미통과(60점미만), 미응시, 채점전
        private String notTakenReason;
        private Double rawScore;
        private Double totalScore;
        private Double totalScorePercent;
        private String teacherComment;
        private Integer gradeLevel;
        private String teacherSign;
        private String studentSign;

        // 루브릭만 가지는 것
        private String rubricSubmitLink;

        public ResultRubricDetail(Exam exam, List<SubjectElement> subjectElements, Teacher teacher) {
            this.examId = exam.getId();
            this.paperId = exam.getPaper().getId();
            this.studentName = exam.getStudent().getName();
            this.teacherName = exam.getTeacher().getName();
            this.evaluationDate = exam.getPaper().getEvaluationDate().toString();
            this.evaluationDevice = exam.getPaper().getEvaluationDevice();
            this.evaluationRoom = exam.getPaper().getEvaluationRoom();

            this.subjectTitle = exam.getPaper().getSubject().getTitle();
            this.subjectElements = subjectElements.stream()
                    .map(SubjectElement::getTitle)
                    .toList();
            this.results = exam.getExamAnswers().stream()
                    .map(ResultRubric::new)
                    .toList();

            this.questionCount = exam.getPaper().getQuestions().size();
            this.examResultStatus = exam.getResultStatus().toKorean(); // 정확한 이름 매핑
            this.notTakenReason = exam.getNotTakenReason().toKorean();
            this.rawScore = exam.getRawScore();
            this.totalScore = exam.getTotalScore();
            this.totalScorePercent = exam.getTotalScorePercent();
            this.teacherComment = exam.getTeacherComment();
            this.gradeLevel = exam.getGradeLevel();
            this.teacherSign = teacher.getSign();
            this.studentSign = exam.getStudentSign();
            this.rubricSubmitLink = exam.getRubricSubmitLink();
        }


        @Data
        class ResultRubric {
            private Long answerId;
            private Long questionId;
            private Integer questionNo;
            private String questionTitle;
            private String questionSummary;
            private Double maxScore;
            private Integer selectedOptionNo;
            private Double scoredPoint;
            private List<Option> options;


            private String codeReviewRequestLink;
            private String codeReviewFeedbackPrLink;
            private List<String> exScenarios;

            public ResultRubric(ExamAnswer answer) {
                this.answerId = answer.getId();
                this.questionId = answer.getQuestion().getId();
                this.questionNo = answer.getQuestionNo(); // answer 기준
                this.questionTitle = answer.getQuestion().getTitle();
                this.questionSummary = answer.getQuestion().getSummary();

                // 가장 높은 점수를 가진 옵션이 정답
                QuestionOption correctOption = answer.getQuestion().getCorrectOption();

                this.maxScore = correctOption != null ? correctOption.getPoint().doubleValue() : 0.0;
                this.selectedOptionNo = answer.getSelectedOptionNo();

                // 💡 채점 결과는 ExamResult에서 가져옴
                ExamResult result = answer.getExamResult();
                this.scoredPoint = result != null ? result.getScoredPoint() : null;

                this.options = answer.getQuestion().getQuestionOptions().stream()
                        .map(option -> new Option(option, selectedOptionNo))
                        .toList();


                this.codeReviewRequestLink = answer.getCodeReviewRequestLink();
                this.codeReviewFeedbackPrLink = result.getCodeReviewFeedbackPrLink();
                this.options = answer.getQuestion().getQuestionOptions().stream().map(option -> new Option(option, selectedOptionNo)).toList();
            }

            @Data
            class Option {
                private Long optionId;
                private Integer optionNo;
                private String optionContent;
                private Boolean isSelected;
                private Integer rubricPoint;

                public Option(QuestionOption option, Integer selectedOptionNo) {
                    this.optionId = option.getId();
                    this.optionNo = option.getNo();
                    this.optionContent = option.getContent();
                    this.isSelected = option.getNo().equals(selectedOptionNo);
                    this.rubricPoint = option.getPoint();
                }
            }
        }
    }


}
