package vn.hoidanit.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductListDto {
	private long id;
	private String name;
	private String description;
	private int price;
}
