package shop.mtcoding.blog.web.exam;

import lombok.Data;
import org.springframework.data.domain.Page;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.course.exam.Exam;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.option.QuestionOption;
import shop.mtcoding.blog.domain.user.teacher.Teacher;

import java.time.LocalDate;
import java.util.Comparator;
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
            Paper paper = subject.getPapers().stream().filter(p -> !p.isReEvaluation()).findFirst().orElse(null);
            Paper rePaper = subject.getPapers().stream().filter(p -> p.isReEvaluation()).findFirst().orElse(null);

            this.subjectId = subject.getId();
            this.code = subject.getCode();
            this.title = subject.getTitle();
            this.purpose = subject.getPurpose();
            this.ncsType = subject.getNcsType().toKorean();
            this.grade = subject.getGrade();
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
    public static class ResultDetailDTO {
        private Long examId;
        private Long paperId;
        private String studentName;
        private String teacherName;
        private String evaluationDate; // 평가일 (subject)
        private String loc; // 평가장소 (임시)
        private String subjectTitle; // 교과목 (subject)
        private List<String> subjectElements;
        private List<AnswerDTO> answers;
        private Integer questionCount;
        private String examState;
        private String reExamReason;
        private String examPassState;
        private Double score;
        private String teacherComment;
        private Integer grade;
        private String teacherSign;
        private String studentSign;
        private Boolean isStudentSign;
        private Boolean isAbsent;
        private Integer studentNo;
        private Long prevExamId; // 해당 교과목에 이전 학생 id
        private Long nextExamId; // 해당 교과목에 다음 학생 id
        private Long originExamId;

        public ResultDetailDTO(Exam exam, List<SubjectElement> subjectElements, Teacher teacher, Long prevExamId, Long nextExamId, Integer currentIndex, Long originExamId) {
            this.examId = exam.getId();
            this.paperId = exam.getPaper().getId();
            this.studentName = exam.getStudent().getName();
            this.teacherName = exam.getTeacherName();
            this.evaluationDate = exam.getPaper().getEvaluationDate().toString();
            this.loc = "3호";
            this.subjectTitle = exam.getPaper().getSubject().getTitle();
            this.subjectElements = subjectElements.stream().map(se -> se.getSubtitle()).toList();
            this.answers = exam.getExamAnswers().stream().map(AnswerDTO::new).toList();
            this.questionCount = exam.getPaper().getQuestionCount();
            this.examState = exam.getExamState();
            this.reExamReason = exam.getReExamReason() == null ? "" : exam.getReExamReason();
            this.examPassState = exam.getPassState();
            this.score = exam.getScore();
            this.teacherComment = exam.getTeacherComment();
            this.grade = exam.getGrade();
            this.teacherSign = teacher.getSign();
            this.studentSign = exam.getStudentSign();
            this.isStudentSign = exam.getStudentSign() == null ? false : true;
            this.studentNo = currentIndex == null ? null : currentIndex + 1;
            this.prevExamId = prevExamId;
            this.nextExamId = nextExamId;
            this.isAbsent = exam.getReExamReason().equals("결석");
            this.originExamId = originExamId;
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
            private Integer studentPoint;
            private List<OptionDTO> options;

            public AnswerDTO(ExamAnswer answer) {
                this.answerId = answer.getId();
                this.questionId = answer.getQuestion().getId();
                this.no = answer.getQuestion().getNo();
                this.title = answer.getQuestion().getTitle();

                // 객관식일때는, isRight인것의 점수를 가져오면 되는데, 객관식이 아닐때는, 정답이 여러개일수 있기 때문에 가장 높은점수를 가져와야 해서 아래 코드 필수임
                QuestionOption _option = answer.getQuestion().getQuestionOptions().stream()
                        .max(Comparator.comparingInt(QuestionOption::getPoint))
                        .orElse(null);

                this.totalPoint = _option.getPoint();
                this.answerNumber = _option.getNo();
                this.selectedOptionNo = answer.getSelectedOptionNo();
                this.studentPoint = answer.getEarnedPoint();
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
                    this.isSelect = no.equals(selectedOptionNo);
                }
            }
        }
    }

}
