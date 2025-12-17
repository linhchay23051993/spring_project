package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.CartItem;
import vn.hoidanit.jobhunter.repository.CartItemRepository;

@Service
public class CartItemService {
	private CartItemRepository cartItemRepository;

	public CartItemService(CartItemRepository cartItemRepository) {
		this.cartItemRepository = cartItemRepository;
	}

	public CartItem findByProductId(long id) {
		return cartItemRepository.findByProductId(id);
	}

}
