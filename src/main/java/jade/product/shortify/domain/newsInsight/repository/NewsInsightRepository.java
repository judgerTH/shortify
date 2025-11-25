package jade.product.shortify.domain.newsInsight.repository;

import jade.product.shortify.domain.newsInsight.entity.NewsInsight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsInsightRepository extends JpaRepository<NewsInsight, Long> {
    Optional<NewsInsight> findFirstByOrderByCreatedAtDesc();
}
