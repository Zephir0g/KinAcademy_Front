package com.fo4ik.kinacademy.dto.course;

import com.fo4ik.kinacademy.entity.course.Section;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Data for course entity", name = "CourseDto")
public class CourseDto {

    @Schema(description = "Course id", example = "1")
    @Min(1)
    Long id;

    @Schema(description = "Course name", example = "Java")
    @NotBlank
    String name;

    @Schema(description = "Course short description", example = "Java course is the best course")
    String shortDescription;

    @Schema(description = "Course description", example = "Java course description")
    String description;


    @Schema(description = "Course author username", example = "John")
    @NotBlank
    String authorUsername;

    @Schema(description = "Course category", example = "Java")
    @NotBlank
    String category;

    @Schema(description = "Course language", example = "English")
    @NotBlank
    String language;

    @Schema(description = "Course status", example = "ACTIVE")
    String status;

    @Schema(description = "Course url", example = "/java-learn", accessMode = Schema.AccessMode.READ_ONLY)
    String url;

    @Schema(description = "Course is public", example = "true")
    @NotBlank
    boolean isPublic;

    @Schema(description = "Course image", example = "https://example.com/image.png")
    String imageUrl;

    @Schema(description = "Course students count", example = "0")
    int studentsCount;

    @Schema(description = "Course last update date", example = "2021-05-01", accessMode = Schema.AccessMode.READ_ONLY)
    @NotBlank
    Date lastUpdateDate;

    @Schema(description = "Course rating", example = "0.0", accessMode = Schema.AccessMode.READ_ONLY)
    Double rating;

    @Schema(description = "Course sections", example = "See SectionDto")
    List<SectionsDto> sections;


}
