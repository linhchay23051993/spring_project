package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.hoidanit.jobhunter.domain.Cart;
import vn.hoidanit.jobhunter.domain.CartItem;
import vn.hoidanit.jobhunter.domain.Order;
import vn.hoidanit.jobhunter.domain.OrderItem;
import vn.hoidanit.jobhunter.domain.OrderItemDto;
import vn.hoidanit.jobhunter.domain.OrderResquestDto;
import vn.hoidanit.jobhunter.domain.Products;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.CartItemRepository;
import vn.hoidanit.jobhunter.repository.CartRepository;
import vn.hoidanit.jobhunter.repository.OrderRepository;
import vn.hoidanit.jobhunter.repository.ProductRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@Service
public class OrderService {
	private OrderRepository orderRepository;
	private CartRepository cartRepository;
	private UserRepository userRepository;
	private ProductRepository productRepository;
	private CartItemRepository cartItemRepository;

	public OrderService(OrderRepository orderRepository, CartRepository cartRepository, UserRepository userRepository,
			ProductRepository productRepository, CartItemRepository cartItemRepository) {
		this.orderRepository = orderRepository;
		this.cartRepository = cartRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.cartItemRepository = cartItemRepository;
	}

	@Transactional
	public Order orderProduct(OrderResquestDto dto) {
		String email = SecurityUtil.getCurrentUserLogin().get();
		User user = userRepository.findByEmail(email);
		Order order = new Order();
		List<OrderItem> orderItems = new ArrayList<>();
		List<Long> listId = new ArrayList<>();
		int allPrice = 0;
		for (OrderItemDto item : dto.getItems()) {
			CartItem cartItem = this.cartItemRepository.findById(item.getId()).get();
			listId.add(item.getId());
			OrderItem orderItem = new OrderItem();
			orderItem.setProductOder(cartItem.getProduct());
			orderItem.setPrice(cartItem.getPrice());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setTotalPrice(cartItem.getTotalPrice());
			orderItem.setOrder(order);
			orderItems.add(orderItem);
			allPrice += cartItem.getTotalPrice();
		}
		order.setItems(orderItems);
		order.setAddress(user.getAddress());
		order.setUserOrder(user);
		order.setAllPrice(allPrice);
		Order orderSave = this.orderRepository.save(order);
		this.cartItemRepository.deleteByIdIn(listId);
		return orderSave;
	}
}
