package vn.hoidanit.jobhunter.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import vn.hoidanit.jobhunter.domain.Products;

public class ProductSpecification {

	public static Specification<Products> filter(String name, Double min, Double max) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (name != null && !name.isBlank()) {
				predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
			}

			if (min != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("price"), min));
			}

			if (max != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("price"), max));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
