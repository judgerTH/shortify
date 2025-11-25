package jade.product.shortify.domain.article.repository;

import jade.product.shortify.domain.article.entity.OriginalArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OriginalArticleRepository extends JpaRepository<OriginalArticle, Long> {
    boolean existsByUrl(String url);
}
