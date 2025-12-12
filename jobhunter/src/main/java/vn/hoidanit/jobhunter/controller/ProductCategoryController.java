package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.ProductCategory;
import vn.hoidanit.jobhunter.service.ProductCategoryService;

@RestController
public class ProductCategoryController {
	private ProductCategoryService productCategoryService;

	public ProductCategoryController(ProductCategoryService productCategoryService) {
		this.productCategoryService = productCategoryService;
	}

	@PostMapping("/product-category")
	public ResponseEntity<ProductCategory> createProductCategory(@RequestBody ProductCategory category) {
		return ResponseEntity.ok(productCategoryService.createProductCategory(category));
	}

	
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@GetMapping("/product-category")
	public ResponseEntity<List<ProductCategory>> findAllProductCategory() {
		return ResponseEntity.ok(productCategoryService.findAllProductCategory());
	}

	@GetMapping("/product-category/{id}")
	public ResponseEntity<ProductCategory> findProductCategoryById(@PathVariable("id") long id) {
		return ResponseEntity.ok(productCategoryService.findProductCategoryById(id));
	}

	@PutMapping("/product-category")
	public ResponseEntity<ProductCategory> findProductCategoryById(@RequestBody ProductCategory category) {
		return ResponseEntity.ok(productCategoryService.updateProductCategory(category));
	}

	@DeleteMapping("/product-category/{id}")
	public ResponseEntity<String> deleteProductCategoryById(@PathVariable("id") long id) {
		return ResponseEntity.ok(productCategoryService.deleteProductCategoryById(id));
	}
}
