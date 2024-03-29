package ru.pominov.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pominov.taskmanager.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
