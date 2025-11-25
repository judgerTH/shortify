package jade.product.shortify.domain.newsInsight.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class NewsInsight {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int tension;
    private int positivity;
    private int stability;

    @Column(columnDefinition = "TEXT")
    private String summary;

    public NewsInsight(int tension, int positivity, int stability, String summary) {
        this.tension = tension;
        this.positivity = positivity;
        this.stability = stability;
        this.summary = summary;
    }
}

