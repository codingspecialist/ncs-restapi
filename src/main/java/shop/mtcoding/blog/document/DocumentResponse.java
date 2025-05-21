package shop.mtcoding.blog.document;

import lombok.Data;
import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog.course.Course;
import shop.mtcoding.blog.course.exam.Exam;
import shop.mtcoding.blog.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.course.subject.Subject;
import shop.mtcoding.blog.course.subject.element.SubjectElement;
import shop.mtcoding.blog.paper.Paper;
import shop.mtcoding.blog.paper.question.Question;
import shop.mtcoding.blog.paper.question.option.QuestionOption;
import shop.mtcoding.blog.user.User;
import shop.mtcoding.blog.user.teacher.Teacher;

import java.util.Arrays;
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

        public No5DTO(List<Exam> exams, List<Exam> reExams, User teacher) {
            Paper paper = exams.stream().map(Exam::getPaper).findFirst().orElse(null);
            Paper rePaper = reExams.stream().map(Exam::getPaper).findFirst().orElse(null);
            this.draftingTeam = "교육운영팀";
            this.docNumber = "부산 " + paper.getEvaluationDate().toString().substring(2);
            this.writingDate = paper.getEvaluationDate().toString();
            this.requestDate = paper.getEvaluationDate().toString();
            this.retentionPeriod = "5년";
            this.author = paper.getSubject().getTeacherName();
            this.recipient = "내부결재";
            this.title = paper.getSubject().getCourse().getTitle() + " 평가 실시보고";
            this.ga = paper.getSubject().getCourse().getTitle();
            this.na = paper.getSubject().getCourse().getStartDate() + " ~ " + paper.getSubject().getCourse().getEndDate() + " (" + paper.getSubject().getCourse().getTotalTime() + "시간)";
            this.da = paper.getSubject().getTitle() + " (" + paper.getEvaluationWay() + ")";
            this.ra = paper.getSubject().getTeacherName();
            this.ma = paper.getEvaluationDate().toString();

            // 중탈안한 총 학생수
            long allStudentCount = paper.getSubject().getCourse().getStudents().stream()
                    .filter(student ->
                            student.getDropOutDate() == null || student.getDropOutDate().isAfter(paper.getEvaluationDate())
                    )
                    .count();
            // 1. 본평가 응시자 수 (시험을 실제로 사용하고, 재시험 아님) - 결석자 제외
            long absentCount = exams.stream()
                    .filter(exam -> "결석".equals(exam.getReExamReason()))
                    .count();

            long reExamExpectCount = exams.stream()
                    .filter(exam -> "60점미만".equals(exam.getReExamReason()))
                    .count();

            long doExamCount = exams.size() - absentCount;

            // 3. 결과 표시 (결석 1명, 재평가대상 1명 이런식으로 표시해야함)
            String temp1 = "";
            if (reExamExpectCount > 0) {
                temp1 += " (미이수 " + reExamExpectCount + "명)";
            }

            String temp2 = "";
            if (absentCount > 0) {
                temp2 += " / 결석 " + absentCount + "명";
            }

            this.ba = "재적 " + allStudentCount + "명 / 실시 " + doExamCount + "명" + temp1 + temp2;

            // 재평가 실시 인원
            this.sa = reExams.size() + "명";

            this.ah = "해당사항 없음 (추후수정필요)";
            this.ja = rePaper == null ? "해당사항 없음" : rePaper.getEvaluationDate().toString();
            this.sign = teacher.getTeacher().getSign();
        }
    }

    @Data
    public static class No2DTO {
        private String subjectPurpose; // 훈련목표
        private String subjectTitle; // 교과목명
        private List<String> subjectElements; // 세부내용들
        private RubricDTO rubricFirst;
        private List<RubricDTO> rubrics;

        public No2DTO(Subject subject, List<Question> questions) {
            this.subjectPurpose = subject.getPurpose();
            this.subjectTitle = subject.getTitle();
            this.subjectElements = subject.getElements().stream().map(subjectElement -> subjectElement.getSubtitle()).toList();
            this.rubricFirst = new RubricDTO(questions.get(0));
            this.rubrics = questions.stream().skip(1).map(question -> new RubricDTO(question)).toList();
        }

        @Data
        class RubricDTO {
            private String subjectElement;
            private String questionPurpose;
            private String questionFail;

            public RubricDTO(Question question) {
                this.subjectElement = question.getSubjectElement().getSubtitle();
                this.questionPurpose = question.getQuestionPurpose();
                this.questionFail = question.getQuestionFail();
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
    public static class No1DTO {
        private String courseTitle;
        private Integer courseRound;
        private String courseStartDate;
        private String courseEndDate;
        private String subjectTitle;
        private String subjectEvaluationWay;
        private String subjectTeacherName;
        private String subjectEvaluationDate; // 채점일시 == 평가일시 == 제출기한
        private String loc;
        private List<String> subjectElements;
        private String submitWay; // 제출방법 : 온라인
        private List<String> examRates; // 평가 배점 (1차, 2차), 결시자는 2차 평가와 같다.
        private String equipment; // 평가환경 장비
        private List<QuestionDTO> questions;
        private String sign;

        // 교과목 조회, 교과목의 본평가 시험지로 출제된 문제 목록 조회
        public No1DTO(Subject subject, List<Question> questions, String sign, Paper paper) {
            this.courseTitle = subject.getCourse().getTitle();
            this.courseRound = subject.getCourse().getRound();
            this.courseStartDate = subject.getCourse().getStartDate().toString();
            this.courseEndDate = MyUtil.localDateToString(subject.getCourse().getEndDate());
            this.subjectTitle = subject.getTitle();
            this.subjectEvaluationWay = paper.getEvaluationWay();
            this.subjectTeacherName = subject.getTeacherName();
            this.subjectEvaluationDate = MyUtil.localDateToString(paper.getEvaluationDate());
            this.loc = "3호";
            this.subjectElements = subject.getElements().stream().map(subjectElement -> subjectElement.getSubtitle()).toList();
            this.submitWay = "온라인 제출";
            this.examRates = Arrays.asList("본평가 배점 : 평가점수 X 100%", "재평가 배점 : 평가점수 X 90%", "결시자 배점 : 평가점수 X 90%");
            this.equipment = "인터넷이 설치되어 있는 PC";
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
                this.point = question.getPoint();
                this.answerNumber = question.getAnswerNumber();
                this.questionPurpose = question.getQuestionPurpose();
                this.options = question.getQuestionOptions().stream().map(OptionDTO::new).toList();
                ;
            }

            @Data
            class OptionDTO {
                private Integer no;
                private String content;
                private Boolean isRight;

                public OptionDTO(QuestionOption option) {
                    this.no = option.getNo();
                    this.content = option.getContent();
                    this.isRight = option.getIsRight();
                }
            }
        }
    }


    @Data
    public static class No3DTO {
        private String teacherName;
        private String evaluationDate; // 평가일 (subject)
        private String loc; // 평가장소 (임시)
        private String subjectTitle; // 교과목 (subject)
        private List<String> subjectElements;
        private Integer questionCount;
        private String teacherSign;
        private Integer grade;
        private List<QuestionDTO> questions;

        public No3DTO(Paper paper, List<SubjectElement> subjectElements, List<Question> questions, User teacher) {
            this.teacherName = paper.getSubject().getTeacherName();
            this.evaluationDate = paper.getEvaluationDate().toString();
            this.loc = "3호";
            this.subjectTitle = paper.getSubject().getTitle();
            this.subjectElements = subjectElements.stream().map(se -> se.getSubtitle()).toList();
            this.questionCount = paper.getCount();
            this.teacherSign = teacher.getTeacher().getSign();
            this.grade = paper.getSubject().getGrade();
            this.questions = questions.stream().map(QuestionDTO::new).toList();
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
                this.point = question.getPoint();
                this.answerNumber = question.getAnswerNumber();
                this.questionPurpose = question.getQuestionPurpose();
                this.options = question.getQuestionOptions().stream().map(OptionDTO::new).toList();
                ;
            }

            @Data
            class OptionDTO {
                private Integer no;
                private String content;
                private Boolean isRight;

                public OptionDTO(QuestionOption option) {
                    this.no = option.getNo();
                    this.content = option.getContent();
                    this.isRight = option.getIsRight();
                }
            }
        }
    }

    @Data
    public static class No4DTO {
        private Long subjectId;
        private Long courseId;
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
        private Integer prevIndex;
        private Integer nextIndex;
        private Integer no;

        public No4DTO(Exam exam, List<SubjectElement> subjectElements, Teacher teacher, Integer prevIndex, Integer nextIndex, Integer currentIndex) {
            this.subjectId = exam.getPaper().getSubject().getId();
            this.courseId = exam.getPaper().getSubject().getCourse().getId();
            this.examId = exam.getId();
            this.paperId = exam.getPaper().getId();
            this.studentName = exam.getStudent().getUser().getName();
            this.teacherName = exam.getTeacherName();
            this.evaluationDate = exam.getPaper().getEvaluationDate().toString();
            this.loc = "3호";
            this.subjectTitle = exam.getPaper().getSubject().getTitle();
            this.subjectElements = subjectElements.stream().map(se -> se.getSubtitle()).toList();
            this.answers = exam.getExamAnswers().stream().map(AnswerDTO::new).toList();
            this.questionCount = exam.getPaper().getCount();
            this.examState = exam.getExamState();
            this.reExamReason = exam.getReExamReason();
            this.examPassState = exam.getPassState();
            this.score = exam.getScore();
            this.teacherComment = exam.getTeacherComment();
            this.grade = exam.getGrade();
            this.teacherSign = teacher.getSign();
            this.studentSign = exam.getStudentSign();
            this.isStudentSign = exam.getStudentSign() == null ? false : true;
            this.prevIndex = prevIndex;
            this.nextIndex = nextIndex;
            this.no = currentIndex + 1;
        }

        @Data
        class AnswerDTO {
            private Long answerId;
            private Long questionId;
            private Integer no;
            private String title;
            private Integer point;
            private Integer answerNumber; // 정답 번호
            private Integer selectedOptionNo; // 학생 선택 번호
            private Integer studentPoint;
            private List<OptionDTO> options;

            public AnswerDTO(ExamAnswer answer) {
                this.answerId = answer.getId();
                this.questionId = answer.getQuestion().getId();
                this.no = answer.getQuestion().getNo();
                this.title = answer.getQuestion().getTitle();
                this.point = answer.getQuestion().getPoint();
                this.answerNumber = answer.getQuestion().getAnswerNumber();
                this.selectedOptionNo = answer.getSelectedOptionNo();
                this.studentPoint = answer.getIsCorrect() ? point : 0;
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
}
