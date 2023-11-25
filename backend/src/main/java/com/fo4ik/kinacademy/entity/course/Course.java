package com.fo4ik.kinacademy.entity.course;

import com.fo4ik.kinacademy.entity.user.Status;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Fields for course to save it in DB", name = "Course", hidden = true)
@Hidden
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    @Setter(AccessLevel.NONE)
    Long id;

    @Column(nullable = false)
    String name, category, description, language;

    @Column(nullable = false)
    Long authorId;

    String url;
    String imagePath;

    Status status;

    boolean isPublic;

    int studentsCount;

    Date lastUpdateDate;

    Double rating;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Section> sections;

    @Operation(summary = "Add Section", description = "Add Section to course")
    public void addSection(Section section) {
        this.sections.add(section);
    }

    @Operation(summary = "Create Url", description = "Create Url for course automatically")
    public void setUrl() {
        this.url = createUrl();
    }

    String createUrl() {
        //TODO create url from name and id or something else
        return this.name;
    }


}
