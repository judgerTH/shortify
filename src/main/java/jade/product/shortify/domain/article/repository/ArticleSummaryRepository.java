package jade.product.shortify.domain.article.repository;

import jade.product.shortify.domain.article.entity.ArticleMeta;
import jade.product.shortify.domain.article.entity.ArticleSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleSummaryRepository extends JpaRepository<ArticleSummary, Long> {

    Optional<ArticleSummary> findByArticleMeta(ArticleMeta articleMeta);
}
