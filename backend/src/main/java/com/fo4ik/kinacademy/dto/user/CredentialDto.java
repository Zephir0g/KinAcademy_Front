package com.fo4ik.kinacademy.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CredentialDto(
        @Schema(description = "User username (login)", example = "john123", name = "username")
                @NotBlank
        String username,
        @Schema(description = "User password", example = "john123", name = "password")
        @NotBlank
        String password) {
}
