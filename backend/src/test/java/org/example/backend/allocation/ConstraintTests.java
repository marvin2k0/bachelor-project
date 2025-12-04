package org.example.backend.allocation;

import jakarta.transaction.Transactional;
import org.example.backend.group.Group;
import org.example.backend.groupPreference.GroupPreference;
import org.example.backend.survey.Survey;
import org.example.backend.survey.SurveyService;
import org.example.backend.survey.dto.ParticipantImportResultDto;
import org.example.backend.user.User;
import org.example.backend.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.test.api.score.stream.ConstraintVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ConstraintTests {
    private ConstraintVerifier<AllocationConstraintProvider, AllocationSolution> constraintVerifier;
    private static final Random random = new Random();

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setup() {
        constraintVerifier = ConstraintVerifier.build(
                new AllocationConstraintProvider(),
                AllocationSolution.class,
                GroupAssignment.class
        );
    }

    @Test
    void test_capacity_in_bounds() {
        final Group group1 = Group.builder()
                .id(1L)
                .name("T01")
                .capacity(2)
                .build();

        final User user1 = User.builder()
                .name("Max Mustermann")
                .email("max.mustermann@uni.de")
                .build();
        final User user2 = User.builder()
                .name("Lisa Musterfrau")
                .email("lisa.musterfrau@uni.de")
                .build();

        final GroupAssignment assignment1 = new GroupAssignment(1L, user1, group1, Collections.emptyList());
        final GroupAssignment assignment2 = new GroupAssignment(1L, user2, group1, Collections.emptyList());

        constraintVerifier.verifyThat(AllocationConstraintProvider::groupCapacity)
                .given(assignment1, assignment2)
                .penalizesBy(0);
    }

    @Test
    void test_worst_case() {
        final Group group1 = Group.builder()
                .id(1L)
                .name("T01")
                .capacity(2)
                .build();

        final Group group2 = Group.builder()
                .id(2L)
                .name("T02")
                .capacity(1)
                .build();

        final User user1 = User.builder()
                .name("Bernhard Müller")
                .build();
        final User user2 = User.builder()
                .name("Bianca Müller")
                .build();
        final User user3 = User.builder()
                .name("Ludwig Ludolf")
                .build();
        final User user4 = User.builder()
                .name("Patrick Star")
                .build();

        final GroupPreference pref1 = GroupPreference.builder()
                .id(1L)
                .user(user1)
                .group(group1)
                .priority(1)
                .build();
        final GroupPreference pref2 = GroupPreference.builder()
                .id(2L)
                .user(user2)
                .group(group1)
                .priority(1)
                .build();
        final GroupPreference pref3 = GroupPreference.builder()
                .id(3L)
                .user(user3)
                .group(group1)
                .priority(1)
                .build();
        final GroupPreference pref4 = GroupPreference.builder()
                .id(4L)
                .user(user4)
                .group(group1)
                .priority(1)
                .build();

        final List<GroupAssignment> assignmentList = List.of(
                new GroupAssignment(1L, user1, Collections.singletonList(pref1)),
                new GroupAssignment(2L, user2, Collections.singletonList(pref2)),
                new GroupAssignment(3L, user3, Collections.singletonList(pref3)),
                new GroupAssignment(4L, user4, Collections.singletonList(pref4))
        );

        final AllocationSolution problem = new AllocationSolution(
                Arrays.asList(group1, group2),
                assignmentList
        );
        final SolverFactory<AllocationSolution> solverFactory = createSolverFactory();
        final Solver<AllocationSolution> solver = solverFactory.buildSolver();
        final AllocationSolution solution = solver.solve(problem);

        System.out.println("solution.getScore() = " + solution.getScore());
        System.out.println(solution.getAssignments());

        assertNotNull(solution);
        assertNotNull(solution.getScore());
        assertEquals(-53, solution.getScore().softScore());
        assertEquals(-1, solution.getScore().hardScore());
    }

    @Test
    void test_best_case() {
        final Group group1 = Group.builder()
                .id(1L)
                .name("T01")
                .capacity(2)
                .build();

        final Group group2 = Group.builder()
                .id(2L)
                .name("T02")
                .capacity(2)
                .build();

        final User user1 = User.builder()
                .name("Bernhard Müller")
                .build();
        final User user2 = User.builder()
                .name("Bianca Müller")
                .build();
        final User user3 = User.builder()
                .name("Ludwig Ludolf")
                .build();

        final GroupPreference pref1 = GroupPreference.builder()
                .id(1L)
                .user(user1)
                .group(group1)
                .priority(1)
                .build();
        final GroupPreference pref2 = GroupPreference.builder()
                .id(2L)
                .user(user2)
                .group(group2)
                .priority(1)
                .build();
        final GroupPreference pref3 = GroupPreference.builder()
                .id(3L)
                .user(user3)
                .group(group1)
                .priority(1)
                .build();
        final GroupPreference pref4 = GroupPreference.builder()
                .id(4L)
                .user(user3)
                .group(group2)
                .priority(2)
                .build();

        final List<GroupAssignment> assignmentList = List.of(
                new GroupAssignment(1L, user1, Collections.singletonList(pref1)),
                new GroupAssignment(2L, user2, Collections.singletonList(pref2)),
                new GroupAssignment(3L, user3, List.of(pref3, pref4))
        );

        final AllocationSolution problem = new AllocationSolution(
                Arrays.asList(group1, group2),
                assignmentList
        );
        final SolverFactory<AllocationSolution> solverFactory = createSolverFactory();
        final Solver<AllocationSolution> solver = solverFactory.buildSolver();
        final AllocationSolution solution = solver.solve(problem);

        System.out.println("solution.getScore() = " + solution.getScore());

        assertNotNull(solution);
        assertNotNull(solution.getScore());
        assertEquals(-3, solution.getScore().softScore());
        assertEquals(0, solution.getScore().hardScore());
    }

    @Test
    void test_random() {
        final Group group1 = Group.builder()
                .id(1L)
                .name("T01")
                .capacity(1)
                .build();
        final Group group2 = Group.builder()
                .id(2L)
                .name("T02")
                .capacity(2)
                .build();
        final Group group3 = Group.builder()
                .id(3L)
                .name("T03")
                .capacity(2)
                .build();
        final Group group4 = Group.builder()
                .id(4L)
                .name("T04")
                .capacity(2)
                .build();

        final List<Group> groups = List.of(group1, group2, group3, group4);

        final User user1 = User.builder()
                .name("Bernhard Müller")
                .build();
        final User user2 = User.builder()
                .name("Bianca Müller")
                .build();
        final User user3 = User.builder()
                .name("Ludwig Ludolf")
                .build();
        final User user4 = User.builder()
                .name("Tom Riddle")
                .build();
        final User user5 = User.builder()
                .name("Robert Spongebob Schwammkopf")
                .build();
        final User user6 = User.builder()
                .name("Sheldon Cooper")
                .build();
        final User user7 = User.builder()
                .name("Harry Potter")
                .build();

        final List<User> users = List.of(user1, user2, user3, user4, user5, user6, user7);
        final List<GroupAssignment> assignments = new ArrayList<>();

        int prefId = 1;
        int assignmentId = 1;

        System.out.println("====== USER PREFERENCES ======");

        for (User user : users) {
            final Integer[] prios = randomPriorities();
            final List<GroupPreference> preferences = new ArrayList<>();
            int i = 0;

            for (Group group : groups)
                preferences.add(new GroupPreference((long) prefId++, user, group, prios[i++ % prios.length]));

            assignments.add(new GroupAssignment(assignmentId++, user, preferences));

            System.out.println("USER: " + user.getName());

            for (GroupPreference preference : preferences)
                System.out.println("\t" + preference.getGroup().getName() + " => " + preference.getPriority());
        }

        final AllocationSolution problem = new AllocationSolution(
                Arrays.asList(group1, group2, group3, group4),
                assignments
        );
        final SolverFactory<AllocationSolution> solverFactory = createSolverFactory();
        final Solver<AllocationSolution> solver = solverFactory.buildSolver();
        final AllocationSolution solution = solver.solve(problem);

        printSolution(solution);

        assertNotNull(solution);
        assertNotNull(solution.getScore());
        assertEquals(0, solution.getScore().hardScore());
    }

    @Test
    void test_equally_distributed() {
        final Group group1 = Group.builder()
                .id(1L)
                .name("T01")
                .capacity(2)
                .build();
        final Group group2 = Group.builder()
                .id(2L)
                .name("T02")
                .capacity(2)
                .build();
        final User user1 = User.builder()
                .name("Madeye Moody")
                .build();
        final User user2 = User.builder()
                .name("Dolores Umbridge")
                .build();

        final List<GroupAssignment> assignments = new ArrayList<>();

        final GroupPreference pref1 = new GroupPreference(1L, user1, group1, 1);
        final GroupPreference pref2 = new GroupPreference(2L, user1, group2, 2);
        final GroupPreference pref3 = new GroupPreference(3L, user2, group2, 1);
        final GroupPreference pref4 = new GroupPreference(4L, user2, group1, 2);
        
        assignments.add(new GroupAssignment(1L, user1, List.of(pref1, pref2)));
        assignments.add(new GroupAssignment(2L, user2, List.of(pref3, pref4)));

        final AllocationSolution problem = new AllocationSolution(
                Arrays.asList(group1, group2),
                assignments
        );
        final SolverFactory<AllocationSolution> solverFactory = createSolverFactory();
        final Solver<AllocationSolution> solver = solverFactory.buildSolver();
        final AllocationSolution solution = solver.solve(problem);

        printSolution(solution);

        assertEquals(-2, solution.getScore().softScore());
    }
    
    @Test
    void test_not_solvable() {
        final Group group1 = Group.builder()
                .id(1L)
                .name("T01")
                .capacity(1)
                .build();

        final User user1 = User.builder()
                .name("Bernhard Müller")
                .build();
        final User user2 = User.builder()
                .name("Bianca Müller")
                .build();
        final User user3 = User.builder()
                .name("Ludwig Ludolf")
                .build();

        final GroupPreference pref1 = GroupPreference.builder()
                .id(1L)
                .user(user1)
                .group(group1)
                .priority(1)
                .build();
        final GroupPreference pref2 = GroupPreference.builder()
                .id(2L)
                .user(user2)
                .group(group1)
                .priority(1)
                .build();
        final GroupPreference pref3 = GroupPreference.builder()
                .id(3L)
                .user(user3)
                .group(group1)
                .priority(1)
                .build();

        final List<GroupAssignment> assignmentList = List.of(
                new GroupAssignment(1L, user1, Collections.singletonList(pref1)),
                new GroupAssignment(2L, user2, Collections.singletonList(pref2)),
                new GroupAssignment(3L, user3, Collections.singletonList(pref3))
        );

        final AllocationSolution problem = new AllocationSolution(
                Collections.singletonList(group1),
                assignmentList
        );
        final SolverFactory<AllocationSolution> solverFactory = createSolverFactory();
        final Solver<AllocationSolution> solver = solverFactory.buildSolver();
        final AllocationSolution solution = solver.solve(problem);

        assertNotNull(solution);
        assertEquals(-2, solution.getScore().hardScore());
    }

    @Test
    @Transactional
    void test_csv_import() throws IOException {
        final Survey survey = Survey.builder()
                .id(1L)
                .name("TEST Survey")
                .build();
        surveyService.saveSurvey(survey);

        assertEquals("TEST Survey", surveyService.getSurveyById(1L).getName());

        final URL resource = getClass().getResource("/students.csv");

        assertNotNull(resource);

        final ParticipantImportResultDto participantImportResultDto = surveyService.importParticipantsFromCsv(
                1L,
                new MockMultipartFile("students.csv", resource.openStream().readAllBytes())
        );
        final List<User> users = participantImportResultDto.importedParticipants().stream().map(p -> userService.getUserById(p.id())).toList();
        final List<Group> groups = generateGroups(users.size());
        final List<GroupAssignment> assignments = randomAssignments(users, groups);

        final AllocationSolution problem = new AllocationSolution(
                groups,
                assignments
        );
        final SolverFactory<AllocationSolution> solverFactory = createSolverFactory();
        final Solver<AllocationSolution> solver = solverFactory.buildSolver();
        final AllocationSolution solution = solver.solve(problem);

        printSolution(solution);

        assertNotNull(solution);
        assertEquals(0, solution.getScore().hardScore());

        final int softDifference = (solution.getScore().softScore() + users.size()) * -1;

        System.out.println("softDifference = " + softDifference);
    }

    private static List<Group> generateGroups(int totalCapacity) {
        final List<Group> groups = new ArrayList<>();
        final int groupCapacity = 6;
        final int groupAmount = totalCapacity / groupCapacity + 1;

        for (int i = 0; i < groupAmount; i++) {
            groups.add(Group.builder()
                    .id((long) (i + 1))
                    .name("T" + ((i < 9) ? "0" : "") + i)
                    .capacity(groupCapacity)
                    .build());
        }

        return groups;
    }

    private static List<GroupAssignment> randomAssignments(List<User> users, List<Group> groups) {
        long assignmentId = 1;
        long prefId = 1;
        final List<GroupAssignment> assignments = new ArrayList<>();

        for (User user : users) {
            final List<GroupPreference> preferences = new ArrayList<>();
            final Integer[] priorities = randomPriorities(groups.size());
            int index = 0;

            for (Group group : groups) {
                final int priority = priorities[index++];
                final GroupPreference pref = new GroupPreference(prefId++, user, group, priority);
                preferences.add(pref);
                System.out.println("\t" + pref.getGroup().getName() + " => " + pref.getPriority());
            }

            assignments.add(new GroupAssignment(assignmentId++, user, preferences));
        }

        return assignments;
    }

    private static Integer[] randomPriorities() {
        return randomPriorities(4);
    }

    private static Integer[] randomPriorities(int amount) {
        final List<Integer> priorities = new ArrayList<>();

        for (int i = 1; i <= amount; i++) {
            priorities.add(i);
        }

        Collections.shuffle(priorities);

        return priorities.toArray(new Integer[]{});
    }

    private static void printSolution(AllocationSolution solution) {
        System.out.println("\n====== SOLUTION (" + solution.getScore() + ") ======");

        final Map<Group, List<GroupAssignment>> groupAssignments = solution.getAssignments().stream().collect(Collectors.groupingBy(GroupAssignment::getAssignedGroup));

        for (Map.Entry<Group, List<GroupAssignment>> entry : groupAssignments.entrySet()) {
            final Group group = entry.getKey();
            final List<GroupAssignment> tempAssignments = entry.getValue();

            System.out.println(group.getName() + " => " + String.join(", ", tempAssignments.stream().map(a -> a.getUser().getName() + " (" + a.getPriorityForGroup(a.getAssignedGroup()) + ")").toList()));
        }
    }

    private SolverFactory<AllocationSolution> createSolverFactory() {
        final SolverConfig solverConfig = new SolverConfig()
                .withSolutionClass(AllocationSolution.class)
                .withEntityClasses(GroupAssignment.class)
                .withConstraintProviderClass(AllocationConstraintProvider.class)
                .withTerminationSpentLimit(Duration.ofSeconds(5));

        return SolverFactory.create(solverConfig);
    }
}
