package jade.product.shortify.domain.article.repository;

import jade.product.shortify.domain.article.entity.ArticleSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
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
    @Query("""
    SELECT s 
    FROM ArticleSummary s 
    JOIN FETCH s.meta m
    WHERE s.createdAt BETWEEN :start AND :end
    ORDER BY s.createdAt DESC
    """)
    List<ArticleSummary> findTop30WithMeta(LocalDateTime start, LocalDateTime end);

    Page<ArticleSummary> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

}
