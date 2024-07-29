package test.web.controller;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import test.model.Orders;
import test.service.OrderService;
import test.support.OrderDTOToOrder;
import test.support.OrderToOrderDTO;
import test.web.dto.OrderDTO;

@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderToOrderDTO toOrderDTO;

	@Autowired
	private OrderDTOToOrder toOrder;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	public ResponseEntity<List<OrderDTO>> getAll(@RequestParam(required = false) String dateFromParam,
			@RequestParam(required = false) String dateToParam) {
		List<Orders> orders;
		try {
			LocalDateTime dateFrom = null;
			LocalDateTime dateTo = null;

			if (dateFromParam != null) {
				dateFrom = getLocalDateTime(dateFromParam);
			}
			if (dateToParam != null) {
				dateTo = getLocalDateTime(dateToParam);
			}

			orders = orderService.find(dateFrom, dateTo);
		} catch (Exception e) {
			orders = orderService.findAll();
		}

		return new ResponseEntity<>(toOrderDTO.convert(orders), HttpStatus.OK);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderDTO> create(@Valid @RequestBody OrderDTO orderDTO) {

		Orders order = toOrder.convert(orderDTO);
		if (order.getProduct() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Orders savedOrder = orderService.save(order);

		return new ResponseEntity<>(toOrderDTO.convert(savedOrder), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {

		Orders order = orderService.findOne(id);
		LocalDateTime orderDateAndTime = order.getDate();
		LocalDateTime currentDate = LocalDateTime.now();

		Duration duration = Duration.between(orderDateAndTime, currentDate);

		long hours = duration.toHours();

		if (hours > 12) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Orders deletedOrder = orderService.delete(id);

		if (deletedOrder != null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	private LocalDateTime getLocalDateTime(String dateTime) throws DateTimeException {
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return LocalDateTime.parse(dateTime, formater);
	}

}
