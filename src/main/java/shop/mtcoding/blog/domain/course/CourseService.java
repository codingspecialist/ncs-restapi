package shop.mtcoding.blog.domain.course;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.api.Exception400;
import shop.mtcoding.blog._core.errors.exception.api.Exception404;
import shop.mtcoding.blog.domain.course.courseteacher.CourseTeacher;
import shop.mtcoding.blog.domain.course.courseteacher.CourseTeacherEnum;
import shop.mtcoding.blog.domain.course.courseteacher.CourseTeacherRepository;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.SubjectRepository;
import shop.mtcoding.blog.domain.user.student.Student;
import shop.mtcoding.blog.domain.user.student.StudentRepository;
import shop.mtcoding.blog.domain.user.teacher.Teacher;
import shop.mtcoding.blog.domain.user.teacher.TeacherRepository;
import shop.mtcoding.blog.web.course.CourseRequest;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CourseService {
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final CourseTeacherRepository courseTeacherRepository;

    @Transactional
    public void 과정등록(CourseRequest.SaveDTO reqDTO) {
        // 1. 메인강사 유효성 검사
        if (reqDTO.getMainTeacherId() == null) {
            throw new Exception400("메인강사는 필수로 등록되어야 합니다.");
        }

        Teacher teacherPS = teacherRepository.findById(reqDTO.getMainTeacherId())
                .orElseThrow(() -> new Exception404("메인강사를 찾을 수 없습니다."));

        // 2. 과정 생성 및 저장 (teacherName 세팅 포함)
        Course coursePS = courseRepository.save(reqDTO.toEntity());

        // 3. 메인강사 등록
        CourseTeacher mainTeacher = CourseTeacher.builder()
                .role(CourseTeacherEnum.MAIN)
                .course(coursePS)
                .teacher(teacherPS)
                .build();
        courseTeacherRepository.save(mainTeacher);

        // 4. 보조강사 등록
        List<Long> subTeacherIds = reqDTO.getSubTeacherIds(); // null 가능성 있음
        if (subTeacherIds != null && !subTeacherIds.isEmpty()) {
            List<CourseTeacher> subTeachers = subTeacherIds.stream()
                    .map(id -> CourseTeacher.builder()
                            .role(CourseTeacherEnum.SUB)
                            .course(coursePS)
                            .teacher(Teacher.builder().id(id).build())
                            .build())
                    .toList();
            courseTeacherRepository.saveAll(subTeachers);
        }
    }


    public CourseModel.Slice 과정목록(Long teacherId, Pageable pageable) {
        Page<Course> coursePG = courseRepository.findAllByTeacherId(teacherId, pageable);
        return new CourseModel.Slice(coursePG);
    }

    public CourseModel.Detail 과정상세_교과목들_학생들(Long courseId) {
        Course coursePS = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));

        List<Subject> subjectListPS = subjectRepository.findAllByCourseId(coursePS.getId());
        List<Student> studentListPS = studentRepository.findAllByCourseId(coursePS.getId());
        return new CourseModel.Detail(coursePS, subjectListPS, studentListPS);
    }

    public CourseModel.Item 과정상세(Long courseId) {
        Course coursePS = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));

        return new CourseModel.Item(coursePS);
    }
}
