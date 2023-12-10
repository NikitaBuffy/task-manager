package ru.pominov.taskmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.pominov.taskmanager.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findTaskByIdAndAuthorId(Long taskId, Long authorId);

    Task findTaskByIdAndPerformerId(Long taskId, Long performerId);

    Page<Task> findAllByAuthorId(Long authorId, Pageable pageable);
}
