package org.example.expert.domain.todo.repository;

import java.util.List;
import java.util.Optional;

import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.user.entity.QUser;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;
	private final QTodo todo = QTodo.todo;
	private final QUser user = QUser.user;
	private final QManager manager = QManager.manager;
	private final QComment comment = QComment.comment;

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

	@Override
	public Page<TodoSearchResponse> searchTodos(
		TodoSearchRequest request,
		Pageable pageable
	) {
		List<TodoSearchResponse> content = jpaQueryFactory
			.select(Projections.constructor(
				TodoSearchResponse.class,
				todo.title,
				manager.countDistinct(),
				comment.count()
			))
			.from(todo)
			.leftJoin(todo.managers, manager)
			.leftJoin(todo.comments, comment)
			.where(
				request.getKeyword() != null
				? todo.title.contains(request.getKeyword()) : null,
				request.getStartDate() != null
				? todo.createdAt.goe(request.getStartDate()) : null,
				request.getEndDate() != null
				? todo.createdAt.loe(request.getEndDate()) : null,
				request.getNickname() != null
				? manager.user.nickname.contains(request.getNickname()) : null
			)
			.groupBy(todo.id)
			.orderBy(todo.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = jpaQueryFactory
			.select(todo.countDistinct())
			.from(todo)
			.leftJoin(todo.managers, manager)
			.where(
				request.getKeyword() != null
				? todo.title.contains(request.getKeyword()) : null,
				request.getStartDate() != null
					? todo.createdAt.goe(request.getStartDate()) : null,
				request.getEndDate() != null
					? todo.createdAt.loe(request.getEndDate()) : null,
				request.getNickname() != null
					? manager.user.nickname.contains(request.getNickname()) : null
			)
			.fetchOne();

		return new PageImpl<>(content, pageable, total);
	}
}
