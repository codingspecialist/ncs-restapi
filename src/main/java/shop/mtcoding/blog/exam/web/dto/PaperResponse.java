package shop.mtcoding.blog.exam.web.dto;

import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog.course.application.domain.SubjectElement;
import shop.mtcoding.blog.exam.application.domain.Paper;
import shop.mtcoding.blog.exam.application.domain.Question;
import shop.mtcoding.blog.exam.application.domain.QuestionOption;
import shop.mtcoding.blog.exam.application.domain.enums.EvaluationWay;

import java.util.List;

public class PaperResponse {

    public record McqDetail(
            Long paperId,
            String evaluationDate,
            String evaluationDevice,
            String evaluationRoom,
            String subjectTitle,
            String teacherName,
            Integer questionCount,
            List<QuestionMax> questions
    ) {
        private McqDetail(Paper paper, List<Question> questions) {
            this(
                    paper.getId(),
                    paper.getEvaluationDate().toString(),
                    paper.getEvaluationDevice(),
                    paper.getEvaluationRoom(),
                    paper.getSubject().getTitle(),
                    paper.getSubject().getCourseTeacher().getTeacher().getName(),
                    questions.size(),
                    questions.stream().map(QuestionMax::from).toList()
            );
        }

        public static McqDetail from(Paper paper, List<Question> questions) {
            return new McqDetail(paper, questions);
        }

        public record QuestionMax(
                Long questionId,
                Integer questionNo,
                String questionTitle,
                String questionSummary,
                Double maxScore,
                List<Option> options
        ) {
            private QuestionMax(Question question) {
                this(
                        question.getId(),
                        question.getNo(),
                        question.getTitle(),
                        question.getSummary(),
                        question.getPaper().getMaxScore(),
                        question.getQuestionOptions().stream().map(Option::from).toList()
                );
            }

            public static QuestionMax from(Question question) {
                return new QuestionMax(question);
            }

            public record Option(
                    Long optionId,
                    Integer optionNo,
                    String optionContent,
                    Integer optionPoint,
                    Boolean isRight
            ) {
                private Option(QuestionOption option) {
                    this(
                            option.getId(),
                            option.getNo(),
                            option.getContent(),
                            option.getPoint(),
                            option.getPoint() > 0
                    );
                }

                public static Option from(QuestionOption option) {
                    return new Option(option);
                }
            }
        }
    }

    public record RubricDetail(
            Long paperId,
            String evaluationDate,
            String evaluationRoom,
            String evaluationDevice,
            String subjectTitle,
            String teacherName,
            Integer questionCount,
            String taskTitle,
            String taskScenario,
            String taskScenarioGuideLink,
            List<String> taskSubmitFormats,
            String taskSubmitTemplateLink,
            List<String> taskChallenges,
            List<QuestionMax> questions
    ) {
        private RubricDetail(Paper paper, List<Question> questions) {
            this(
                    paper.getId(),
                    paper.getEvaluationDate().toString(),
                    paper.getEvaluationDevice(),
                    paper.getEvaluationRoom(),
                    paper.getSubject().getTitle(),
                    paper.getSubject().getCourseTeacher().getTeacher().getName(),
                    questions.size(),
                    paper.getTaskTitle(),
                    paper.getTaskScenario(),
                    paper.getTaskScenarioGuideLink(),
                    MyUtil.parseMultilineWithoutHyphen(paper.getTaskSubmitFormat()),
                    paper.getTaskSubmitTemplateLink(),
                    MyUtil.parseMultilineWithoutHyphen(paper.getTaskChallenge()),
                    questions.stream().map(QuestionMax::from).toList()
            );
        }

        public static RubricDetail from(Paper paper, List<Question> questions) {
            return new RubricDetail(paper, questions);
        }

        public record QuestionMax(
                Long questionId,
                Integer questionNo,
                String questionTitle,
                List<String> questionSummaries,
                List<Option> options
        ) {
            private QuestionMax(Question question) {
                this(
                        question.getId(),
                        question.getNo(),
                        question.getTitle(),
                        MyUtil.parseMultiline(question.getSummary()),
                        question.getQuestionOptions().stream().map(Option::from).toList()
                );
            }

            public static QuestionMax from(Question question) {
                return new QuestionMax(question);
            }

            public record Option(
                    Long optionId,
                    Integer optionNo,
                    String optionContent,
                    Integer optionPoint
            ) {
                private Option(QuestionOption option) {
                    this(
                            option.getId(),
                            option.getNo(),
                            option.getContent(),
                            option.getPoint()
                    );
                }

                public static Option from(QuestionOption option) {
                    return new Option(option);
                }
            }
        }
    }

    public record Max(
            Long paperId,
            String courseTitle,
            Integer courseRound,
            Long subjectId,
            String subjectTitle,
            Integer questionCount,
            String paperVersion,
            String evaluationWay,
            String evaluationDate
    ) {
        private Max(Paper paper) {
            this(
                    paper.getId(),
                    null, // paper.getSubject().getCourse().getTitle(),
                    null, // paper.getSubject().getCourse().getRound(),
                    null, // paper.getSubject().getId(),
                    null, // paper.getSubject().getTitle(),
                    paper.getQuestions().size(),
                    paper.getPaperVersion().toKorean(),
                    paper.getEvaluationWay().toKorean(),
                    MyUtil.localDateToString(paper.getEvaluationDate())
            );
        }

        public static Max from(Paper paper) {
            return new Max(paper);
        }
    }

    public record MaxList(List<Paper> papers) {
    }

    public record Detail(Paper paper, List<Question> questions) {
    }

    public record NextQuestion(Integer expectNo, Long paperId, EvaluationWay evaluationWay, List<Element> elements) {
        public NextQuestion(Integer expectNo, Long paperId) {
            this(expectNo, paperId, null, List.of());
        }

        public NextQuestion withElements(List<SubjectElement> subjectElements, Paper paper) {
            List<Element> converted = subjectElements.stream()
                    .map(e -> new Element(e.getId(), e.getTitle()))
                    .toList();
            return new NextQuestion(expectNo, paperId, paper.getEvaluationWay(), converted);
        }

        record Element(Long elementId, String subtitle) {
        }
    }
}