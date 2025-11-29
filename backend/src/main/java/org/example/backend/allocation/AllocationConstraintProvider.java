package org.example.backend.allocation;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintCollectors;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

public class AllocationConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                groupCapacity(constraintFactory)
        };
    }

    public Constraint groupCapacity(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(GroupAssignment.class)
                .filter(assignment -> assignment.getAssignedGroup() != null)
                .groupBy(GroupAssignment::getAssignedGroup, ConstraintCollectors.count())
                .filter((group, count) -> count > group.getCapacity())
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Group capacity exceeded");
    }
}
