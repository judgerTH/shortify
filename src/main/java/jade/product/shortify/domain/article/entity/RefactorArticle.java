package jade.product.shortify.domain.article.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "refactor_article")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefactorArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연관관계: 하나의 기사 원문 → 여러 개 요약 버전
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_article_id", nullable = false)
    private OriginalArticle originalArticle;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String summaryText; // 요약 결과

    private String model; // gemini-2.5-flash 등

    @CreationTimestamp
    private LocalDateTime createdAt;
}
