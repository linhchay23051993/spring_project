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
import vn.hoidanit.jobhunter.repository.CartItemRepository;
import vn.hoidanit.jobhunter.repository.CartRepository;
import vn.hoidanit.jobhunter.repository.ProductRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@Service
public class CartService {
	private CartRepository cartRepository;
	private UserRepository userRepository;
	private ProductRepository productRepository;
	private CartItemRepository cartItemRepository;

	public CartService(CartRepository cartRepository, UserRepository userRepository,
			ProductRepository productRepository, CartItemRepository cartItemRepository) {
		this.cartRepository = cartRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.cartItemRepository = cartItemRepository;
	}

	public void createCart(CartRequestDTO dto) {
		String email = SecurityUtil.getCurrentUserLogin().get();
		User user = userRepository.findByEmail(email);
		Cart cart = new Cart();
		List<CartItem> cartItems = new ArrayList<>();
		for (CartItemDTO item : dto.getItems()) {
			CartItem cartItem = new CartItem();
			Products product = productRepository.findById(item.getProductId()).get();
			cartItem.setProduct(product);
			cartItem.setQuantity(item.getQuantity());
			cartItem.setPrice(product.getPrice());
			cartItem.setTotalPrice(product.getPrice() * item.getQuantity());
			cartItem.setCart(cart);
			cartItems.add(cartItem);
		}
		cart.setStatus(dto.getStatus());
		cart.setUser(user);
		cart.setItems(cartItems);
		cartRepository.save(cart);
	}

	public void updateCart(CartRequestDTO dto) {
		String email = SecurityUtil.getCurrentUserLogin().get();
		User user = userRepository.findByEmail(email);
		Cart cart = cartRepository.findByUserId(user.getId());
		List<CartItem> cartItems = new ArrayList<>();
		for (CartItemDTO item : dto.getItems()) {
			CartItem cartItem = this.cartItemRepository.findByProductId(item.getProductId());
			if (cartItem != null) {
				Products product = productRepository.findById(item.getProductId()).get();
				cartItem.setProduct(product);
				cartItem.setQuantity(item.getQuantity());
				cartItem.setPrice(product.getPrice());
				cartItem.setTotalPrice(product.getPrice() * item.getQuantity());
				cartItem.setCart(cart);
				cartItems.add(cartItem);
			} else {
				cartItem = new CartItem();
				Products product = productRepository.findById(item.getProductId()).get();
				cartItem.setProduct(product);
				cartItem.setQuantity(item.getQuantity());
				cartItem.setPrice(product.getPrice());
				cartItem.setTotalPrice(product.getPrice() * item.getQuantity());
				cartItem.setCart(cart);
				cartItems.add(cartItem);
			}
			cart.setStatus(dto.getStatus());
			cart.setItems(cartItems);
			cartRepository.save(cart);

		}
	}
}
