package vn.hoidanit.jobhunter.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResLoginDto {
	@JsonProperty("access_token")
	private String accessToken;
	private UserLogin user;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public UserLogin getUser() {
		return user;
	}

	public void setUser(UserLogin user) {
		this.user = user;
	}

	public static class UserLogin {
		private long id;
		private String email;
		private String username;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getUserName() {
			return username;
		}

		public void setUserName(String name) {
			this.username = name;
		}

		public UserLogin(long id, String email, String name) {
			super();
			this.id = id;
			this.email = email;
			this.username = name;
		}

		public UserLogin() {

		}

	}

	public static class UserGetAccount {
		private UserLogin user;

		public UserLogin getUser() {
			return user;
		}

		public void setUser(UserLogin user) {
			this.user = user;
		}

		public UserGetAccount(UserLogin user) {
			super();
			this.user = user;
		}

		public UserGetAccount() {

		}

	}

}
