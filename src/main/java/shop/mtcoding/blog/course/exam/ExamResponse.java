package shop.mtcoding.blog.course.exam;

import lombok.Data;
import shop.mtcoding.blog.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.course.subject.Subject;
import shop.mtcoding.blog.course.subject.element.SubjectElement;
import shop.mtcoding.blog.paper.Paper;
import shop.mtcoding.blog.paper.question.Question;
import shop.mtcoding.blog.paper.question.option.QuestionOption;
import shop.mtcoding.blog.user.teacher.Teacher;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ExamResponse {

    @Data
    public static class SubjectDTO {
        private Long subjectId;
        private String code; // 능력단위 코드
        private String title;
        private String purpose;
        private String gubun;
        private Integer grade;
        private Integer totalTime;
        private Integer no; // 과정에서 몇번째로 시작하는 교과목인지에 대한 연번
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
            Paper paper = subject.getPapers().stream().filter(p -> p.getIsReEvaluation() == false).findFirst().orElse(null);
            Paper rePaper = subject.getPapers().stream().filter(p -> p.getIsReEvaluation() == true).findFirst().orElse(null);

            this.subjectId = subject.getId();
            this.code = subject.getCode();
            this.title = subject.getTitle();
            this.purpose = subject.getPurpose();
            this.gubun = subject.getGubun();
            this.grade = subject.getGrade();
            this.totalTime = subject.getTotalTime();
            this.no = subject.getNo();
            this.learningWay = subject.getLearningWay();
            this.evaluationWay = paper.getEvaluationWay();
            this.evaluationDate = paper.getEvaluationDate().toString();

            if (rePaper != null) {
                this.revaluationDate = rePaper.getEvaluationDate().toString();
            } else {
                this.revaluationDate = "시험지없음";
            }
            this.startDate = subject.getStartDate();
            this.endDate = subject.getEndDate();
            this.courseId = subject.getCourse().getId();
            this.courseTitle = subject.getCourse().getTitle();
            this.courseRound = subject.getCourse().getRound();
        }
    }

    // 관리자가 보는 시험결과
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
        private Integer studentNo;
        private Integer prevIndex; // 해당 교과목에 이전 학생 id
        private Integer nextIndex; // 해당 교과목에 다음 학생 id
        private Boolean isAbsent;

        public ResultDetailDTO(Exam exam, List<SubjectElement> subjectElements, Teacher teacher, Integer prevIndex, Integer nextIndex, Integer currentIndex) {
            this.examId = exam.getId();
            this.paperId = exam.getPaper().getId();
            this.studentName = exam.getStudent().getName();
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
            this.studentNo = currentIndex + 1;
            this.prevIndex = prevIndex;
            this.nextIndex = nextIndex;
            this.isAbsent = exam.getReExamReason().equals("결석");
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

    // 학생이 보는 시험결과 (List로 보이는 화면)
    // PK, 번호(교과목번호), 과정명/회차, paper.getSubject(교과목), 시험유형, 학생명, 훈련강사, 결과점수, 통과여부, (통과못했거나, 재평가지로 재평가하기버튼필요)
    @Data
    public static class ResultDTO {
        // 교과목 번호, 교과목 내 시험 순서
        private Long examId;
        private Long paperId; // 몇번째 시험지에 몇번 학생? (1,2)
        private Integer studentNo; // 학생번호 필요 다음 버튼 클릭할때!!
        private Integer subjectNo;
        private String courseNameAndRound;
        private String subjectTitle;
        private String examState;
        private String studentName;
        private String teacherName;
        private Double examScore;
        private String examPassState;

        private String reExamReason;
        private Boolean isAbsent;
        private Boolean isNotPass;
        private Integer grade;
        private Boolean isUse;

        private Boolean isNotStart;
        private Long studentId;

        public ResultDTO() {
        }

        public ResultDTO(Exam exam) {
            this.studentId = exam.getStudent().getId();
            this.examId = exam.getId();
            this.paperId = exam.getPaper().getId();
            this.studentNo = 99;
            this.subjectNo = exam.getPaper().getSubject().getNo();
            this.courseNameAndRound = exam.getStudent().getCourse().getTitle() + "/" + exam.getStudent().getCourse().getRound() + "회차";
            this.subjectTitle = exam.getPaper().getSubject().getTitle();
            this.examState = exam.getExamState();
            this.studentName = exam.getStudent().getName();
            this.teacherName = exam.getTeacherName();
            this.examScore = exam.getScore();
            this.examPassState = exam.getPassState();
            this.isNotPass = exam.getScore() >= 60 ? false : true;
            if (exam.getReExamReason().equals("")) {
                this.reExamReason = exam.getReExamReason();
            } else {
                this.reExamReason = "/" + exam.getReExamReason();
            }
            this.grade = exam.getGrade();
            this.isUse = exam.getIsUse();
            this.isAbsent = exam.getReExamReason().equals("결석");
        }
    }

    @Data
    public static class StartDTO {
        private Long paperId;
        private String studentName;
        private String teacherName;
        private String evaluationDate; // 평가일 (subject)
        private String loc; // 평가장소 (임시)
        private String subjectTitle; // 교과목 (subject)
        private List<String> subjectElements;
        private List<QuestionDTO> questions;
        private Integer questionCount;

        public StartDTO(Paper paper, String studentName, List<SubjectElement> subjectElements, List<Question> questions) {
            this.paperId = paper.getId();
            this.studentName = studentName;
            this.teacherName = paper.getSubject().getTeacherName();
            this.evaluationDate = paper.getEvaluationDate().toString();
            this.loc = "3호";
            this.subjectTitle = paper.getSubject().getTitle();
            this.subjectElements = subjectElements.stream().map(se -> se.getSubtitle()).toList();
            this.questions = questions.stream().map(QuestionDTO::new).toList();
            this.questionCount = questions.size();
        }

        @Data
        class QuestionDTO {
            private Long questionId;
            private Integer no;
            private String title;
            private Integer point;
            private List<OptionDTO> options;

            public QuestionDTO(Question question) {
                this.questionId = question.getId();
                this.no = question.getNo();
                this.title = question.getTitle();
                this.point = question.getPoint();
                this.options = question.getQuestionOptions().stream().map(OptionDTO::new).toList();
            }

            @Data
            class OptionDTO {
                private Long optionId;
                private Integer no;
                private String content;

                public OptionDTO(QuestionOption option) {
                    this.optionId = option.getId();
                    this.no = option.getNo();
                    this.content = option.getContent();
                }
            }
        }
    }


    @Data
    public static class MyPaperListDTO {

        private Long studentId;
        private List<PaperDTO> papers;

        public MyPaperListDTO(Long studentId, List<Paper> papers, Map<Long, Boolean> attendanceMap) {
            this.studentId = studentId;
            this.papers = papers.stream().map(paper -> {

                Boolean isAttendance = attendanceMap.get(paper.getId());

                return new PaperDTO(paper, isAttendance);
            }).toList();
        }

        @Data
        class PaperDTO {
            private Integer subjectNo;
            private Long paperId;
            private String courseTitle;
            private Integer courseRound;
            private Long subjectId;
            private String subjectTitle; // 교과목명
            private Integer count; // 문항수
            private String paperState;
            private String teacherName;
            private Boolean isAttendance; // 시험 응시 이력이 있음?

            public PaperDTO(Paper paper, Boolean isAttendance) {
                this.subjectNo = paper.getSubject().getNo();
                this.paperId = paper.getId();
                this.courseTitle = paper.getSubject().getCourse().getTitle();
                this.courseRound = paper.getSubject().getCourse().getRound();
                this.subjectId = paper.getSubject().getId();
                this.subjectTitle = paper.getSubject().getTitle();
                this.count = paper.getCount();
                this.paperState = paper.getPaperState();
                this.teacherName = paper.getSubject().getTeacherName();
                this.isAttendance = isAttendance;
            }
        }


    }
}
