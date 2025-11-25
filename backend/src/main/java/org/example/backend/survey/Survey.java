package org.example.backend.survey;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.user.User;
import org.example.backend.group.Group;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "survey")
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToMany
    @JoinTable(
            name = "survey_participants",
            joinColumns = @JoinColumn(name = "survey_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private List<User> participants = new ArrayList<>();

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Group> groups = new ArrayList<>();

    public boolean isActive() {
        return isActive(LocalDateTime.now());
    }

    public boolean isActive(LocalDateTime now) {
        return (startTime == null || !now.isBefore(startTime)) &&
                (endTime == null || !now.isAfter(endTime));
    }
}