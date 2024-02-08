package com.fo4ik.kinacademy.entity.course;

import com.fo4ik.kinacademy.entity.user.User;
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

    @Column(nullable = false)
    private String name;

//    @ElementCollection
    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "watchedVideos")
    private List<User> usersWhoWatched;

    @Column(nullable = false)
    private String urlToVideo;

}
