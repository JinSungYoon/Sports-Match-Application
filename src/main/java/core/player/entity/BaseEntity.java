package core.player.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Getter;

@Getter
@MappedSuperclass
public class BaseEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long id;
	
	@Column(updatable = false)
	protected LocalDateTime createdDate;
	
	@Column
	protected LocalDateTime updatedDate;
	
	@PrePersist
	protected void onPersist() {
		this.createdDate = this.updatedDate = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updatedDate = LocalDateTime.now();
	}
	
}
