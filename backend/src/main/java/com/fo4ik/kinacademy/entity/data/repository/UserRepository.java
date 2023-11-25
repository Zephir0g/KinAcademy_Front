package com.fo4ik.kinacademy.entity.data.repository;


import com.fo4ik.kinacademy.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);


}
