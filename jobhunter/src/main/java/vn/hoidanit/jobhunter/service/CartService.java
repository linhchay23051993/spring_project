package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Cart;
import vn.hoidanit.jobhunter.domain.CartItem;
import vn.hoidanit.jobhunter.domain.CartItemDTO;
import vn.hoidanit.jobhunter.domain.CartRequestDTO;
import vn.hoidanit.jobhunter.domain.Products;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.CartRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@Service
public class CartService {
	private CartRepository cartRepository;
	private UserRepository userRepository;

	public CartService(CartRepository cartRepository, UserRepository userRepository) {
		this.cartRepository = cartRepository;
		this.userRepository = userRepository;
	}
	public void createCart(CartRequestDTO dto) {
		String email = SecurityUtil.getCurrentUserLogin().get();
		System.out.print("LinhChay: ---" + email);
		User user = userRepository.findByEmail(email);
		Cart cart = new Cart();
		List<CartItem> cartItems = new ArrayList<>();
		for (CartItemDTO item : dto.getItems()) {
			CartItem cartItem = new CartItem();
			Products product = new Products();
			product.setId(item.getProductId());
			cartItem.setProduct(product);
			cartItem.setQuantity(item.getQuantity());
			cartItem.setPrice(item.getPrice());
			cartItem.setTotalPrice(item.getTotalPrice());
			cartItem.setCart(cart);
			cartItems.add(cartItem);
		}
		cart.setStatus(dto.getStatus());
		cart.setUser(user);
		cart.setItems(cartItems);
		cartRepository.save(cart);
		
	}
}
