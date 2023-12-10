package ru.pominov.taskmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.pominov.taskmanager.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByTaskId(Long taskId, Pageable pageable);
}
