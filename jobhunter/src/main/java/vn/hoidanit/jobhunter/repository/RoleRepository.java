package vn.hoidanit.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.Roles;

@Repository
public interface RoleRepository  extends JpaRepository<Roles, Long>{

}
