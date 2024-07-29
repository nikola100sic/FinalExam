package test.service;

import java.time.LocalDateTime;
import java.util.List;

import test.model.Orders;

public interface OrderService {

	Orders findOne(Long id);

	List<Orders> findAll();

	Orders save(Orders order);

	Orders delete(Long id);

	List<Orders> find(LocalDateTime dateFrom, LocalDateTime dateTo);

}
