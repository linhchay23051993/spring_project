package vn.hoidanit.jobhunter.domain.response;

import java.time.Instant;

import vn.hoidanit.jobhunter.util.constant.GenderEnum;

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

	public CompanyUser getCompany() {
		return company;
	}

	public void setCompany(CompanyUser company) {
		this.company = company;
	}

	public ResUserDto() {
	}

	public ResUserDto(long id, String email, String name, GenderEnum gender, String address, int age, Instant updatedAt,
			Instant createdAt, CompanyUser company) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.gender = gender;
		this.address = address;
		this.age = age;
		this.updatedAt = updatedAt;
		this.createdAt = createdAt;
		this.company = company;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public GenderEnum getGender() {
		return gender;
	}

	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public static class CompanyUser {
		private long id;
		private String name;

		public CompanyUser() {
		}

		public CompanyUser(long id, String name) {
			this.id = id;
			this.name = name;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

}
