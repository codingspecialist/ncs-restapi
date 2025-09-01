package shop.mtcoding.blog.course.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.utils.Resp;
import shop.mtcoding.blog.course.application.service.SubjectElementService;

import shop.mtcoding.blog.course.web.dto.SubjectElementRequest;
import shop.mtcoding.blog.course.web.dto.SubjectElementResponse;

import java.util.List;

@RequestMapping("/api/subjects/{subjectId}/elements")
@RequiredArgsConstructor
@RestController
public class SubjectElementController {
    private final SubjectElementService subjectElementService;

    @GetMapping
    public ResponseEntity<?> list(@PathVariable(value = "subjectId") Long subjectId) {
        var respDTO = subjectElementService.교과목요소목록(subjectId);
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    @PostMapping
    public ResponseEntity<?> save(@PathVariable(value = "subjectId") Long subjectId,
            @RequestBody List<SubjectElementRequest.Save> reqDTOs) {
        subjectElementService.교과목요소전체등록(subjectId, reqDTOs);
        return ResponseEntity.ok(Resp.ok(null));
    }
}
