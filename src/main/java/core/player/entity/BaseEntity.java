package core.player.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})	// JPA 내부에서 엔티티 객체가 생성/변경되는 것을 감지하는 역할
public abstract class BaseEntity{
	
	@CreatedDate	// 엔티티 생성시간
	@Column(updatable = false)
	protected LocalDateTime createdDate;
	
	
	@LastModifiedDate	// 최종 수정시간
	@Column(updatable = true)
	protected LocalDateTime updatedDate;
	
	@PrePersist		// JPA에서 persist가 날아가기 전에 수행되도록 하는 어노테이션
	protected void onPersist() {
		this.createdDate = this.updatedDate = LocalDateTime.now();
	}
	
	@PreUpdate		// JPA에서 update가 되기 전에 수행되도록 하는 어노테이션
	protected void onUpdate() {
		this.updatedDate = LocalDateTime.now();
	}
	
}
