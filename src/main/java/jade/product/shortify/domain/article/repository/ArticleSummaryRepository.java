package jade.product.shortify.domain.article.repository;

import jade.product.shortify.domain.article.entity.ArticleSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface ArticleSummaryRepository extends JpaRepository<ArticleSummary, Long> {

    @Query("""
        SELECT s FROM ArticleSummary s
        WHERE s.createdAt >= :start
        ORDER BY s.createdAt DESC
        """)
    List<ArticleSummary> findTodaySummaries(LocalDateTime start, Pageable pageable);

    List<ArticleSummary> findTop30ByCreatedAtBetween(LocalDateTime localDateTime, LocalDateTime localDateTime1);
}
