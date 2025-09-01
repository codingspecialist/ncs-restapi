package shop.mtcoding.blog.document.web.dto;

import lombok.Data;
import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog.course.application.domain.Course;
import shop.mtcoding.blog.course.application.domain.Subject;
import shop.mtcoding.blog.course.application.domain.SubjectElement;
import shop.mtcoding.blog.exam.application.domain.Exam;
import shop.mtcoding.blog.exam.application.domain.Paper;
import shop.mtcoding.blog.exam.application.domain.Question;
import shop.mtcoding.blog.exam.application.domain.enums.EvaluationWay;
import shop.mtcoding.blog.exam.application.domain.enums.ExamResultStatus;
import shop.mtcoding.blog.user.application.domain.Teacher;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.springframework.data.domain.Page;

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
        private String ja; // 교과목ID로 paper를 조회하는데, isUse가 true걸로, 그 중에 isReEvaluation이 true인것들의 평가일자
        // 0번지하나만!!

        private String sign;

        public No5DTO(List<Exam> exams, List<Exam> reExams, Teacher teacher) {
            Paper paper = exams.stream().map(Exam::getPaper).findFirst().orElse(null);
            Paper rePaper = reExams.stream().map(Exam::getPaper).findFirst().orElse(null);
            this.draftingTeam = "교육운영팀";
            this.docNumber = "부산 " + paper.getEvaluationDate().toString().substring(2);
            this.writingDate = paper.getEvaluationDate().toString();
            this.requestDate = paper.getEvaluationDate().toString();
            this.retentionPeriod = "5년";
            this.author = paper.getSubject().getCourseTeacher().getTeacher().getName();
            this.recipient = "내부결재";
            this.title = paper.getSubject().getCourse().getTitle() + " 평가 실시보고";
            this.ga = paper.getSubject().getCourse().getTitle();
            this.na = paper.getSubject().getCourse().getStartDate() + " ~ "
                    + paper.getSubject().getCourse().getEndDate() + " (" + paper.getSubject().getCourse().getTotalTime()
                    + "시간)";
            this.da = paper.getSubject().getTitle() + " (" + paper.getEvaluationWay().toKorean() + ")";
            this.ra = paper.getSubject().getCourseTeacher().getTeacher().getName();
            this.ma = paper.getEvaluationDate().toString();

            // 중탈안한 총 학생수
            long allStudentCount = paper.getSubject().getCourse().getCourseStudents().stream()
                    .filter(student -> student.getStudent().getDropOutDate() == null
                            || student.getStudent().getDropOutDate().isAfter(paper.getEvaluationDate()))
                    .count();
            // 1. 본평가 응시자 수 (시험을 실제로 사용하고, 재시험 아님) - 결석자 제외
            long notTakenCount = exams.stream()
                    .filter(exam -> exam.getResultStatus() == ExamResultStatus.NOT_TAKEN)
                    .count();

            long reTestCount = exams.stream()
                    .filter(exam -> exam.getResultStatus() == ExamResultStatus.FAIL)
                    .count();

            long doExamCount = exams.size() - notTakenCount;

            // 3. 결과 표시 (결석 1명, 재평가대상 1명 이런식으로 표시해야함)
            String temp1 = "";
            if (reTestCount > 0) {
                temp1 += " (미이수 " + reTestCount + "명)";
            }

            String temp2 = "";
            if (notTakenCount > 0) {
                temp2 += " / 미응시 " + notTakenCount + "명";
            }

            this.ba = "재적 " + allStudentCount + "명 / 실시 " + doExamCount + "명" + temp1 + temp2;

            // 재평가 실시 인원
            this.sa = reExams.size() + "명";

            this.ah = "해당사항 없음 (추후수정필요)";
            this.ja = rePaper == null ? "해당사항 없음" : rePaper.getEvaluationDate().toString();
            this.sign = teacher.getSign();
        }
    }

    @Data
    public static class No4McqDTO {
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
        private String ja; // 교과목ID로 paper를 조회하는데, isUse가 true걸로, 그 중에 isReEvaluation이 true인것들의 평가일자
        // 0번지하나만!!

        private String sign;

        public No4McqDTO(Exam exam, List<SubjectElement> elements, Teacher teacher, Integer prevIndex,
                Integer nextIndex, Integer currentIndex) {
            Paper paper = exam.getPaper();
            this.draftingTeam = "교육운영팀";
            this.docNumber = "부산 " + paper.getEvaluationDate().toString().substring(2);
            this.writingDate = paper.getEvaluationDate().toString();
            this.requestDate = paper.getEvaluationDate().toString();
            this.retentionPeriod = "5년";
            this.author = paper.getSubject().getCourseTeacher().getTeacher().getName();
            this.recipient = "내부결재";
            this.title = paper.getSubject().getCourse().getTitle() + " 평가 실시보고";
            this.ga = paper.getSubject().getCourse().getTitle();
            this.na = paper.getSubject().getCourse().getStartDate() + " ~ "
                    + paper.getSubject().getCourse().getEndDate() + " (" + paper.getSubject().getCourse().getTotalTime()
                    + "시간)";
            this.da = paper.getSubject().getTitle() + " (" + paper.getEvaluationWay().toKorean() + ")";
            this.ra = paper.getSubject().getCourseTeacher().getTeacher().getName();
            this.ma = paper.getEvaluationDate().toString();

            // 중탈안한 총 학생수
            long allStudentCount = paper.getSubject().getCourse().getCourseStudents().stream()
                    .filter(student -> student.getStudent().getDropOutDate() == null
                            || student.getStudent().getDropOutDate().isAfter(paper.getEvaluationDate()))
                    .count();
            this.ba = "재적 " + allStudentCount + "명";

            // 재평가 실시 인원
            this.sa = "1명";

            this.ah = "해당사항 없음 (추후수정필요)";
            this.ja = "해당사항 없음";
            this.sign = teacher.getSign();
        }
    }

    @Data
    public static class No4RubricDTO {
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
        private String ja; // 교과목ID로 paper를 조회하는데, isUse가 true걸로, 그 중에 isReEvaluation이 true인것들의 평가일자
        // 0번지하나만!!

        private String sign;

        public No4RubricDTO(Exam exam, List<SubjectElement> elements, Teacher teacher, Integer prevIndex,
                Integer nextIndex, Integer currentIndex) {
            Paper paper = exam.getPaper();
            this.draftingTeam = "교육운영팀";
            this.docNumber = "부산 " + paper.getEvaluationDate().toString().substring(2);
            this.writingDate = paper.getEvaluationDate().toString();
            this.requestDate = paper.getEvaluationDate().toString();
            this.retentionPeriod = "5년";
            this.author = paper.getSubject().getCourseTeacher().getTeacher().getName();
            this.recipient = "내부결재";
            this.title = paper.getSubject().getCourse().getTitle() + " 평가 실시보고";
            this.ga = paper.getSubject().getCourse().getTitle();
            this.na = paper.getSubject().getCourse().getStartDate() + " ~ "
                    + paper.getSubject().getCourse().getEndDate() + " (" + paper.getSubject().getCourse().getTotalTime()
                    + "시간)";
            this.da = paper.getSubject().getTitle() + " (" + paper.getEvaluationWay().toKorean() + ")";
            this.ra = paper.getSubject().getCourseTeacher().getTeacher().getName();
            this.ma = paper.getEvaluationDate().toString();

            // 중탈안한 총 학생수
            long allStudentCount = paper.getSubject().getCourse().getCourseStudents().stream()
                    .filter(student -> student.getStudent().getDropOutDate() == null
                            || student.getStudent().getDropOutDate().isAfter(paper.getEvaluationDate()))
                    .count();
            this.ba = "재적 " + allStudentCount + "명";

            // 재평가 실시 인원
            this.sa = "1명";

            this.ah = "해당사항 없음 (추후수정필요)";
            this.ja = "해당사항 없음";
            this.sign = teacher.getSign();
        }
    }

    @Data
    public static class No3McqDTO {
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
        private String ja; // 교과목ID로 paper를 조회하는데, isUse가 true걸로, 그 중에 isReEvaluation이 true인것들의 평가일자
        // 0번지하나만!!

        private String sign;

        public No3McqDTO(Paper paper, List<SubjectElement> elements, List<Question> questions, Teacher teacher) {
            this.draftingTeam = "교육운영팀";
            this.docNumber = "부산 " + paper.getEvaluationDate().toString().substring(2);
            this.writingDate = paper.getEvaluationDate().toString();
            this.requestDate = paper.getEvaluationDate().toString();
            this.retentionPeriod = "5년";
            this.author = paper.getSubject().getCourseTeacher().getTeacher().getName();
            this.recipient = "내부결재";
            this.title = paper.getSubject().getCourse().getTitle() + " 평가 실시보고";
            this.ga = paper.getSubject().getCourse().getTitle();
            this.na = paper.getSubject().getCourse().getStartDate() + " ~ "
                    + paper.getSubject().getCourse().getEndDate() + " (" + paper.getSubject().getCourse().getTotalTime()
                    + "시간)";
            this.da = paper.getSubject().getTitle() + " (" + paper.getEvaluationWay().toKorean() + ")";
            this.ra = paper.getSubject().getCourseTeacher().getTeacher().getName();
            this.ma = paper.getEvaluationDate().toString();

            // 중탈안한 총 학생수
            long allStudentCount = paper.getSubject().getCourse().getCourseStudents().stream()
                    .filter(student -> student.getStudent().getDropOutDate() == null
                            || student.getStudent().getDropOutDate().isAfter(paper.getEvaluationDate()))
                    .count();
            this.ba = "재적 " + allStudentCount + "명";

            // 재평가 실시 인원
            this.sa = "1명";

            this.ah = "해당사항 없음 (추후수정필요)";
            this.ja = "해당사항 없음";
            this.sign = teacher.getSign();
        }
    }

    @Data
    public static class No3RubricDTO {
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
        private String ja; // 교과목ID로 paper를 조회하는데, isUse가 true걸로, 그 중에 isReEvaluation이 true인것들의 평가일자
        // 0번지하나만!!

        private String sign;

        public No3RubricDTO(Paper paper, List<Question> questions, Teacher teacher) {
            this.draftingTeam = "교육운영팀";
            this.docNumber = "부산 " + paper.getEvaluationDate().toString().substring(2);
            this.writingDate = paper.getEvaluationDate().toString();
            this.requestDate = paper.getEvaluationDate().toString();
            this.retentionPeriod = "5년";
            this.author = paper.getSubject().getCourseTeacher().getTeacher().getName();
            this.recipient = "내부결재";
            this.title = paper.getSubject().getCourse().getTitle() + " 평가 실시보고";
            this.ga = paper.getSubject().getCourse().getTitle();
            this.na = paper.getSubject().getCourse().getStartDate() + " ~ "
                    + paper.getSubject().getCourse().getEndDate() + " (" + paper.getSubject().getCourse().getTotalTime()
                    + "시간)";
            this.da = paper.getSubject().getTitle() + " (" + paper.getEvaluationWay().toKorean() + ")";
            this.ra = paper.getSubject().getCourseTeacher().getTeacher().getName();
            this.ma = paper.getEvaluationDate().toString();

            // 중탈안한 총 학생수
            long allStudentCount = paper.getSubject().getCourse().getCourseStudents().stream()
                    .filter(student -> student.getStudent().getDropOutDate() == null
                            || student.getStudent().getDropOutDate().isAfter(paper.getEvaluationDate()))
                    .count();
            this.ba = "재적 " + allStudentCount + "명";

            // 재평가 실시 인원
            this.sa = "1명";

            this.ah = "해당사항 없음 (추후수정필요)";
            this.ja = "해당사항 없음";
            this.sign = teacher.getSign();
        }
    }

    @Data
    public static class No2McqDTO {
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
        private String ja; // 교과목ID로 paper를 조회하는데, isUse가 true걸로, 그 중에 isReEvaluation이 true인것들의 평가일자
        // 0번지하나만!!

        private String sign;

        public No2McqDTO(Subject subject, List<Question> questions) {
            this.draftingTeam = "교육운영팀";
            this.docNumber = "부산 임시";
            this.writingDate = "임시";
            this.requestDate = "임시";
            this.retentionPeriod = "5년";
            this.author = subject.getCourseTeacher().getTeacher().getName();
            this.recipient = "내부결재";
            this.title = subject.getCourse().getTitle() + " 평가 실시보고";
            this.ga = subject.getCourse().getTitle();
            this.na = subject.getCourse().getStartDate() + " ~ "
                    + subject.getCourse().getEndDate() + " (" + subject.getCourse().getTotalTime()
                    + "시간)";
            this.da = subject.getTitle() + " (임시)";
            this.ra = subject.getCourseTeacher().getTeacher().getName();
            this.ma = "임시";

            // 중탈안한 총 학생수
            long allStudentCount = subject.getCourse().getCourseStudents().stream()
                    .filter(student -> student.getStudent().getDropOutDate() == null)
                    .count();
            this.ba = "재적 " + allStudentCount + "명";

            // 재평가 실시 인원
            this.sa = "1명";

            this.ah = "해당사항 없음 (추후수정필요)";
            this.ja = "해당사항 없음";
            this.sign = null;
        }
    }

    @Data
    public static class No2RubricDTO {
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
        private String ja; // 교과목ID로 paper를 조회하는데, isUse가 true걸로, 그 중에 isReEvaluation이 true인것들의 평가일자
        // 0번지하나만!!

        private String sign;

        public No2RubricDTO(Subject subject, List<Question> questions) {
            this.draftingTeam = "교육운영팀";
            this.docNumber = "부산 임시";
            this.writingDate = "임시";
            this.requestDate = "임시";
            this.retentionPeriod = "5년";
            this.author = subject.getCourseTeacher().getTeacher().getName();
            this.recipient = "내부결재";
            this.title = subject.getCourse().getTitle() + " 평가 실시보고";
            this.ga = subject.getCourse().getTitle();
            this.na = subject.getCourse().getStartDate() + " ~ "
                    + subject.getCourse().getEndDate() + " (" + subject.getCourse().getTotalTime()
                    + "시간)";
            this.da = subject.getTitle() + " (임시)";
            this.ra = subject.getCourseTeacher().getTeacher().getName();
            this.ma = "임시";

            // 중탈안한 총 학생수
            long allStudentCount = subject.getCourse().getCourseStudents().stream()
                    .filter(student -> student.getStudent().getDropOutDate() == null)
                    .count();
            this.ba = "재적 " + allStudentCount + "명";

            // 재평가 실시 인원
            this.sa = "1명";

            this.ah = "해당사항 없음 (추후수정필요)";
            this.ja = "해당사항 없음";
            this.sign = null;
        }
    }

    @Data
    public static class No1McqDTO {
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
        private String ja; // 교과목ID로 paper를 조회하는데, isUse가 true걸로, 그 중에 isReEvaluation이 true인것들의 평가일자
        // 0번지하나만!!

        private String sign;

        public No1McqDTO(Subject subject, List<Question> questions, String teacherSign, Paper paper) {
            this.draftingTeam = "교육운영팀";
            this.docNumber = "부산 " + paper.getEvaluationDate().toString().substring(2);
            this.writingDate = paper.getEvaluationDate().toString();
            this.requestDate = paper.getEvaluationDate().toString();
            this.retentionPeriod = "5년";
            this.author = paper.getSubject().getCourseTeacher().getTeacher().getName();
            this.recipient = "내부결재";
            this.title = paper.getSubject().getCourse().getTitle() + " 평가 실시보고";
            this.ga = paper.getSubject().getCourse().getTitle();
            this.na = paper.getSubject().getCourse().getStartDate() + " ~ "
                    + paper.getSubject().getCourse().getEndDate() + " (" + paper.getSubject().getCourse().getTotalTime()
                    + "시간)";
            this.da = paper.getSubject().getTitle() + " (" + paper.getEvaluationWay().toKorean() + ")";
            this.ra = paper.getSubject().getCourseTeacher().getTeacher().getName();
            this.ma = paper.getEvaluationDate().toString();

            // 중탈안한 총 학생수
            long allStudentCount = paper.getSubject().getCourse().getCourseStudents().stream()
                    .filter(student -> student.getStudent().getDropOutDate() == null
                            || student.getStudent().getDropOutDate().isAfter(paper.getEvaluationDate()))
                    .count();
            this.ba = "재적 " + allStudentCount + "명";

            // 재평가 실시 인원
            this.sa = "1명";

            this.ah = "해당사항 없음 (추후수정필요)";
            this.ja = "해당사항 없음";
            this.sign = teacherSign;
        }
    }

    @Data
    public static class No1RubricDTO {
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
        private String ja; // 교과목ID로 paper를 조회하는데, isUse가 true걸로, 그 중에 isReEvaluation이 true인것들의 평가일자
        // 0번지하나만!!

        private String sign;

        public No1RubricDTO(Subject subject, List<Question> questions, String teacherSign, Paper paper) {
            this.draftingTeam = "교육운영팀";
            this.docNumber = "부산 " + paper.getEvaluationDate().toString().substring(2);
            this.writingDate = paper.getEvaluationDate().toString();
            this.requestDate = paper.getEvaluationDate().toString();
            this.retentionPeriod = "5년";
            this.author = paper.getSubject().getCourseTeacher().getTeacher().getName();
            this.recipient = "내부결재";
            this.title = paper.getSubject().getCourse().getTitle() + " 평가 실시보고";
            this.ga = paper.getSubject().getCourse().getTitle();
            this.na = paper.getSubject().getCourse().getStartDate() + " ~ "
                    + paper.getSubject().getCourse().getEndDate() + " (" + paper.getSubject().getCourse().getTotalTime()
                    + "시간)";
            this.da = paper.getSubject().getTitle() + " (" + paper.getEvaluationWay().toKorean() + ")";
            this.ra = paper.getSubject().getCourseTeacher().getTeacher().getName();
            this.ma = paper.getEvaluationDate().toString();

            // 중탈안한 총 학생수
            long allStudentCount = paper.getSubject().getCourse().getCourseStudents().stream()
                    .filter(student -> student.getStudent().getDropOutDate() == null
                            || student.getStudent().getDropOutDate().isAfter(paper.getEvaluationDate()))
                    .count();
            this.ba = "재적 " + allStudentCount + "명";

            // 재평가 실시 인원
            this.sa = "1명";

            this.ah = "해당사항 없음 (추후수정필요)";
            this.ja = "해당사항 없음";
            this.sign = teacherSign;
        }
    }

    @Data
    public static class SubjectDTO {
        private Long id;
        private String title;
        private String evaluationWay;

        public SubjectDTO(Subject subject) {
            this.id = subject.getId();
            this.title = subject.getTitle();
            this.evaluationWay = "NCS"; // Subject에는 evaluationWay가 없음
        }
    }

    @Data
    public static class CourseDTO {
        private Long id;
        private String title;
        private String startDate;
        private String endDate;
        private String mainTeacherName;

        public CourseDTO(Course course) {
            this.id = course.getId();
            this.title = course.getTitle();
            this.startDate = course.getStartDate().toString();
            this.endDate = course.getEndDate().toString();
            this.mainTeacherName = course.getMainTeacherName();
        }
    }

}