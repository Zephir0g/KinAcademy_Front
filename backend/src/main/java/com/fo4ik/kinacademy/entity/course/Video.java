package com.fo4ik.kinacademy.entity.course;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable=false)
    private String name;

    @ElementCollection
    @Setter(AccessLevel.NONE)
    private List<Long> usersIdWitchWatchedThisVideo;

    public void addUserIdWitchWatchedThisVideo(Long id) {
        this.usersIdWitchWatchedThisVideo.add(id);
    }

}
