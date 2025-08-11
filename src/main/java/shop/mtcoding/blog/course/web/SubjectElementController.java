package shop.mtcoding.blog.course.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.utils.Resp;
import shop.mtcoding.blog.course.application.port.in.dto.SubjectElementCommand;
import shop.mtcoding.blog.course.application.service.SubjectElementService;
import shop.mtcoding.blog.course.web.dto.SubjectElementRequest;
import shop.mtcoding.blog.course.web.dto.SubjectElementResponse;

import java.util.List;

@RequestMapping("/api/subjects/{subjectId}/elements")
@RequiredArgsConstructor
@Controller
public class SubjectElementController {
    private final SubjectElementService subjectElementService;

    @GetMapping
    public ResponseEntity<?> list(@PathVariable(value = "subjectId") Long subjectId) {
        var modelData = subjectElementService.교과목요소목록(subjectId);
        var respDTO = new SubjectElementResponse.MaxList(modelData.subject());
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    @PostMapping
    public ResponseEntity<?> save(@PathVariable(value = "subjectId") Long subjectId, @RequestBody List<SubjectElementRequest.Save> reqDTOs) {

        List<SubjectElementCommand.Save> commands = reqDTOs.stream()
                .map(dto -> dto.toCommand())
                .toList(); // Java 16 이상

        subjectElementService.교과목요소전체등록(subjectId, commands);
        return ResponseEntity.ok(Resp.ok(null));
    }
}
