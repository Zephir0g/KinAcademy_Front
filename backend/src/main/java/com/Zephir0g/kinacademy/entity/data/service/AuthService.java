package com.Zephir0g.kinacademy.entity.data.service;

import com.Zephir0g.kinacademy.configuration.jwt.JwtService;
import com.Zephir0g.kinacademy.core.PasswordConfig;
import com.Zephir0g.kinacademy.core.Response;
import com.Zephir0g.kinacademy.dto.user.AuthResponseDto;
import com.Zephir0g.kinacademy.dto.user.CredentialDto;
import com.Zephir0g.kinacademy.dto.user.SingUpUserDto;
import com.Zephir0g.kinacademy.entity.user.Role;
import com.Zephir0g.kinacademy.entity.user.Status;
import com.Zephir0g.kinacademy.entity.user.User;
import com.Zephir0g.kinacademy.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordConfig passwordConfig;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public Response register(SingUpUserDto singUpDto) {


        var jwtToken = jwtService.generateToken(userService.register(singUpDto));


        return Response.builder()
                .isSuccess(true)
                .httpStatus(HttpStatus.OK)
                .message(jwtToken)
                .build();
    }

    public Response authenticate(CredentialDto credentialDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentialDTO.username(),
                            credentialDTO.password()
                    )
            );

            var user = userService.getUserByUsername(credentialDTO.username());
            return Response.builder()
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK)
                    .message(jwtService.generateToken(user))
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .isSuccess(false)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("Incorrect Login or Password")
                    .build();
        }
    }

    public Response authenticate(String username, String password){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        var user = userService.getUserByUsername(username);
        var jwtToken = jwtService.generateToken(user);
        return Response.builder()
                .isSuccess(true)
                .httpStatus(HttpStatus.OK)
                .message(jwtToken)
                .build();
    }
}
