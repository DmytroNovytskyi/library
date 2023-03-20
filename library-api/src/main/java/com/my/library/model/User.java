package com.my.library.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.Set;

/**
 * A class representing a user in the library system.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @ManyToMany
    @ToString.Exclude
    private Set<Book> books;

    /**
     * Checks if the specified object is equal to this user.
     *
     * @param o The object to compare to.
     * @return true if object is a user and IDs are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    /**
     * Generates a hash code for this user.
     *
     * @return The hash code value.
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
