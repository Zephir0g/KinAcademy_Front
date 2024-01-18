package com.fo4ik.kinacademy.entity.user;


import com.fo4ik.kinacademy.entity.course.Course;
import com.fo4ik.kinacademy.entity.course.Video;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {
    // Unique identifier for the User entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    @Setter(AccessLevel.NONE)
    Long id;

    // First name of the user
    @Column(nullable = false)
    String firstName;

    // Surname or last name of the user
    @Column(nullable = false)
    String surname;

    // User's password (should be securely hashed)
    @Column(nullable = false)
    String password;

    // Unique email address for the user
    @Column(unique = true, length = 50)
    String email;

    // Unique username for the user
    @Column(unique = true)
    @NotNull
    String username;

    // Transient field for a secure token (not stored in the database)
    @Transient
    String SECURE_TOKEN;

    // User's role (e.g., ADMIN, USER) defined as an enumeration
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Role role;

    // User's status (e.g., ACTIVE, INACTIVE) defined as an enumeration
    @Column(nullable = false)
    Status status;

    // Preferred language of the user
    @Column(nullable = false)
    String language;

    // List of course IDs associated with the user (stored in a separate table)
    @ElementCollection
    @CollectionTable(name = "user_courses", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "course_id")
    List<Long> coursesId = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_watched_videos",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "video_id")
    )
    private List<Video> watchedVideos;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", SECURE_TOKEN='" + SECURE_TOKEN + '\'' +
                ", roles=" + role +
                ", status=" + status +
                ", language='" + language + '\'' +
                ", courses=" + coursesId +
                '}';
    }
}
