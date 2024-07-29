package test.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import test.model.Orders;
import test.web.dto.OrderDTO;

@Component
public class OrderToOrderDTO implements Converter<Orders, OrderDTO> {

	@Override
	public OrderDTO convert(Orders order) {
		OrderDTO dto = new OrderDTO();
		dto.setId(order.getId());
		dto.setQuantity(order.getQuantity());
		dto.setTotalPrice(order.getTotalPrice());
		dto.setDate(order.getDate().toString());
		dto.setProductId(order.getProduct().getId());
		dto.setProductName(order.getProduct().getName());
		return dto;
	}

	public List<OrderDTO> convert(List<Orders> orders) {
		List<OrderDTO> ordersDTO = new ArrayList<>();

		for (Orders order : orders) {
			ordersDTO.add(convert(order));
		}

		return ordersDTO;
	}
}
