package vn.hoidanit.jobhunter.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@Entity
@Table(name = "Products")
@Getter
@Setter
public class Products {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	@Column(columnDefinition = "MEDIUMTEXT")
	private String description;
	private int price;
	private int quantity;
	private String image;
	private Instant updatedAt;
	private Instant createdAt;
	private String createBy;
	private String updatedBy;

	@PrePersist
	public void handleBeforeCreate() {
		this.createBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
				? SecurityUtil.getCurrentUserLogin().get()
				: "";
		this.createdAt = Instant.now();
	}

	@PreUpdate
	public void handleBeforeUpdate() {
		this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
				? SecurityUtil.getCurrentUserLogin().get()
				: "";
		this.updatedAt = Instant.now();
	}

}
