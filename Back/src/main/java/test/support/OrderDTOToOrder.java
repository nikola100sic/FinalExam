package test.support;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import test.model.Orders;
import test.service.OrderService;
import test.service.ProductService;
import test.web.dto.OrderDTO;

@Component
public class OrderDTOToOrder implements Converter<OrderDTO, Orders> {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;

	@Override
	public Orders convert(OrderDTO dto) {
		LocalDateTime date = LocalDateTime.now();
		Orders order;
		if (dto.getId() == null) {
			order = new Orders();
		} else {
			order = orderService.findOne(dto.getId());
		}
		if (order != null) {
			order.setId(dto.getId());
			order.setProduct(productService.findOne(dto.getProductId()));
			order.setDate(date);
			order.setQuantity(dto.getQuantity());
			order.setTotalPrice(productService.findOne(dto.getProductId()).getPrice() * dto.getQuantity());

		}
		return order;
	}
}
