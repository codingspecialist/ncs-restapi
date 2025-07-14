package shop.mtcoding.blog.course.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.mtcoding.blog._core.errors.exception.api.Exception404;
import shop.mtcoding.blog.course.adapter.out.external.UserRepositoryAdapter;
import shop.mtcoding.blog.course.application.port.in.CourseUseCase;
import shop.mtcoding.blog.course.application.port.in.dto.CourseCommand;
import shop.mtcoding.blog.course.application.port.in.dto.CourseOutput;
import shop.mtcoding.blog.course.application.port.out.CourseRepositoryPort;
import shop.mtcoding.blog.course.domain.Course;
import shop.mtcoding.blog.course.domain.CourseStudent;
import shop.mtcoding.blog.course.domain.CourseTeacher;
import shop.mtcoding.blog.course.domain.Subject;
import shop.mtcoding.blog.course.domain.enums.CourseTeacherEnum;
import shop.mtcoding.blog.user.domain.User;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CourseService implements CourseUseCase {

    private final CourseRepositoryPort courseRepositoryPort;
    private final UserRepositoryAdapter userRepositoryAdapter;

    @Override
    public CourseOutput.MaxPage 과정목록(Long teacherId, Pageable pageable) {
        Page<Course> coursePG = courseRepositoryPort.findAllByTeacherId(teacherId, pageable);
        return new CourseOutput.MaxPage(coursePG);
    }

    @Override
    public CourseOutput.Max 과정등록(CourseCommand.Save command) {
        User loadUser = userRepositoryAdapter.loadUserByTeacherId(command.mainTeacherId());
        Course savedCourse = courseRepositoryPort.save(Course.create(command));

        CourseTeacher mainTeacher = CourseTeacher.create(savedCourse, loadUser.getTeacher(), CourseTeacherEnum.MAIN);
        savedCourse.addCourseTeacher(mainTeacher);

        // 4. 보조강사 등록
        List<User> subUsers = userRepositoryAdapter.loadUserByTeacherIdIn(command.subTeacherIds());
        subUsers.stream().forEach(user -> {
            CourseTeacher subTeacher = CourseTeacher.create(savedCourse, user.getTeacher(), CourseTeacherEnum.SUB);
            savedCourse.addCourseTeacher(subTeacher);
        });
        return new CourseOutput.Max(savedCourse);
    }

    @Override
    public CourseOutput.Max 과정정보(Long courseId) {
        Course findCourse = courseRepositoryPort.findById(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));

        return new CourseOutput.Max(findCourse);
    }

    @Override
    public CourseOutput.Detail 과정상세(Long courseId) {
        Course findCourse = courseRepositoryPort.findById(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));

        List<Subject> findSubjects = courseRepositoryPort.findAllSubjectsByCourseId(findCourse.getId());
        List<CourseStudent> findStudents = courseRepositoryPort.findAllStudentsByCourseId(findCourse.getId());
        return new CourseOutput.Detail(findCourse, findSubjects, findStudents);
    }
}
