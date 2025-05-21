package shop.mtcoding.blog.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/api/teacher/document/course/{courseId}/subject/{subjectId}/no1")
    public String no1(@PathVariable("courseId") Long courseId, @PathVariable("subjectId") Long subjectId, Model model) {
        DocumentResponse.No1DTO respDTO = documentService.no1(subjectId);
        model.addAttribute("model", respDTO);
        return "document/no1";
    }

    @GetMapping("/api/teacher/document/course/{courseId}/subject/{subjectId}/no2")
    public String no2(@PathVariable("courseId") Long courseId, @PathVariable("subjectId") Long subjectId, Model model) {
        DocumentResponse.No2DTO respDTO = documentService.no2(courseId, subjectId);
        model.addAttribute("model", respDTO);
        return "document/no2";
    }

    @GetMapping("/api/teacher/document/course/{courseId}/subject/{subjectId}/no3")
    public String no3(@PathVariable("courseId") Long courseId, @PathVariable("subjectId") Long subjectId, Model model) {
        DocumentResponse.No3DTO respDTO = documentService.no3(courseId, subjectId);
        model.addAttribute("model", respDTO);
        return "document/no3";
    }

    @GetMapping("/api/teacher/document/course/{courseId}/subject/{subjectId}/no4")
    public String no4(@PathVariable("courseId") Long courseId, @PathVariable("subjectId") Long subjectId, Model model, @RequestParam(value = "currentIndex", defaultValue = "0") Integer currentIndex) {
        DocumentResponse.No4DTO respDTO = documentService.no4(courseId, subjectId, currentIndex);
        model.addAttribute("model", respDTO);
        return "document/no4";
    }


    @GetMapping("/api/teacher/document/course/{courseId}/subject/{subjectId}/no5")
    public String no5(@PathVariable("courseId") Long courseId, @PathVariable("subjectId") Long subjectId, Model model) {
        DocumentResponse.No5DTO respDTO = documentService.no5(courseId, subjectId);
        model.addAttribute("model", respDTO);
        return "document/no5";
    }

    @GetMapping("/api/teacher/document/course")
    public String course(Model model) {

        List<DocumentResponse.CourseDTO> respDTOs = documentService.과정목록();
        model.addAttribute("models", respDTOs);
        return "document/course-list";
    }

    @GetMapping("/api/teacher/document/course/{courseId}/subject")
    public String subject(@PathVariable("courseId") Long courseId, Model model) {

        List<DocumentResponse.SubjectDTO> respDTOs = documentService.교과목목록(courseId);
        model.addAttribute("models", respDTOs);
        return "document/subject-list";
    }

    @GetMapping("/api/teacher/document/course/{courseId}/subject/{subjectId}")
    public String subject(@PathVariable("courseId") Long courseId, @PathVariable("subjectId") Long subjectId, Model model) {
        model.addAttribute("courseId", courseId);
        model.addAttribute("subjectId", subjectId);
        return "document/document-list";
    }
}
