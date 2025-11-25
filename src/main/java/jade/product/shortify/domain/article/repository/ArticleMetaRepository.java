package jade.product.shortify.domain.article.repository;

import jade.product.shortify.domain.article.entity.ArticleMeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleMetaRepository extends JpaRepository<ArticleMeta, Long> {
}
