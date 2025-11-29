package org.example.backend.allocation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.allocation.dto.AllocationResult;
import org.example.backend.group.Group;
import org.example.backend.group.GroupRepository;
import org.example.backend.groupPreference.GroupPreference;
import org.example.backend.groupPreference.GroupPreferenceService;
import org.example.backend.survey.Survey;
import org.example.backend.survey.SurveyService;
import org.example.backend.user.User;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AllocationService {
    private final SurveyService surveyService;
    private final GroupPreferenceService groupPreferenceService;
    private final GroupRepository groupRepository;
    private final SolverManager<AllocationSolution, Long> solverManager;

    @Transactional
    public AllocationSolution allocateSurvey(long surveyId) throws ExecutionException, InterruptedException {
        final Survey survey = surveyService.getSurveyById(surveyId);

        if (survey == null)
            throw new UnsupportedOperationException("Survey not found id:" + surveyId);

        final List<Group> groups = survey.getGroups();
        final List<User> participants = survey.getParticipants();
        final List<GroupPreference> allPreferences = groupPreferenceService.getAllGroupPreferences().stream()
                .filter(preference -> groups.stream().anyMatch(g -> g.getId().equals(preference.getGroup().getId())))
                .filter(preference -> participants.stream().anyMatch(p -> p.getId().equals(preference.getUser().getId())))
                .toList();
        final Map<Long, List<GroupPreference>> preferenceByParticipant = allPreferences.stream()
                .collect(Collectors.groupingBy(pref -> pref.getUser().getId()));
        final List<GroupAssignment> assignments = new ArrayList<>();

        long assignmentId = 1;

        for (User participant : participants) {
            final List<GroupPreference> participantPreferences = preferenceByParticipant.getOrDefault(participant.getId(), new ArrayList<>());
            assignments.add(new GroupAssignment(assignmentId++, participant, participantPreferences));
        }

        final AllocationSolution problem = new AllocationSolution(groups, assignments);
        final SolverJob<AllocationSolution, Long> solverJob = solverManager.solve(surveyId, problem);
        final AllocationSolution solution = solverJob.getFinalBestSolution();

        saveAllocation(solution);

        return solution;
    }

    @Transactional
    protected void saveAllocation(AllocationSolution solution) {
        for (GroupAssignment assignment : solution.getAssignments()) {
            if (assignment.getAssignedGroup() != null) {
                final Group group = groupRepository.findById(assignment.getAssignedGroup().getId())
                        .orElseThrow(() -> new RuntimeException("Group not found"));

                final User student = assignment.getUser();

                if (!group.getMembers().contains(student)) {
                    group.getMembers().add(student);
                    groupRepository.save(group);
                }
            }
        }
    }

    public AllocationResult getResult(Long surveyId) {
        final Survey survey = surveyService.getSurveyById(surveyId);

        if (survey == null)
            throw new UnsupportedOperationException("Survey not found id:" + surveyId);

        List<Group> groups = survey.getGroups();
        Map<Long, List<User>> groupAssignments = new HashMap<>();
        List<User> unassigned = new ArrayList<>(survey.getParticipants());

        for (Group group : groups) {
            groupAssignments.put(group.getId(), new ArrayList<>(group.getMembers()));
            unassigned.removeAll(group.getMembers());
        }

        return new AllocationResult(groupAssignments, unassigned);
    }
}
