package org.example.expert.domain.manager.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "log")
public class Log {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private LogStatus status;

	@Column(columnDefinition = "TEXT")
	private String detail;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	protected Log() {}

	public Log(LogStatus status, String detail) {
		this.status = status;
		this.detail = detail;
	}

	@PrePersist
	public void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
}
