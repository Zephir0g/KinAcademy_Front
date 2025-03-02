package com.Zephir0g.kinacademy.entity.course;

import com.Zephir0g.kinacademy.entity.user.Status;
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
    String name, category, language;

    @Column(nullable = false, columnDefinition = "TEXT")
    String description, shortDescription;

    @Column(nullable = false)
    String authorUsername;

    String url;
    String imageUrl;

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

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", language='" + language + '\'' +
                ", authorUsername='" + authorUsername + '\'' +
                ", url='" + url + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", status=" + status +
                ", isPublic=" + isPublic +
                ", studentsCount=" + studentsCount +
                ", lastUpdateDate=" + lastUpdateDate +
                ", rating=" + rating +
                ", sections=" + sections +
                '}';
    }
}
