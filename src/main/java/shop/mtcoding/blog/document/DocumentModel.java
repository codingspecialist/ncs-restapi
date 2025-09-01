//package shop.mtcoding.blog.document;
//
//import org.springframework.data.domain.Page;
//
//import java.util.List;
//
//public class DocumentModel {
//    public record No1(Subject subject, List<Question> questions, Teacher teacher, Paper paper) {
//    }
//
//    public record No2(EvaluationWay evaluationWay, Subject subject, List<Question> questions) {
//    }
//
//    public record No3(Paper paper, List<SubjectElement> elements, List<Question> questions, Teacher teacher) {
//    }
//
//    public record No4(Exam exam, List<SubjectElement> elements, Teacher teacher, Integer prevIndex, Integer nextIndex,
//                      Integer currentIndex) {
//    }
//
//    public record No5(List<Exam> exams, List<Exam> reExams, Teacher teacher) {
//    }
//
//    public record CourseSlice(Page<Course> coursePage) {
//    }
//
//    public record SubjectItems(List<Subject> subjects) {
//    }
//
//}
