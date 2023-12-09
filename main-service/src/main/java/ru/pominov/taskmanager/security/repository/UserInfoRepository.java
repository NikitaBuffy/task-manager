package ru.pominov.taskmanager.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pominov.taskmanager.security.model.UserInfo;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByEmail(String email);
}
