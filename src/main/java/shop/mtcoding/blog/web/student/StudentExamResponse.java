package shop.mtcoding.blog.web.student;

import lombok.Data;
import shop.mtcoding.blog.core.utils.MyUtil;
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
                this.questionCount = paper.getQuestions().size();
                this.paperType = paper.getPaperType().toKorean();
                this.teacherName = paper.getSubject().getTeacherName();
                this.isAttendance = isAttendance;
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
        private List<QuestionDTO> questions;
        private Integer questionCount;

        public McqStartDTO(Paper paper, String studentName, List<SubjectElement> subjectElements, List<Question> questions) {
            this.paperId = paper.getId();
            this.studentName = studentName;
            this.teacherName = paper.getSubject().getTeacherName();
            this.evaluationDate = paper.getEvaluationDate().toString();
            this.evaluationDevice = paper.getEvaluationDevice();
            this.evaluationRoom = paper.getEvaluationRoom();
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
            private Integer totalPoint;
            private String exContent;
            private List<OptionDTO> options;

            public QuestionDTO(Question question) {
                this.questionId = question.getId();
                this.no = question.getNo();
                this.title = question.getTitle();
                this.totalPoint = question.getQuestionOptions().stream().mapToInt(option -> option.getPoint()).max().getAsInt();
                this.exContent = question.getExContent();
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
            this.teacherName = paper.getSubject().getTeacherName();
            this.evaluationDate = paper.getEvaluationDate().toString();
            this.evaluationDevice = paper.getEvaluationDevice();
            this.evaluationRoom = paper.getEvaluationRoom();
            this.subjectTitle = paper.getSubject().getTitle();
            this.subjectElements = subjectElements.stream().map(se -> se.getSubtitle()).toList();
            this.questions = questions.stream().map(QuestionDTO::new).toList();
            this.questionCount = questions.size();
            this.pblTitle = paper.getPblTitle();
            this.pblScenario = paper.getPblScenario();
            this.pblScenarioGuideLink = paper.getPblScenarioGuideLink();
            this.pblSubmitFormats = MyUtil.parseMultilineWithoutHyphen(paper.getPblSubmitFormat());
            this.pblSubmitTemplateLink = paper.getPblSubmitTemplateLink();
            this.pblChallenges = MyUtil.parseMultilineWithoutHyphen(paper.getPblChallenge());
        }

        @Data
        class QuestionDTO {
            private Long questionId;
            private Integer no;
            private String title;
            private Integer totalPoint; // 이 문제의 총점
            private List<String> scenarios; // 가이드 요약본
            private List<OptionDTO> options;

            public QuestionDTO(Question question) {
                this.questionId = question.getId();
                this.no = question.getNo();
                this.title = question.getTitle();
                this.totalPoint = question.getQuestionOptions().stream().mapToInt(QuestionOption::getPoint).max().getAsInt();
                this.scenarios = MyUtil.parseMultiline(question.getScenario());
                this.options = question.getQuestionOptions().stream().map(OptionDTO::new).toList();
            }

            @Data
            class OptionDTO {
                private Long optionId;
                private Integer no;
                private String rubricItem;
                private Integer point; // 각 루브릭의 점수

                public OptionDTO(QuestionOption option) {
                    this.optionId = option.getId();
                    this.no = option.getNo();
                    this.rubricItem = option.getRubricItem();
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
        private String examState;
        private String reExamReason;
        private String examPassState;
        private Double manjumScore;
        private Double score;
        private Double finalScore;
        private String submitLink;
        private String teacherComment;
        private Integer grade;
        private String teacherSign;
        private String studentSign;
        private Boolean isAbsent;

        public RubricResultDetailDTO(Exam exam, List<SubjectElement> subjectElements, Teacher teacher) {
            this.examId = exam.getId();
            this.paperId = exam.getPaper().getId();
            this.studentName = exam.getStudent().getName();
            this.teacherName = exam.getTeacherName();
            this.evaluationDate = exam.getPaper().getEvaluationDate().toString();
            this.evaluationRoom = exam.getPaper().getEvaluationRoom();
            this.evaluationDevice = exam.getPaper().getEvaluationDevice();
            this.subjectTitle = exam.getPaper().getSubject().getTitle();
            this.subjectElements = subjectElements.stream().map(se -> se.getSubtitle()).toList();
            this.answers = exam.getExamAnswers().stream().map(AnswerDTO::new).toList();
            this.questionCount = exam.getPaper().getQuestions().size();
            this.examState = exam.getExamState();
            this.reExamReason = exam.getReExamReason();
            this.examPassState = exam.getPassState();
            this.manjumScore = exam.getManjumScore();
            this.score = exam.getScore();
            this.finalScore = exam.getFinalScore();
            this.submitLink = exam.getSubmitLink();
            this.teacherComment = exam.getTeacherComment();
            this.grade = exam.getGrade();
            this.teacherSign = teacher.getSign();
            this.studentSign = exam.getStudentSign();
            this.isAbsent = exam.getReExamReason().equals("결석");
        }

        @Data
        class AnswerDTO {
            private Long answerId;
            private Long questionId;
            private Integer no;
            private String title;
            private Integer totalPoint; // 배점
            private Integer selectedOptionNo; // 학생 선택 번호
            private Integer studentPoint;
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
                this.codeReviewLink = answer.getCodeReviewLink();
                this.codeReviewPRLink = answer.getCodeReviewPRLink();
                this.scenarios = MyUtil.parseMultilineWithoutHyphen(answer.getQuestion().getScenario());
                this.selectedOptionNo = answer.getSelectedOptionNo();
                this.studentPoint = answer.getEarnedPoint();
                this.options = answer.getQuestion().getQuestionOptions().stream().map(option -> new OptionDTO(option, selectedOptionNo)).toList();
            }

            @Data
            class OptionDTO {
                private Long optionId;
                private Integer no;
                private String rubricItem;
                private Boolean isSelect; // 해당 옵션이 선택되었는지 여부
                private Integer point;

                public OptionDTO(QuestionOption option, Integer selectedOptionNo) {
                    this.optionId = option.getId();
                    this.no = option.getNo();
                    this.rubricItem = option.getRubricItem();
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
        private String examState;
        private String reExamReason;
        private String examPassState;
        private Double score;
        private Double manjumScore;
        private Double finalScore;
        private String teacherComment;
        private Integer grade;
        private String teacherSign;
        private String studentSign;
        private Boolean isAbsent;

        public McqResultDetailDTO(Exam exam, List<SubjectElement> subjectElements, Teacher teacher) {
            this.examId = exam.getId();
            this.paperId = exam.getPaper().getId();
            this.studentName = exam.getStudent().getName();
            this.teacherName = exam.getTeacherName();
            this.evaluationDate = exam.getPaper().getEvaluationDate().toString();
            this.evaluationRoom = exam.getPaper().getEvaluationRoom();
            this.evaluationDevice = exam.getPaper().getEvaluationDevice();
            this.subjectTitle = exam.getPaper().getSubject().getTitle();
            this.subjectElements = subjectElements.stream().map(se -> se.getSubtitle()).toList();
            this.answers = exam.getExamAnswers().stream().map(AnswerDTO::new).toList();
            this.questionCount = exam.getPaper().getQuestions().size();
            this.examState = exam.getExamState();
            this.reExamReason = exam.getReExamReason();
            this.examPassState = exam.getPassState();
            this.manjumScore = exam.getManjumScore();
            this.score = exam.getScore();
            this.finalScore = exam.getFinalScore();
            this.teacherComment = exam.getTeacherComment();
            this.grade = exam.getGrade();
            this.teacherSign = teacher.getSign();
            this.studentSign = exam.getStudentSign();
            this.isAbsent = exam.getReExamReason().equals("결석");
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
            private String exContent;
            private List<OptionDTO> options;

            public AnswerDTO(ExamAnswer answer) {
                this.answerId = answer.getId();
                this.questionId = answer.getQuestion().getId();
                this.no = answer.getQuestion().getNo();
                this.title = answer.getQuestion().getTitle();

                this.exContent = answer.getQuestion().getExContent();
                QuestionOption _option = answer.getQuestion().getQuestionOptions().stream()
                        .max(Comparator.comparingInt(QuestionOption::getPoint))
                        .orElse(null);
                this.totalPoint = _option.getPoint(); // 배점
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
                    this.isSelect = no == selectedOptionNo ? true : false;
                }
            }
        }
    }

}
