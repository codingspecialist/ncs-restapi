package shop.mtcoding.blog.webv2.course.subject.element;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.utils.Resp;
import shop.mtcoding.blog.domainv2222222.course.subject.element.SubjectElementService;

import java.util.List;

@RequestMapping("/api/subjects/{subjectId}/elements")
@RequiredArgsConstructor
@Controller
public class SubjectElementController {
    private final SubjectElementService subjectElementService;

    @GetMapping
    public ResponseEntity<?> list(@PathVariable(value = "subjectId") Long subjectId) {
        var modelData = subjectElementService.교과목요소목록(subjectId);
        var respDTO = new SubjectElementResponse.Items(modelData.subject());
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    @PostMapping
    public ResponseEntity<?> save(@PathVariable(value = "subjectId") Long subjectId, @RequestBody List<SubjectElementRequest.Save> reqDTOs) {
        subjectElementService.교과목요소전체등록(subjectId, reqDTOs);
        return ResponseEntity.ok(Resp.ok(null));
    }
}
