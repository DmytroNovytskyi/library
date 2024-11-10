package com.my.library.repository;

import com.my.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface for the user repository, based on JpaRepository.
 * Provides standard methods for CRUD operations on User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Checks if a user exists in the repository by their username or email.
     *
     * @param username The user's username
     * @param email    The user's email address
     * @return true if the user exists, false otherwise
     */
    boolean existsByUsernameOrEmail(String username, String email);

    /**
     * Checks if a user exists in the repository by their email and excluding a specific ID.
     *
     * @param email The user's email address
     * @param id    The ID to exclude from the check
     * @return true if the user exists, false otherwise
     */
    boolean existsByEmailAndIdIsNot(String email, Long id);
}
