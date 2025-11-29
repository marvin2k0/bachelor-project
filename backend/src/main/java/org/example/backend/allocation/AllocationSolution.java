package org.example.backend.allocation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend.group.Group;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PlanningSolution
public class AllocationSolution {
    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "groupRange")
    private List<Group> groups;

    @PlanningEntityCollectionProperty
    private List<GroupAssignment> assignments;

    @PlanningScore
    private HardSoftScore score;

    public AllocationSolution(List<Group> groups, List<GroupAssignment> assignments) {
        this.groups = groups;
        this.assignments = assignments;
    }
}
