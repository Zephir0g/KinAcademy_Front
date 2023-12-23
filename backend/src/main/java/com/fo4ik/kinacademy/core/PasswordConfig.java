package com.fo4ik.kinacademy.core;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.CharBuffer;

@Component
@RequiredArgsConstructor
public class PasswordConfig {


    //method with encoder password and return string
//    public static String getEncoderPassword(String password){
//        return new BCryptPasswordEncoder(8).encode(password);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    public String encodeData(String data){
        return new BCryptPasswordEncoder(10).encode(data);
    }

    public String encodeData(char[] password){
        return new BCryptPasswordEncoder(10).encode(CharBuffer.wrap(password));
    }

    public boolean checkEncodedData(String password, String encodedPassword){
        return new BCryptPasswordEncoder(10).matches(password, encodedPassword);
    }

    public boolean checkEncodedData(char[] password, String encodedPassword){
        return new BCryptPasswordEncoder(10).matches(CharBuffer.wrap(password), encodedPassword);
    }
}
