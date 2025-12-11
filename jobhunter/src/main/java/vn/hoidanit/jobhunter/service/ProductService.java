package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Products;
import vn.hoidanit.jobhunter.repository.ProductRepository;

@Service
public class ProductService {

	private ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Products createProduct(Products products) {
		return this.productRepository.save(products);
	}

	public Products updateProduct(Products products) {
		Products productsDB = this.productRepository.findById(products.getId()).get();
		productsDB.setName(products.getName());
		productsDB.setPrice(products.getPrice());
		productsDB.setDescription(products.getDescription());
		return this.productRepository.save(productsDB);
	}

	public List<Products> findAllProduct() {
		return productRepository.findAll();
	}

	public Products findProductById(long id) {
		return productRepository.findById(id).get();
	}

	public String deleteProductById(long id) {
		productRepository.deleteById(id);
		return "Delete success";
	}
}
