package com.fo4ik.kinacademy.entity.data.service;

import com.fo4ik.kinacademy.configuration.jwt.JwtService;
import com.fo4ik.kinacademy.core.PasswordConfig;
import com.fo4ik.kinacademy.core.Response;
import com.fo4ik.kinacademy.dto.user.AuthResponseDto;
import com.fo4ik.kinacademy.dto.user.CredentialDto;
import com.fo4ik.kinacademy.dto.user.SingUpUserDto;
import com.fo4ik.kinacademy.entity.user.Role;
import com.fo4ik.kinacademy.entity.user.Status;
import com.fo4ik.kinacademy.entity.user.User;
import com.fo4ik.kinacademy.exceptions.AppException;
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
