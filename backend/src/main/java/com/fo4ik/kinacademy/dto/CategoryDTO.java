package com.fo4ik.kinacademy.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryDTO(
        @Schema(description = "Category name", example = "Web Development", name = "name")
        String name) {
}
