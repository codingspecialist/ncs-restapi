package shop.mtcoding.blog.web.exam;

import lombok.Data;
import org.springframework.data.domain.Page;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.course.exam.Exam;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.domain.course.student.Student;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.option.QuestionOption;
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

        private ResultDTO() {
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

        public static ExamResponse.ResultDTO ofAbsent(Paper mainPaper, Student student) {
            ExamResponse.ResultDTO dto = new ResultDTO();
            dto.setExamId(0L);
            dto.setPaperId(mainPaper.getId());
            dto.setStudentNo(99); // 미지정
            dto.setSubjectNo(mainPaper.getSubject().getNo());
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
            dto.setIsAbsent(true);
            return dto;
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
        private Integer studentNo;
        private Long prevExamId; // 해당 교과목에 이전 학생 id
        private Long nextExamId; // 해당 교과목에 다음 학생 id
        private Boolean isAbsent;
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
            this.reExamReason = exam.getReExamReason();
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

        // 미이수 결과 보기
        public ResultDetailDTO(Exam exam, List<SubjectElement> subjectElements, Teacher teacher) {
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
            this.reExamReason = exam.getReExamReason();
            this.examPassState = exam.getPassState();
            this.score = exam.getScore();
            this.teacherComment = exam.getTeacherComment();
            this.grade = exam.getGrade();
            this.teacherSign = teacher.getSign();
            this.studentSign = exam.getStudentSign();
            this.isStudentSign = exam.getStudentSign() == null ? false : true;
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

}
