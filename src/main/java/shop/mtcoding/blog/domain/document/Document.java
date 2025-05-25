package shop.mtcoding.blog.domain.document;

import java.time.LocalDateTime;

// 추후 Entity로 최종 보고서를 저장해둬야 함 (매번 조회할 수 없음)
// 저장시에는 json으로 저장 (저장되면 캐싱해야함)
// 수정이 필요하면 전체 json을 덮어쓰기
// Document가 존재하는지 여부는 필드에 최소 2개 필요
public class Document {
    private Long id;
    private Integer type; // 보고서 타입 (no1, no2 ~ no7)
    private String snapshot; // 보고서 내용 한방 저장
    private Long subjectId; // 캐싱 용도
    private LocalDateTime createdAt;
}
