package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Roles;
import vn.hoidanit.jobhunter.repository.RoleRepository;

@Service
public class RolesService {

	private RoleRepository roleRepository;

	public RolesService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public Roles findRoleById(long id) {
		return this.roleRepository.findById(id).get();
	}
	public Roles createRole(Roles role) {
		return this.roleRepository.save(role);
	}
}
