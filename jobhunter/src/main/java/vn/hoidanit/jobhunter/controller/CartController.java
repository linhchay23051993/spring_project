package vn.hoidanit.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.Cart;
import vn.hoidanit.jobhunter.domain.CartRequestDTO;
import vn.hoidanit.jobhunter.service.CartService;
import vn.hoidanit.jobhunter.service.error.IdInvalidException;
import vn.hoidanit.jobhunter.service.error.OverQuanlityException;

@RestController
public class CartController {
	private CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@PostMapping("/cart")
	public ResponseEntity<String> createCart(@RequestBody CartRequestDTO dto) {
		this.cartService.createCart(dto);
		return ResponseEntity.ok("Create OK");
	}

	@PutMapping("/cart")
	public ResponseEntity<String> updateCart(@RequestBody CartRequestDTO dto) throws OverQuanlityException {
		this.cartService.updateCart(dto);
		return ResponseEntity.ok("Update OK");
	}
	
	@GetMapping("/cart")
	public ResponseEntity<Cart> getCart() {
		return ResponseEntity.ok(cartService.getCart());
	}
	
	@DeleteMapping("/cart/{id}")
	public ResponseEntity<String> deleteCartItem(@PathVariable ("id") long id) {
		cartService.deleteCartItem(id);
		return ResponseEntity.ok("Da xoa khoi gio hang");
	}
	
	@DeleteMapping("/cart-delete/{id}")
	public ResponseEntity<String> deleteCart(@PathVariable ("id") long id) throws IdInvalidException {
		cartService.deleteCart(id);
		return ResponseEntity.ok("Da xoa gio hang");
	}
	
	
}
