package shop.mtcoding.blog.exam.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.utils.Resp;
import shop.mtcoding.blog.exam.application.domain.enums.EvaluationWay;
import shop.mtcoding.blog.exam.application.service.PaperService;
import shop.mtcoding.blog.exam.web.dto.PaperRequest;
import shop.mtcoding.blog.exam.web.dto.PaperResponse;

@RequestMapping("/api/papers")
@RequiredArgsConstructor
@Controller
public class PaperController {
    private final PaperService paperService;

    // @SessionUser User sessionUser

    // 시험지목록(교과목별)
    @GetMapping
    public ResponseEntity<?> list(Long subjectId) { // /api/papers?subjectId=1
        var respDTO = paperService.교과목별시험지목록(subjectId);
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    // 시험지상세
    @GetMapping("/{paperId}")
    public ResponseEntity<?> detail(@PathVariable("paperId") Long paperId) {
        var detail = paperService.시험지상세(paperId);

        if (detail.paper().getEvaluationWay() == EvaluationWay.MCQ) {
            var respDTO = PaperResponse.McqDetail.from(detail.paper(), detail.questions());
            return ResponseEntity.ok(Resp.ok(respDTO));
        } else {
            var respDTO = PaperResponse.RubricDetail.from(detail.paper(), detail.questions());
            return ResponseEntity.ok(Resp.ok(respDTO));
        }
    }

    // 시험지등록(교과목별)
    @PostMapping
    public ResponseEntity<?> save(@RequestBody PaperRequest.Save reqDTO) {
        paperService.시험지등록(reqDTO);
        return ResponseEntity.ok(Resp.ok(null));
    }

    // 다음 문제 준비
    @GetMapping("/{paperId}/next-question")
    public ResponseEntity<?> questionSaveForm(@PathVariable(name = "paperId") Long paperId) {
        var output = paperService.다음문제준비(paperId);
        var respDTO = output;
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    // 문제등록
    @PostMapping("/{paperId}/questions")
    public ResponseEntity<?> questionSave(@PathVariable("paperId") Long paperId,
            @RequestBody PaperRequest.QuestionSave reqDTO) {
        paperService.문제등록(paperId, reqDTO);
        return ResponseEntity.ok(Resp.ok(null));
    }

}