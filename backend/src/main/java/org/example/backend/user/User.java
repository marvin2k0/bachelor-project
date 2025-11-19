package org.example.backend.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.backend.survey.Survey;
import org.example.backend.group.Group;
import org.example.backend.groupPreference.GroupPreference;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;


    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Survey> createdSurveys;

    @ManyToMany(mappedBy = "participants")
    private List<Survey> participatedSurveys = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupPreference> groupPreferences = new ArrayList<>();

    @ManyToMany(mappedBy = "members")
    private List<Group> assignedGroups = new ArrayList<>();
}