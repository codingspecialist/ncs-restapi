package shop.mtcoding.blog.exam.web.dto;

import lombok.Data;
import shop.mtcoding.blog.course.application.domain.SubjectElement;
import shop.mtcoding.blog.exam.application.domain.Exam;
import shop.mtcoding.blog.exam.application.domain.ExamAnswer;
import shop.mtcoding.blog.exam.application.domain.ExamResult;
import shop.mtcoding.blog.exam.application.domain.QuestionOption;
import shop.mtcoding.blog.user.application.domain.Teacher;

import java.util.List;

public class ExamTeacherResponse {


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
            this.studentName = exam.getCourseStudent().getStudent().getName();
            this.teacherName = exam.getCourseTeacher().getTeacher().getName();
            this.evaluationDate = exam.getPaper().getEvaluationDate().toString();
            this.evaluationDevice = exam.getPaper().getEvaluationDevice();
            this.evaluationRoom = exam.getPaper().getEvaluationRoom();

            //this.subjectTitle = exam.getPaper().getSubject().getTitle();
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
            this.studentName = exam.getCourseStudent().getStudent().getName();
            this.teacherName = exam.getCourseTeacher().getTeacher().getName();
            this.evaluationDate = exam.getPaper().getEvaluationDate().toString();
            this.evaluationDevice = exam.getPaper().getEvaluationDevice();
            this.evaluationRoom = exam.getPaper().getEvaluationRoom();

            //this.subjectTitle = exam.getPaper().getSubject().getTitle();
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
