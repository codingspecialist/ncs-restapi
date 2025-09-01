package shop.mtcoding.blog.course.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.api.Exception404;
import shop.mtcoding.blog.course.adapter.UserRepositoryAdapter;
import shop.mtcoding.blog.course.application.domain.Course;
import shop.mtcoding.blog.course.application.domain.CourseStudent;
import shop.mtcoding.blog.course.application.domain.CourseTeacher;
import shop.mtcoding.blog.course.application.domain.Subject;
import shop.mtcoding.blog.course.application.domain.enums.TeacherType;
import shop.mtcoding.blog.course.application.repository.CourseRepository;
import shop.mtcoding.blog.course.application.repository.CourseStudentRepository;
import shop.mtcoding.blog.course.application.repository.SubjectRepository;
import shop.mtcoding.blog.course.application.service.dto.CourseCommand;
import shop.mtcoding.blog.course.application.service.dto.CourseOutput;
import shop.mtcoding.blog.user.application.domain.User;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepositoryAdapter userRepositoryAdapter;

    public CourseOutput.MaxPage 과정목록(Long teacherId, Pageable pageable) {
        Page<Course> coursePG = courseRepository.findAllByTeacherId(teacherId, pageable);
        return new CourseOutput.MaxPage(coursePG);
    }

    public CourseOutput.Max 과정등록(CourseCommand.Save command) {
        User loadUser = userRepositoryAdapter.loadUserByTeacherId(command.mainTeacherId());
        Course savedCourse = courseRepository.save(Course.create(command));

        CourseTeacher mainTeacher = CourseTeacher.create(savedCourse, loadUser.getTeacher(), TeacherType.MAIN);
        savedCourse.addCourseTeacher(mainTeacher);

        // 4. 보조강사 등록
        List<User> subUsers = userRepositoryAdapter.loadUserByTeacherIdIn(command.subTeacherIds());
        subUsers.stream().forEach(user -> {
            CourseTeacher subTeacher = CourseTeacher.create(savedCourse, user.getTeacher(), TeacherType.SUB);
            savedCourse.addCourseTeacher(subTeacher);
        });
        return new CourseOutput.Max(savedCourse);
    }

    public CourseOutput.Max 과정정보(Long courseId) {
        Course findCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));

        return new CourseOutput.Max(findCourse);
    }

    public CourseOutput.Detail 과정상세(Long courseId) {
        Course findCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));

        List<Subject> findSubjects = subjectRepository.findAllByCourseId(findCourse.getId());
        List<CourseStudent> findStudents = courseStudentRepository.findAllByCourseId(findCourse.getId());
        return new CourseOutput.Detail(findCourse, findSubjects, findStudents);
    }
}
