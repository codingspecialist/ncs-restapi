package shop.mtcoding.blog.web.student;

import lombok.Data;
import shop.mtcoding.blog.domain.course.exam.Exam;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.domain.course.student.Student;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.option.QuestionOption;
import shop.mtcoding.blog.domain.user.teacher.Teacher;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class StudentExamResponse {

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
            private Integer questionCount; // 문항수
            private String paperType;
            private String teacherName;
            private Boolean isAttendance; // 시험 응시 이력이 있음?

            public PaperDTO(Paper paper, Boolean isAttendance) {
                this.subjectNo = paper.getSubject().getNo();
                this.paperId = paper.getId();
                this.courseTitle = paper.getSubject().getCourse().getTitle();
                this.courseRound = paper.getSubject().getCourse().getRound();
                this.subjectId = paper.getSubject().getId();
                this.subjectTitle = paper.getSubject().getTitle();
                this.questionCount = paper.getQuestionCount();
                this.paperType = paper.getPaperType().toKorean();
                this.teacherName = paper.getSubject().getTeacherName();
                this.isAttendance = isAttendance;
            }
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
            private List<OptionDTO> options;

            public QuestionDTO(Question question) {
                this.questionId = question.getId();
                this.no = question.getNo();
                this.title = question.getTitle();
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

        public static ResultDTO ofAbsent(Paper mainPaper, Student student) {
            ResultDTO dto = new ResultDTO();
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

    // 학생이 보는거니까, 연번, 이전, 다음, 미이수 결과 보는 번호 같은거 다 지우자.
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
                this.point = answer.getEarnedPoint();
                QuestionOption _option = answer.getQuestion().getQuestionOptions().stream()
                        .max(Comparator.comparingInt(QuestionOption::getPoint))
                        .orElse(null);
                this.answerNumber = _option.getNo();
                this.selectedOptionNo = answer.getSelectedOptionNo();
                this.studentPoint = answer.getIsRight() ? point : 0;
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
