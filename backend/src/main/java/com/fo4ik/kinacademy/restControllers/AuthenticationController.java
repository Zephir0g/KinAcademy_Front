package com.fo4ik.kinacademy.restControllers;

import com.fo4ik.kinacademy.core.PasswordConfig;
import com.fo4ik.kinacademy.core.UserAuthProvider;
import com.fo4ik.kinacademy.dto.user.CredentialDto;
import com.fo4ik.kinacademy.dto.user.SingUpUserDto;
import com.fo4ik.kinacademy.dto.user.UserDto;
import com.fo4ik.kinacademy.entity.data.service.UserService;
import com.fo4ik.kinacademy.exceptions.AppException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Methods for authentication")
public class AuthenticationController {
    private final UserService userService;
    private final UserAuthProvider userAuthProvider;
    private final PasswordConfig passwordConfig;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login, needed login and password", tags = {"Authentication"})
    public ResponseEntity<UserDto> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User login and password by format JSON", required = true)
            @RequestBody CredentialDto credentialDTO) {
        UserDto user = userService.login(credentialDTO);
        user.setSECURE_TOKEN(userAuthProvider.createToken(user));

        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    @Hidden
    public ResponseEntity<UserDto> register(@RequestBody SingUpUserDto singUpDto) {
        UserDto user = userService.register(singUpDto);
        user.setSECURE_TOKEN(userAuthProvider.createToken(user));
        return ResponseEntity.created(URI.create("/api/users/" + user.getId())).body(user);
    }

    @RequestMapping(value = "/is-secure-token-valid", method = RequestMethod.GET)
    @Operation(summary = "Check token", description = "Check token, needed user id and SECURE_TOKEN", tags = {"Authentication"})
    public ResponseEntity<?> isSecureTokenValid(
            @Parameter(description = "User id", required = true)
            @RequestParam Long userId,
            @Parameter(hidden = true)
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String SECRET_TOKEN
    ) {

        if (userService.isUserSecureTokenValid(SECRET_TOKEN, userId)) {
            return ResponseEntity.ok("{ \"message\" : \"Token is valid\"}");
        } else {
            throw new AppException("Token is invalid", HttpStatus.BAD_REQUEST);
        }
    }
}
