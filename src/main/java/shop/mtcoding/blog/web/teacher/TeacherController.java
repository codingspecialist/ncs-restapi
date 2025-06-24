package shop.mtcoding.blog.web.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.mtcoding.blog.core.utils.Resp;
import shop.mtcoding.blog.domain.user.teacher.TeacherModel;
import shop.mtcoding.blog.domain.user.teacher.TeacherService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/api/teachers")
    public ResponseEntity<?> items() {
        TeacherModel.Items items = teacherService.강사목록();
        List<TeacherResponse.DTO> respDTOs = items.teachers().stream()
                .map(TeacherResponse.DTO::new)
                .toList();
        return ResponseEntity.ok(Resp.ok(respDTOs));
    }
}
