package org.example.expert.domain.todo.repository;

import java.util.Optional;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.stereotype.Repository;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;
	private final QTodo todo = QTodo.todo;
	private final QUser user = QUser.user;

	@Override
	public Optional<Todo> findByIdWithUser(Long todoId) {
		return Optional.ofNullable(
			jpaQueryFactory
				.selectFrom(todo)
				.leftJoin(todo.user, user).fetchJoin()
				.where(todo.id.eq(todoId))
				.fetchOne()
		);
	}

}
