package vn.hoidanit.jobhunter.domain.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
	private int total;
	private List<ProductListDto> listDto;
}
