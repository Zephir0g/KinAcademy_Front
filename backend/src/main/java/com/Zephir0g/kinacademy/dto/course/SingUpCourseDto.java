package com.Zephir0g.kinacademy.dto.course;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SingUpCourseDto(
        @Schema(description = "Course name", example = "Java")
        @NotBlank
        String name,

        @Schema(description = "Course short description", example = "Java course is the best course")
        String shortDescription,

        @Schema(description = "Course description", example = "This is course about Java")
        String description,

        @Schema(description = "Course description", example = "Java")
        @NotBlank
        String category,

        @Schema(description = "Course language", example = "English")
        @NotBlank
        String language,

        @Schema(description = "Course ulr", example = "java-learning")
        @NotBlank
        String url,

        @Schema(description = "Course is public", example = "true")
        @NotBlank
        boolean isPublic) {
}
