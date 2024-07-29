package test.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import test.model.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

	Orders findOneById(Long id);

	@Query("SELECT o FROM Orders o WHERE" + "(:dateFrom IS NULL OR o.date >= :dateFrom) AND"
			+ "(:dateTo IS NULL OR o.date <= :dateTo)")
	List<Orders> search(@Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo);

}
