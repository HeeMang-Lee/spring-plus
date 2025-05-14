package org.example.expert.domain.todo.dto.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoSearchRequest {
	private String nickname;
	private String keyword;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
}
