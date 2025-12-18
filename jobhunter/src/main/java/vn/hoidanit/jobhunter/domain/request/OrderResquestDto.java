package vn.hoidanit.jobhunter.domain.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResquestDto {
	private List<OrderItemDto> items;
}
