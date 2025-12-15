package vn.hoidanit.jobhunter.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {
	private long productId;
    private int quantity;
    private int price;
    private int totalPrice;
}
