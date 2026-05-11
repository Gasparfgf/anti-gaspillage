package backend.antigasp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.antigasp.model.entity.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

	List<Offer> findByMerchantId(Long merchantId);
}
