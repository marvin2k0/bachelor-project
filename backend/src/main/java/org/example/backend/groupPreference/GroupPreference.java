package org.example.backend.groupPreference;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.group.Group;
import org.example.backend.survey.Survey;
import org.example.backend.user.User;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@ToString
@Table(name = "GroupPreference")
public class GroupPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    private int priority;
}