package com.my.library.repository;

import com.my.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByAuthorAndName(String author, String name);

    boolean existsByAuthorAndNameAndIdIsNot(String author, String name, Long id);
}
