package shop.mtcoding.blog.user.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.mtcoding.blog._core.utils.Resp;
import shop.mtcoding.blog.domainv2222222.user.teacher.TeacherModel;
import shop.mtcoding.blog.domainv2222222.user.teacher.TeacherService;
import shop.mtcoding.blog.webv2.teacher.TeacherResponse;

import java.util.List;

@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@RestController
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public ResponseEntity<?> items() {
        TeacherModel.Items items = teacherService.강사목록();
        List<TeacherResponse.DTO> respDTOs = items.teachers().stream()
                .map(TeacherResponse.DTO::new)
                .toList();
        return ResponseEntity.ok(Resp.ok(respDTOs));
    }
}
