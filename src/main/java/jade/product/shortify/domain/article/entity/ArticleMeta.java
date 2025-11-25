package jade.product.shortify.domain.article.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String press;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;

    private LocalDateTime publishedAt;
    private LocalDateTime collectedAt;

    private String category;
    private String thumbnailUrl;

    public static ArticleMeta fromOriginal(OriginalArticle original) {
        return ArticleMeta.builder()
                .press(original.getPress())
                .title(original.getTitle())
                .url(original.getUrl())
                .publishedAt(original.getPublishedAt())
                .collectedAt(LocalDateTime.now())
                .build();
    }
}
