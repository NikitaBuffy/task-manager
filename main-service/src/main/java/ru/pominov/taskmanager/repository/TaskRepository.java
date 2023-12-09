package ru.pominov.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pominov.taskmanager.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
