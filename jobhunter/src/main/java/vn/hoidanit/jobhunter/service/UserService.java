package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDto;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDto;
import vn.hoidanit.jobhunter.domain.response.ResUserDto;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;

	private final CompanyService companyService;

	public UserService(UserRepository userRepository, CompanyService companyService) {
		this.userRepository = userRepository;
		this.companyService = companyService;
	}

	public User handleCreateUser(User user) {
		// check company
		if (user.getCompany() != null) {
			Optional<Company> comOptional = this.companyService.findById(user.getCompany().getId());
			user.setCompany(comOptional.isPresent() ? comOptional.get() : null);

		}
		return this.userRepository.save(user);
	}

	public void handleDeleteUser(Long id) {
		this.userRepository.deleteById(id);
	}

	public User fetchUserById(long id) {
		Optional<User> userOptional = this.userRepository.findById(id);
		if (userOptional.isPresent()) {
			return userOptional.get();
		}
		return null;
	}

	public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
		Page<User> pageUser = this.userRepository.findAll(spec, pageable);
		ResultPaginationDTO rs = new ResultPaginationDTO();
		ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

		mt.setPage(pageable.getPageNumber() + 1);
		mt.setPageSize(pageable.getPageSize());
		mt.setPages(pageUser.getTotalPages());
		mt.setTotal(pageUser.getTotalElements());

		rs.setMeta(mt);
		List<ResUserDto> listUser = pageUser.getContent().stream()
				.map(item -> new ResUserDto(item.getId(), item.getEmail(), item.getName(), item.getGender(),
						item.getAddress(), item.getAge(), item.getUpdatedAt(), item.getCreatedAt(),
						new ResUserDto.CompanyUser(
								item.getCompany().getId(),
								item.getCompany().getName())

				))
				.collect(Collectors.toList());
		rs.setResult(listUser);

		return rs;
	}

	public User handleUpdateUSer(User reqUser) {
		User currentUser = this.fetchUserById(reqUser.getId());
		if (currentUser != null) {
			currentUser.setAddress(reqUser.getAddress());
			currentUser.setGender(reqUser.getGender());
			currentUser.setAge(reqUser.getAge());
			currentUser.setName(reqUser.getName());
			if (reqUser.getCompany() != null) {
				Optional<Company> comOptional = this.companyService.findById(reqUser.getCompany().getId());
				currentUser.setCompany(comOptional.isPresent() ? comOptional.get() : null);
			}

			currentUser = this.userRepository.save(currentUser);
			return currentUser;

		}
		return null;
	}

	public User handleGetUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public boolean isEmailExist(String email) {
		return this.userRepository.existsByEmail(email);
	}

	public ResCreateUserDto convertToResCreateUserDto(User user) {
		ResCreateUserDto res = new ResCreateUserDto();
		res.setId(user.getId());
		res.setEmail(user.getEmail());
		res.setName(user.getName());
		res.setAge(user.getAge());
		res.setCreatedAt(user.getCreatedAt());
		res.setGender(user.getGender());
		res.setAddress(user.getAddress());
		return res;
	}

	public ResUserDto convertToResUserDto(User user) {
		ResUserDto res = new ResUserDto();
		ResUserDto.CompanyUser com = new ResUserDto.CompanyUser();
		if (user.getCompany() != null) {
			com.setId(user.getCompany().getId());
			com.setName(user.getCompany().getName());
			res.setCompany(com);
		}

		res.setId(user.getId());
		res.setEmail(user.getEmail());
		res.setName(user.getName());
		res.setAge(user.getAge());
		res.setCreatedAt(user.getCreatedAt());
		res.setUpdatedAt(user.getUpdatedAt());
		res.setGender(user.getGender());
		res.setAddress(user.getAddress());
		return res;
	}

	public ResUpdateUserDto convertToResUpdateUserDto(User user) {
		ResUpdateUserDto res = new ResUpdateUserDto();
		ResUpdateUserDto.CompanyUser com = new ResUpdateUserDto.CompanyUser();
		if (user.getCompany() != null) {
			com.setId(user.getCompany().getId());
			com.setName(user.getCompany().getName());
			res.setCompany(com);
		}

		res.setId(user.getId());
		res.setName(user.getName());
		res.setAge(user.getAge());
		res.setUpdatedAt(user.getUpdatedAt());
		res.setGender(user.getGender());
		res.setAddress(user.getAddress());
		return res;
	}

	public void updateUserToken(String token, String email) {
		User currentUser = this.userRepository.findByEmail(email);
		if (currentUser != null) {
			currentUser.setRefreshToken(token);
			this.userRepository.save(currentUser);
		}
	}

	public User getUsetByRefreshTokenAndEmail(String token, String email) {
		return this.userRepository.findByRefreshTokenAndEmail(token, email);
	}

}
