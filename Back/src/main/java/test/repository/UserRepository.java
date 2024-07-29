package test.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import test.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findFirstByUsername(String username);
}
