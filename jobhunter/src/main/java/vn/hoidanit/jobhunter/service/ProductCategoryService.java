package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.ProductCategory;
import vn.hoidanit.jobhunter.repository.ProductCategoryRepository;

@Service
public class ProductCategoryService {
	private ProductCategoryRepository categoryRepository;

	public ProductCategoryService(ProductCategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public ProductCategory createProductCategory(ProductCategory category) {
		return categoryRepository.save(category);
	}

	public ProductCategory updateProductCategory(ProductCategory category) {
		ProductCategory productCategory = this.findProductCategoryById(category.getId());
		productCategory.setName(category.getName());
		return categoryRepository.save(productCategory);
	}

	public List<ProductCategory> findAllProductCategory() {
		return categoryRepository.findAll();
	}

	public ProductCategory findProductCategoryById(long id) {
		return categoryRepository.findById(id).get();
	}

	public String deleteProductCategoryById(long id) {
		categoryRepository.deleteById(id);
		return "Delete success";
	}
}
