package test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import test.model.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

	Store findOneById(Long id);

}
