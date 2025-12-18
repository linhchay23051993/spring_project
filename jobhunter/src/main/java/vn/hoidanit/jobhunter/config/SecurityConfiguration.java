package vn.hoidanit.jobhunter.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;

import vn.hoidanit.jobhunter.service.error.CustomAccessDeniedHandler;
import vn.hoidanit.jobhunter.service.error.CustomAuthenticationEntryPoint;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
	@Value("${hoidanit.jwt.base64-secret}")
	private String jwtKey;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, CustomAuthenticationEntryPoint cutoms,
			CustomAccessDeniedHandler accessDenien) throws Exception {
		http.csrf(c -> c.disable()).cors(Customizer.withDefaults())
				.authorizeHttpRequests(authz -> authz
						.requestMatchers("/", "/auth/login", "/auth/refresh", "/auth/register").permitAll()
						// User Controller
						.requestMatchers(HttpMethod.POST, "/users**").hasAuthority("ROLE_ADMIN")
						.requestMatchers(HttpMethod.GET, "/users**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
						.requestMatchers(HttpMethod.PUT, "/users**").hasAuthority("ROLE_ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/users**").hasAuthority("ROLE_ADMIN")
						// Product Controller
						.requestMatchers(HttpMethod.POST, "/product/**").hasAuthority("ROLE_ADMIN")
						.requestMatchers(HttpMethod.PUT, "/product**").hasAuthority("ROLE_ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/product**").hasAuthority("ROLE_ADMIN")
						// Cart Controller
						.requestMatchers(HttpMethod.POST, "/cart**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
						.requestMatchers(HttpMethod.GET, "/cart**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
						.requestMatchers(HttpMethod.PUT, "/cart**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
						.requestMatchers(HttpMethod.DELETE, "/cart**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
						// Order controller
						.requestMatchers(HttpMethod.POST, "/order**").hasAuthority("ROLE_ADMIN")
//						.requestMatchers(HttpMethod.GET,"/users**").hasAuthority("ROLE_ADMIN")
//						.requestMatchers(HttpMethod.PUT,"/users**").hasAuthority("ROLE_ADMIN")
//						.requestMatchers(HttpMethod.DELETE,"/users**").hasAuthority("ROLE_ADMIN")
						.anyRequest().authenticated())
				.oauth2ResourceServer(
						(oauth2) -> oauth2.jwt(Customizer.withDefaults()).authenticationEntryPoint(cutoms))
				.exceptionHandling(ex -> ex.accessDeniedHandler(accessDenien)).formLogin(f -> f.disable())
//				.exceptionHandling(
//						exceptions -> exceptions.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint()) // 401
//								.accessDeniedHandler(new BearerTokenAccessDeniedHandler())) // 403

				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
				.macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
		return token -> {
			try {
				return jwtDecoder.decode(token);
			} catch (Exception e) {
				System.out.println(">>> JWT error: " + e.getMessage());
				throw e;
			}
		};
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthorityPrefix("");
		grantedAuthoritiesConverter.setAuthoritiesClaimName("permissions");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}

	@Bean
	public JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
	}

	private SecretKey getSecretKey() {
		byte[] keyBytes = Base64.from(jwtKey).decode();
		return new SecretKeySpec(keyBytes, 0, keyBytes.length, SecurityUtil.JWT_ALGORITHM.getName());
	}

}
