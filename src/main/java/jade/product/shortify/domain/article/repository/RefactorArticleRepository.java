package jade.product.shortify.domain.article.repository;

import jade.product.shortify.domain.article.entity.RefactorArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefactorArticleRepository extends JpaRepository<RefactorArticle, Long> {
}
