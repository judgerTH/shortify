package jade.product.shortify.domain.article.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "article_meta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 네이버에서 가져온 원 제목 (원문 제목)
    @Column(nullable = false)
    private String title;

    // 언론사 이름 (연합뉴스, KBS 뉴스, MBC, 경기일보 등)
    @Column(nullable = false)
    private String press;

    // 원문 URL (네이버 기사 or 언론사 직링크)
    @Column(nullable = false, unique = true)
    private String url;

    // 썸네일 이미지 등이 필요하면 추가
    private String thumbnailUrl;

    // 카테고리(정치/사회/경제...) 필요하면 나중에 확장
    private String category;

    // 기사 발행 시간 (언론사 기준)
    private LocalDateTime publishedAt;

    // 리스트에서 처음 발견한 시간
    @CreationTimestamp
    private LocalDateTime collectedAt;
}
