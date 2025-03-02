package com.Zephir0g.kinacademy.restControllers;

import com.Zephir0g.kinacademy.core.Response;
import com.Zephir0g.kinacademy.dto.user.CredentialDto;
import com.Zephir0g.kinacademy.dto.user.SingUpUserDto;
import com.Zephir0g.kinacademy.dto.user.UserDto;
import com.Zephir0g.kinacademy.entity.data.service.AuthService;
import com.Zephir0g.kinacademy.entity.data.service.UserService;
import com.Zephir0g.kinacademy.entity.user.Status;
import com.Zephir0g.kinacademy.exceptions.AppException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Methods for authentication")
public class AuthenticationController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    @Hidden
    public ResponseEntity<HttpStatus> register(@RequestBody SingUpUserDto singUpDto) {

        Response response = authService.register(singUpDto);
        if (!response.isSuccess()) {
            throw new AppException(response.getMessage(), response.getHttpStatus());
        }

        return ResponseEntity.ok().build();
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @Operation(summary = "Authenticate", description = "Authenticate, needed username (login) and password",
            tags = {"Authentication"})
    public ResponseEntity<?> authenticate(
            @Parameter(description = "User login and password by format JSON", required = true)
            @RequestBody CredentialDto credentialDTO) {

        Response response = authService.authenticate(credentialDTO);
        if (!response.isSuccess()) {
            return ResponseEntity.status(response.getHttpStatus()).body(response.getMessage());
        }

        UserDto userDto = userService.getUserByUsernameDto(credentialDTO.username());
        if (!userDto.getStatus().equals(Status.ACTIVE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is inactive");
        }
        userDto.setSECURE_TOKEN(response.getMessage());

        return ResponseEntity.ok(userDto);
    }
}
