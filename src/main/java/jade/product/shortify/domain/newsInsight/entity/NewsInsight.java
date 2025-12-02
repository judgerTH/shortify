package jade.product.shortify.domain.newsInsight.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsInsight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int tension;
    private int positivity;
    private int stability;

    @Column(columnDefinition = "TEXT")
    private String summary;

    private LocalDateTime createdAt;

    public static NewsInsight create(int tension, int positivity, int stability, String summary) {
        return NewsInsight.builder()
                .tension(tension)
                .positivity(positivity)
                .stability(stability)
                .summary(summary)
                .createdAt(LocalDateTime.now())
                .build();
    }
}

