package ru.pominov.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pominov.taskmanager.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
