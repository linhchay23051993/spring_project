package vn.hoidanit.jobhunter.domain.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequestDTO {
	private String status;
	private List<CartItemDTO> items;
}
