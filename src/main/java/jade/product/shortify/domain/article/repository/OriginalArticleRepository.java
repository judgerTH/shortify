package jade.product.shortify.domain.article.repository;

import jade.product.shortify.domain.article.entity.OriginalArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OriginalArticleRepository extends JpaRepository<OriginalArticle, Long> {

    Optional<OriginalArticle> findByUrl(String url);  // 중복 기사 방지용
}
