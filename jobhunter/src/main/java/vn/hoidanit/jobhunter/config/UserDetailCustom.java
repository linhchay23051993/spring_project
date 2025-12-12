package vn.hoidanit.jobhunter.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import vn.hoidanit.jobhunter.domain.Roles;
import vn.hoidanit.jobhunter.service.UserService;

@Component("userDetailsService")
public class UserDetailCustom implements UserDetailsService {
	UserService userService;

	public UserDetailCustom(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		vn.hoidanit.jobhunter.domain.User user = userService.handleGetUserByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not fund");
		}
		// Lấy role của user
		List<Roles> roles = user.getListRole();
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (Roles role : roles) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()));
		}

		return new User(user.getEmail(), user.getPassword(), authorities);
	}

}
