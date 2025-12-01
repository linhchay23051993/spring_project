package vn.hoidanit.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResLoginDto;
import vn.hoidanit.jobhunter.domain.request.ReqLoginDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.service.error.IdInvalidException;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private SecurityUtil securityUtil;
	private UserService userService;

	@Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
	private long refreshTokenExpiration;

	public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
			UserService userService) {
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.securityUtil = securityUtil;
		this.userService = userService;
	}

	@PostMapping("/auth/login")
	public ResponseEntity<ResLoginDto> login(@Valid @RequestBody ReqLoginDTO loginDTO) {
		// Nạp input gồm username/password vào Security
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginDTO.getUsername(), loginDTO.getPassword());

		// xác thực người dùng => cần viết hàm loadUserByUsername
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

		// set thong tin nguoi dung dang nhap vao context (co the su dung sau nay)
		SecurityContextHolder.getContext().setAuthentication(authentication);
		ResLoginDto res = new ResLoginDto();
		User currentUser = this.userService.handleGetUserByEmail(loginDTO.getUsername());
		ResLoginDto.UserLogin userLogin = new ResLoginDto.UserLogin(currentUser.getId(), currentUser.getEmail(),
				currentUser.getName());
		res.setUser(userLogin);
		String access_token = securityUtil.createAccessToken(authenticationToken.getName(), userLogin);
		res.setAccessToken(access_token);
		// create Refresh token
		String refreshToken = this.securityUtil.createRefreshToken(loginDTO.getUsername(), res);
		// update user
		this.userService.updateUserToken(refreshToken, loginDTO.getUsername());
		// set cookie
		ResponseCookie resCookies = ResponseCookie.from("refresh_token", refreshToken).httpOnly(true).secure(true)
				.path("/").maxAge(refreshTokenExpiration).build();

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookies.toString()).body(res);
	}

	@GetMapping("/auth/account")
	@ApiMessage("Get account")
	public ResponseEntity<ResLoginDto.UserGetAccount> getAccount() {
		String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
		User currentUser = this.userService.handleGetUserByEmail(email);
		ResLoginDto.UserLogin userLogin = new ResLoginDto.UserLogin();
		ResLoginDto.UserGetAccount userGetAccount = new ResLoginDto.UserGetAccount();
		userLogin.setId(currentUser.getId());
		userLogin.setEmail(currentUser.getEmail());
		userLogin.setUserName(currentUser.getName());
		userGetAccount.setUser(userLogin);

		return ResponseEntity.ok(userGetAccount);
	}

	@GetMapping("/auth/refresh")
	@ApiMessage("Get User by refresh token")
	public ResponseEntity<ResLoginDto> getRefreshToken(
			@CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token) throws IdInvalidException {
		if (refresh_token.equals("abc")) {
			throw new IdInvalidException("Ban khong co refresh token");
		}
		// check valid
		Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
		String email = decodedToken.getSubject();

		// check refresh token va email
		User currentUser = this.userService.getUsetByRefreshTokenAndEmail(refresh_token, email);
		if (currentUser == null) {
			throw new IdInvalidException("Token khong hop le");
		}
		// issue new token/set Refresh token as Cookies
		ResLoginDto res = new ResLoginDto();
		User currentUserDB = this.userService.handleGetUserByEmail(email);
		ResLoginDto.UserLogin userLogin = new ResLoginDto.UserLogin(currentUserDB.getId(), currentUserDB.getEmail(),
				currentUserDB.getName());
		res.setUser(userLogin);
		String access_token = securityUtil.createAccessToken(email, userLogin);
		res.setAccessToken(access_token);
		// create Refresh token
		String new_refresh_token = this.securityUtil.createRefreshToken(email, res);
		// update user
		this.userService.updateUserToken(new_refresh_token, email);
		// set cookie
		ResponseCookie resCookies = ResponseCookie.from("refresh_token", new_refresh_token).httpOnly(true).secure(true)
				.path("/").maxAge(refreshTokenExpiration).build();

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookies.toString()).body(res);
	}

	@PostMapping("/auth/logout")
	public ResponseEntity<Void> logout() {
		String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
		// update refresh token == null
		this.userService.updateUserToken(null, email);
		// set cookie
		ResponseCookie deleteCookies = ResponseCookie.from("refresh_token", null)
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(0)
				.build();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookies.toString()).body(null);
	}

}
