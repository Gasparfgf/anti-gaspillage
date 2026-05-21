package backend.antigasp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.antigasp.model.entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{

	List<Reservation> findByUserId(Long userId);
    List<Reservation> findByOfferId(Long offerId);
}
