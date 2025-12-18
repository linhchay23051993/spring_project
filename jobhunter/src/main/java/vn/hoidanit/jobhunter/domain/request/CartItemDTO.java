package vn.hoidanit.jobhunter.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {
	private long productId;
    private int quantity;
}
