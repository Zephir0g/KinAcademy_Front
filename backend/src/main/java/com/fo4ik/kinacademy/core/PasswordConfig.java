package com.fo4ik.kinacademy.core;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.CharBuffer;

@Component
@RequiredArgsConstructor
public class PasswordConfig {

    // Configures and provides a BCryptPasswordEncoder bean with strength 10
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * Encodes a string password using BCryptPasswordEncoder.
     *
     * @param data The password to be encoded
     * @return The encoded password
     */
    public String encodeData(String data){
        return new BCryptPasswordEncoder(10).encode(data);
    }

    /**
     * Encodes a character array password using BCryptPasswordEncoder.
     *
     * @param password The password to be encoded as char array
     * @return The encoded password
     */
    public String encodeData(char[] password){
        return new BCryptPasswordEncoder(10).encode(CharBuffer.wrap(password));
    }

    /**
     * Checks if a plain text password matches the encoded password.
     *
     * @param password The plain text password
     * @param encodedPassword The encoded password to compare against
     * @return True if the passwords match, false otherwise
     */
    public boolean checkEncodedData(String password, String encodedPassword){
        return new BCryptPasswordEncoder(10).matches(password, encodedPassword);
    }

    /**
     * Checks if a plain text password as char array matches the encoded password.
     *
     * @param password The plain text password as char array
     * @param encodedPassword The encoded password to compare against
     * @return True if the passwords match, false otherwise
     */
    public boolean checkEncodedData(char[] password, String encodedPassword){
        return new BCryptPasswordEncoder(10).matches(CharBuffer.wrap(password),
                                                            encodedPassword);
    }
}

