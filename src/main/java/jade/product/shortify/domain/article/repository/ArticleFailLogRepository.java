package jade.product.shortify.domain.article.repository;

import jade.product.shortify.domain.article.entity.ArticleFailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleFailLogRepository extends JpaRepository<ArticleFailLog, Long> {
}
