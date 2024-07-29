package test.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import test.model.Orders;
import test.model.Product;
import test.repository.OrderRepository;
import test.repository.ProductRepository;
import test.service.OrderService;

@Service
public class JpaOrderService implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Orders findOne(Long id) {
		return orderRepository.findOneById(id);
	}

	@Override
	public List<Orders> findAll() {
		return orderRepository.findAll();
	}

	@Override
	public Orders save(Orders order) {
		Product product = productRepository.findOneById(order.getProduct().getId());
		Integer availableProduct = product.getAvailable();
		if (order.getQuantity() > availableProduct) {
			return null;
		} else {
			order.setProduct(product);
			Integer substitutionProduct = availableProduct - order.getQuantity();
			product.setAvailable(substitutionProduct);
			productRepository.save(product);

			return orderRepository.save(order);
		}
	}

	@Override
	public Orders delete(Long id) {
		Orders order = findOne(id);
		if (order != null) {
			Product product = productRepository.findOneById(order.getProduct().getId());

			Integer availableProduct = product.getAvailable() + order.getQuantity();
			product.setAvailable(availableProduct);
			productRepository.save(product);

			order.getProduct().getOrders().remove(order);
			orderRepository.delete(order);
			return order;
		}
		return null;
	}

	@Override
	public List<Orders> find(LocalDateTime dateFrom, LocalDateTime dateTo) {
		return orderRepository.search(dateFrom, dateTo);
	}

}
