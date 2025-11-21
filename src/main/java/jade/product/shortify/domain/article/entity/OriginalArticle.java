package jade.product.shortify.domain.article.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "original_article")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OriginalArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String press;          // 언론사 이름
    private String url;

    private LocalDateTime publishedAt; // 기사 작성일

    @CreationTimestamp
    private LocalDateTime crawledAt;   // 크롤링 시각
}
