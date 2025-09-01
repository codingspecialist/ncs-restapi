package shop.mtcoding.blog.document.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.mtcoding.blog.document.application.service.DocumentService;
import shop.mtcoding.blog.user.application.domain.User;

@RequestMapping("/api/documents")
@RequiredArgsConstructor
@RestController
public class DocumentController {

    private final DocumentService documentService;
    private final HttpSession session;

    @GetMapping("/courses")
    public ResponseEntity<?> course(
            @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = "id") Pageable pageable) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var respDTOs = documentService.과정목록(sessionUser, pageable);
        return ResponseEntity.ok(respDTOs);
    }

    @GetMapping("/courses/{courseId}/subjects")
    public ResponseEntity<?> subject(@PathVariable("courseId") Long courseId) {
        var respDTOs = documentService.교과목목록(courseId);
        return ResponseEntity.ok(respDTOs);
    }

    @GetMapping("/subjects/{subjectId}")
    public ResponseEntity<?> subjectDetail(@PathVariable("subjectId") Long subjectId) {
        return ResponseEntity.ok(Map.of("subjectId", subjectId));
    }

    @GetMapping("/subjects/{subjectId}/no1")
    public ResponseEntity<?> no1(@PathVariable("subjectId") Long subjectId) {
        var respDTO = documentService.no1(subjectId);
        return ResponseEntity.ok(respDTO);
    }

    @GetMapping("/subjects/{subjectId}/no2")
    public ResponseEntity<?> no2(@PathVariable("subjectId") Long subjectId)
            throws JsonProcessingException {
        var respDTO = documentService.no2(subjectId);
        return ResponseEntity.ok(respDTO);
    }

    @GetMapping("/subjects/{subjectId}/no3")
    public ResponseEntity<?> no3(@PathVariable("subjectId") Long subjectId) {
        var respDTO = documentService.no3(subjectId);
        return ResponseEntity.ok(respDTO);
    }

    @GetMapping("/subjects/{subjectId}/no4")
    public ResponseEntity<?> no4(@PathVariable("subjectId") Long subjectId,
            @RequestParam(value = "currentIndex", defaultValue = "0") Integer currentIndex) {
        var respDTO = documentService.no4(subjectId, currentIndex);
        return ResponseEntity.ok(respDTO);
    }

    @GetMapping("/subjects/{subjectId}/no5")
    public ResponseEntity<?> no5(@PathVariable("subjectId") Long subjectId) {
        var respDTO = documentService.no5(subjectId);
        return ResponseEntity.ok(respDTO);
    }
}