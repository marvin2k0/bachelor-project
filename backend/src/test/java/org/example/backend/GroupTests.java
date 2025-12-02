package org.example.backend;

import org.example.backend.group.Group;
import org.example.backend.group.GroupService;
import org.example.backend.groupPreference.GroupPreference;
import org.example.backend.survey.Survey;
import org.example.backend.survey.SurveyService;
import org.example.backend.user.User;
import org.example.backend.user.UserService;

import org.example.backend.user.dto.UserCreationDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GroupTests {

    @Autowired
    private GroupService groupService;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private UserService userService;

    private Group group;

    @AfterEach
    void cleanup() {
        if (group != null && group.getId() != null) {
            groupService.deleteGroupById(group.getId());
            assertThrows(RuntimeException.class,
                    () -> groupService.getGroupById(group.getId()));
        }
        group = null;
    }

    @Test
    void test_create_group() {
        final String groupName = "Group A";

        group = Group.builder()
                .name(groupName)
                .capacity(5)
                .build();

        groupService.saveGroup(group);

        assertTrue(groupService.getAllGroups()
                .stream()
                .anyMatch(g -> g.getName().equals(groupName)));
    }

    @Test
    void test_group_belongs_to_survey() {
        Survey survey = Survey.builder()
                .name("TestSurvey")
                .build();
        survey = surveyService.saveSurvey(survey);

        group = Group.builder()
                .name("Group B")
                .capacity(4)
                .survey(survey)
                .build();

        groupService.saveGroup(group);

        Group found = groupService.getGroupById(group.getId());
        assertNotNull(found.getSurvey());
        assertEquals("TestSurvey", found.getSurvey().getName());

        surveyService.deleteSurveyById(survey.getId());
    }

    @Test
    void test_add_members_to_group() {
        ;
        User u1 = userService.createUser(new UserCreationDto("Anna", "1111111", "anna@gmail.com"));
        User u2 = userService.createUser(new UserCreationDto("Ben", "1111112", "ben@gmail.com"));

        group = Group.builder()
                .name("Group C")
                .capacity(10)
                .build();

        group.getMembers().add(u1);
        group.getMembers().add(u2);

        groupService.saveGroup(group);

        Group found = groupService.getGroupById(group.getId());

        assertEquals(2, found.getMembers().size());
        assertTrue(found.getMembers().stream().anyMatch(u -> u.getUsername().equals("Anna")));
        assertTrue(found.getMembers().stream().anyMatch(u -> u.getUsername().equals("Ben")));
    }

    @Test
    void test_group_preferences() {
        User user = userService.createUser(new UserCreationDto("Chris", "1111113", "chris@gmail.com"));

        group = Group.builder()
                .name("Group D")
                .capacity(6)
                .build();

        GroupPreference pref = GroupPreference.builder()
                .user(user)
                .group(group)
                .priority(1)
                .build();

        group.getGroupPreferences().add(pref);
        groupService.saveGroup(group);

        Group found = groupService.getGroupById(group.getId());

        assertEquals(1, found.getGroupPreferences().size());
        assertEquals(1, found.getGroupPreferences().getFirst().getPriority());
        assertEquals("Chris", found.getGroupPreferences().getFirst().getUser().getUsername());
    }
}
