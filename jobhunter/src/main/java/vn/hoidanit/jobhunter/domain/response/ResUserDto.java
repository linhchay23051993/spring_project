package vn.hoidanit.jobhunter.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResUserDto {
	private long id;
	private String name;
	private String email;
	private GenderEnum gender;
	private int age;
	private String address;
	private Instant createdAt;
	private Instant updatedAt;

	private CompanyUser company;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CompanyUser {
		private long id;
		private String name;

	}

}
