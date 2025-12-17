package vn.hoidanit.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.Order;
import vn.hoidanit.jobhunter.domain.OrderResquestDto;
import vn.hoidanit.jobhunter.service.OrderService;

@RestController
public class OrderController {

	private OrderService orderService;
	

	public OrderController(OrderService orderService) {
		super();
		this.orderService = orderService;
	}


	@PostMapping("/order")
	public ResponseEntity<Order> orderProduct(@RequestBody OrderResquestDto orderResquestDto) {

		return ResponseEntity.ok(orderService.orderProduct(orderResquestDto));
	}

}
