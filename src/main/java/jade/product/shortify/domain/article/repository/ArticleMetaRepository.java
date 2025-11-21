package jade.product.shortify.domain.article.repository;

import jade.product.shortify.domain.article.entity.ArticleMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleMetaRepository extends JpaRepository<ArticleMeta, Long> {

    boolean existsByUrl(String url);

    Optional<ArticleMeta> findByUrl(String url);
}
