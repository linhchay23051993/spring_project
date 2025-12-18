package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.hoidanit.jobhunter.domain.Cart;
import vn.hoidanit.jobhunter.domain.CartItem;
import vn.hoidanit.jobhunter.domain.Products;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.request.CartItemDTO;
import vn.hoidanit.jobhunter.domain.request.CartRequestDTO;
import vn.hoidanit.jobhunter.repository.CartItemRepository;
import vn.hoidanit.jobhunter.repository.CartRepository;
import vn.hoidanit.jobhunter.repository.ProductRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.service.error.IdInvalidException;
import vn.hoidanit.jobhunter.service.error.OverQuanlityException;
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

	public void updateCart(CartRequestDTO dto) throws OverQuanlityException {
		String email = SecurityUtil.getCurrentUserLogin().get();
		User user = userRepository.findByEmail(email);
		Cart cart = cartRepository.findByUserId(user.getId());
		List<CartItem> cartItems = new ArrayList<>();
		for (CartItemDTO item : dto.getItems()) {
			CartItem cartItem = this.cartItemRepository.findByProductId(item.getProductId());
			if (cartItem != null) {
				Products product = productRepository.findById(item.getProductId()).get();
				cartItem.setProduct(product);
				if (item.getQuantity() > product.getQuantity()) {
					throw new OverQuanlityException("HANG KHONG CON DU SO LUONG");
				} else {
					cartItem.setQuantity(item.getQuantity());
				}
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
	@Transactional
	public Cart getCart() {
		String email = SecurityUtil.getCurrentUserLogin().get();
		User user = userRepository.findByEmail(email);
		Cart cart = cartRepository.findByUserId(user.getId());
		List<CartItem> cartItems = cart.getItems();
		for (CartItem item : cartItems) {
			Products product = productRepository.findById(item.getProduct().getId()).get();
			if(item.getPrice() != product.getPrice()) {
				item.setPrice(product.getPrice());
				item.setTotalPrice(product.getPrice()* item.getQuantity());
			}
		}
		cart.setItems(cartItems);
		cartRepository.save(cart);
		return cartRepository.findByUserId(user.getId());
	}

	@Transactional
	public void deleteCartItem(long productId) {
		String email = SecurityUtil.getCurrentUserLogin().get();
		User user = userRepository.findByEmail(email);
		Cart cart = cartRepository.findByUserId(user.getId());
		this.cartItemRepository.deleteByCartIdAndProductId(cart.getId(), productId);
	}
	
	@Transactional
	public void deleteCart(long cartId) throws IdInvalidException {
		// Lấy user hiện tại từ JWT
		String email = SecurityUtil.getCurrentUserLogin().get();
		User user = userRepository.findByEmail(email);
		
		// Tìm cart theo cartId
		Cart cart = cartRepository.findById(cartId)
			.orElse(null);
		
		// Kiểm tra cart có tồn tại không
		if (cart == null) {
			throw new IdInvalidException("Gio hang khong ton tai");
		}
		
		// Kiểm tra ownership: cart phải thuộc về user hiện tại
		if (cart.getUser() == null || cart.getUser().getId() != user.getId()) {
			throw new IdInvalidException("Ban khong co quyen xoa gio hang nay");
		}
		
		// Xóa cart nếu ownership hợp lệ
		this.cartRepository.deleteById(cartId);
	}
	
}
