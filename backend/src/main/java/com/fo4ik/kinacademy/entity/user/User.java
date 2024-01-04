package com.fo4ik.kinacademy.entity.user;


import com.fo4ik.kinacademy.entity.course.Course;
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
    @NotNull
    String username;

    @Transient
    String SECURE_TOKEN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Role role;

    @Column(nullable = false)
    Status status;

    @Column(nullable = false)
    String language;

    /*@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    List<Course> courses;*/

    @ElementCollection
    @CollectionTable(name = "user_courses", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "course_id")
    List<Long> coursesId = new ArrayList<>();

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
