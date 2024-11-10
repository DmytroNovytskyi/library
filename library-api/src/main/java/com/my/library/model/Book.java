package com.my.library.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.Set;

/**
 * A class representing a book in the library.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"author", "name"})})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer available;
    @ManyToMany(mappedBy = "books")
    @ToString.Exclude
    private Set<User> users;

    /**
     * Checks if the specified object is equal to this book.
     *
     * @param o The object to compare to
     * @return true if object is a book and IDs are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Book book = (Book) o;
        return id != null && Objects.equals(id, book.id);
    }

    /**
     * Generates a hash code for this book.
     *
     * @return The hash code value.
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
