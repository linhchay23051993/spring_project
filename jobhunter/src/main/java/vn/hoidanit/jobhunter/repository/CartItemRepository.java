package vn.hoidanit.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
	CartItem findByProductId(long id);
	void deleteByCartIdAndProductId(long cartId, long productId);
	void deleteByIdIn(List<Long> ids);
}
