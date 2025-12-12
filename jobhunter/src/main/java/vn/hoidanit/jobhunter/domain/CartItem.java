package vn.hoidanit.jobhunter.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CartItem")
@Getter
@Setter
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Products product;

	private int quantity;
	private int price;
	private int totalPrice;

	@ManyToOne
	Cart cart;
}
