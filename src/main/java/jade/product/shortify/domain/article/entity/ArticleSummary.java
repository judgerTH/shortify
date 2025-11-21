package jade.product.shortify.domain.article.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "article_summary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 메타(원문 기사)에 대한 요약인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_meta_id", nullable = false)
    private ArticleMeta articleMeta;

    // 요약된 제목
    @Column(nullable = false)
    private String summaryTitle;

    // 요약된 본문 (LLM 결과)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String summaryContent;

    // 키워드, 태그 등 (선택)
    private String keywords;

    // 사용한 모델 이름 (gemini-2.5-flash 등)
    private String modelName;

    // 생성 시각
    @CreationTimestamp
    private LocalDateTime createdAt;
}
