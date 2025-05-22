package shop.mtcoding.blog.course;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.Exception400;
import shop.mtcoding.blog._core.errors.exception.Exception404;
import shop.mtcoding.blog.course.courseteacher.CourseTeacher;
import shop.mtcoding.blog.course.courseteacher.CourseTeacherEnum;
import shop.mtcoding.blog.course.courseteacher.CourseTeacherRepository;
import shop.mtcoding.blog.course.student.Student;
import shop.mtcoding.blog.course.student.StudentRepository;
import shop.mtcoding.blog.course.subject.Subject;
import shop.mtcoding.blog.course.subject.SubjectRepository;
import shop.mtcoding.blog.user.teacher.Teacher;
import shop.mtcoding.blog.user.teacher.TeacherRepository;

import java.util.ArrayList;
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
        // 1. 과정 등록 (메인강사 이름 전달)
        Long mainTeacherId = reqDTO.getMainTeacherId();
        if (mainTeacherId == null) throw new Exception400("메인강사는 필수로 등록되어야 합니다");
        Teacher teacherPS = teacherRepository.findById(reqDTO.getMainTeacherId())
                .orElseThrow(() -> new Exception404("메인강사를 찾을 수 없습니다"));

        Course coursePS = courseRepository.save(reqDTO.toEntity(teacherPS.getName()));

        // 2. 과정별 강사 등록 (메인강사)
        CourseTeacher mainTeacher = CourseTeacher.builder()
                .role(CourseTeacherEnum.MAIN)
                .course(coursePS)
                .teacher(teacherPS)
                .build();
        courseTeacherRepository.save(mainTeacher);

        // 3. 과정별 강사들 등록 (보조강사들)
        List<CourseTeacher> subTeachers = new ArrayList<>();
        List<Long> subTeacherIds = reqDTO.getSubTeacherIds() == null ? new ArrayList<>() : reqDTO.getSubTeacherIds();

        subTeacherIds.forEach(subTeacherId -> {
            CourseTeacher subTeacher = CourseTeacher.builder()
                    .role(CourseTeacherEnum.SUB)
                    .course(coursePS)
                    .teacher(Teacher.builder().id(subTeacherId).build())
                    .build();
            subTeachers.add(subTeacher);
        });
        courseTeacherRepository.saveAll(subTeachers);
    }

    public CourseResponse.PagingDTO 과정목록(Pageable pageable) {
        Page<Course> paging = courseRepository.findAll(pageable);
        return new CourseResponse.PagingDTO(paging);
    }

    public CourseResponse.DetailDTO 과정상세(Long courseId) {
        Course coursePS = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));

        List<Subject> subjectListPS = subjectRepository.findByCourseId(coursePS.getId());
        List<Student> studentListPS = studentRepository.findByCourseId(coursePS.getId());
        return new CourseResponse.DetailDTO(coursePS, subjectListPS, studentListPS);
    }
}
