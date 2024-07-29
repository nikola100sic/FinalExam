package test.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import test.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	Product findOneById(Long id);

	@Query("SELECT p FROM Product p WHERE" + "(:storeId IS NULL OR p.store.id = :storeId) AND"
			+ "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND"
			+ "(:priceFrom IS NULL OR p.price >= :priceFrom) AND " + "(:priceTo IS NULL OR p.price <= :priceTo)")
	Page<Product> search(@Param("storeId") Long storeId, @Param("name") String name,
			@Param("priceFrom") Double priceFrom, @Param("priceTo") Double priceTo, Pageable pageable);

}
