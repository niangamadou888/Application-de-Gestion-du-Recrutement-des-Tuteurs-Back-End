package com.project.backend.dao;

import com.project.backend.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends CrudRepository<User, String> {
    Optional<User> findByUserEmail(String userEmail);
    List<User> findByResetToken(String resetToken);
}
