package org.example.backend.allocation;

import org.example.backend.group.Group;
import org.example.backend.groupPreference.GroupPreference;
import org.example.backend.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.test.api.score.stream.ConstraintVerifier;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ConstraintTests {
    private ConstraintVerifier<AllocationConstraintProvider, AllocationSolution> constraintVerifier;
    private static final Random random = new Random();

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
                .username("Max Mustermann")
                .email("max.mustermann@uni.de")
                .build();
        final User user2 = User.builder()
                .username("Lisa Musterfrau")
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
                .username("Bernhard Müller")
                .build();
        final User user2 = User.builder()
                .username("Bianca Müller")
                .build();
        final User user3 = User.builder()
                .username("Ludwig Ludolf")
                .build();
        final User user4 = User.builder()
                .username("Patrick Star")
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
                .username("Bernhard Müller")
                .build();
        final User user2 = User.builder()
                .username("Bianca Müller")
                .build();
        final User user3 = User.builder()
                .username("Ludwig Ludolf")
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
                .username("Bernhard Müller")
                .build();
        final User user2 = User.builder()
                .username("Bianca Müller")
                .build();
        final User user3 = User.builder()
                .username("Ludwig Ludolf")
                .build();
        final User user4 = User.builder()
                .username("Tom Riddle")
                .build();
        final User user5 = User.builder()
                .username("Robert Spongebob Schwammkopf")
                .build();
        final User user6 = User.builder()
                .username("Sheldon Cooper")
                .build();
        final User user7 = User.builder()
                .username("Harry Potter")
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

            System.out.println("USER: " + user.getUsername());

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
                .username("Madeye Moody")
                .build();
        final User user2 = User.builder()
                .username("Dolores Umbridge")
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
                .username("Bernhard Müller")
                .build();
        final User user2 = User.builder()
                .username("Bianca Müller")
                .build();
        final User user3 = User.builder()
                .username("Ludwig Ludolf")
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
                Arrays.asList(group1),
                assignmentList
        );
        final SolverFactory<AllocationSolution> solverFactory = createSolverFactory();
        final Solver<AllocationSolution> solver = solverFactory.buildSolver();
        final AllocationSolution solution = solver.solve(problem);

        assertNotNull(solution);
        assertEquals(-2, solution.getScore().hardScore());
    }

    private static Integer[] randomPriorities() {
        final List<Integer> priorities = new ArrayList<>(List.of(1, 2, 3, 4));

        Collections.shuffle(priorities);

        return priorities.toArray(new Integer[]{});
    }

    private static void printSolution(AllocationSolution solution) {
        System.out.println("\n====== SOLUTION (" + solution.getScore() + ") ======");

        final Map<Group, List<GroupAssignment>> groupAssignments = solution.getAssignments().stream().collect(Collectors.groupingBy(GroupAssignment::getAssignedGroup));

        for (Map.Entry<Group, List<GroupAssignment>> entry : groupAssignments.entrySet()) {
            final Group group = entry.getKey();
            final List<GroupAssignment> tempAssignments = entry.getValue();

            System.out.println(group.getName() + " => " + String.join(", ", tempAssignments.stream().map(a -> a.getUser().getUsername()).toList()));
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
