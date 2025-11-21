package jade.product.shortify.domain.ping.repository;

import jade.product.shortify.domain.ping.entity.PingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PingRepository extends JpaRepository<PingEntity, Long> {
}
