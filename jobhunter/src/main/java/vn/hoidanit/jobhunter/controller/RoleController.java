package vn.hoidanit.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.Roles;
import vn.hoidanit.jobhunter.service.RolesService;

@RestController
public class RoleController {
	private RolesService rolesService;

	public RoleController(RolesService rolesService) {
		this.rolesService = rolesService;
	}
	@PostMapping("/role")
	public ResponseEntity<Roles> createRole(@RequestBody Roles role){
		return ResponseEntity.ok(this.rolesService.createRole(role));
	}
	
}
