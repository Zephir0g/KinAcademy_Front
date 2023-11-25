package com.fo4ik.kinacademy.entity.user;


import com.fo4ik.kinacademy.entity.course.Course;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    @Setter(AccessLevel.NONE)
    Long id;

    @Column(nullable = false)
    String firstName, surname;

    @Column(nullable = false)
    String password;

    @Column(unique = true, length = 50)
    String email;

    @Column(unique = true)
    String login;

    @Column(unique = true)
    String USER_TOKEN;

    @Transient
    String SECURE_TOKEN;

    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    List<Role> roles;

    @Column(nullable = false)
    Status status;

    @Column(nullable = false)
    String language;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    List<Course> courses;

    @Operation(summary = "Add Course", description = "Add Course to user", tags = {"User"})
    public void addCourse(Course course) {
        this.courses.add(course);
    }

    String createUserToken() {
        UUID uuid = UUID.nameUUIDFromBytes(login.getBytes());
        return uuid.toString();
    }

    public void setUSER_TOKEN() {
        this.USER_TOKEN = createUserToken();
    }

}
