package com.my.library.repository;

import com.my.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsernameOrEmail(String username, String email);

    boolean existsByEmailAndIdIsNot(String email, Long id);
}
