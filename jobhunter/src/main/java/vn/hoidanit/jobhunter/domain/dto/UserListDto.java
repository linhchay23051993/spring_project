package vn.hoidanit.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class UserListDto {
	private long id;
	private String email;
	private int age;
	private GenderEnum gender;
	private String address;
}
