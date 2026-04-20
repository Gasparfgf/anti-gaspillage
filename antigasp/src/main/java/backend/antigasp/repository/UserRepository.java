package backend.antigasp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.antigasp.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}
