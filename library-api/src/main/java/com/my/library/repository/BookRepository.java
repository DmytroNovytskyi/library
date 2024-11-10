package com.my.library.repository;

import com.my.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface for the book repository, based on JpaRepository.
 * Provides standard methods for CRUD operations on Book entities.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Checks if a book exists in the repository by the author's name and book title.
     *
     * @param author The author's name
     * @param name   The book's title
     * @return true if the book exists, false otherwise
     */
    boolean existsByAuthorAndName(String author, String name);

    /**
     * Checks if a book exists in the repository by the author's name, book title, and excluding a specific ID.
     *
     * @param author The author's name
     * @param name   The book's title
     * @param id     The ID to exclude from the check
     * @return true if the book exists, false otherwise
     */
    boolean existsByAuthorAndNameAndIdIsNot(String author, String name, Long id);
}
