package com.fo4ik.kinacademy.dto.user;

import com.fo4ik.kinacademy.entity.user.Role;
import com.fo4ik.kinacademy.entity.user.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder()
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Data for user entity")
public class UserDto {
    @Schema(description = "User id", example = "1")
    @Min(1)
    @NotBlank
    Long id;

    @Schema(description = "User name", example = "John")
    String firstName;

    @Schema(description = "User surname", example = "Doe")
    String surname;

    @Schema(description = "User login", example = "john123")
    @NotBlank
    String login;

    @Schema(description = "User email", example = "john.example@gmail.com")
    @UniqueElements
    @Size(min = 5, max = 50)
    String email;

    @Schema(description = "User password", example = "john123")
    @UniqueElements
    @NotBlank
    String password;

    @Schema(description = "User language", example = "English")
    @NotBlank
    String language;

    @Schema(description = "User token", accessMode = Schema.AccessMode.READ_ONLY, example = "903ru3j9090nodikicvj908seuf90w3....")
    @UniqueElements
    @NotBlank
    String SECURE_TOKEN;

    @Schema(description = "User roles", example = "[\"USER\"]")
    List<Role> roles;

    @Schema(description = "User status", example = "ACTIVE")
    Status status;

    @Schema(description = "User courses", example = "[1, 2, 3]")
    List<Long> coursesId;
}
