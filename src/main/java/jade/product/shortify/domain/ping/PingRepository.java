package jade.product.shortify.domain.ping;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PingRepository extends JpaRepository<PingEntity, Long> {
}
