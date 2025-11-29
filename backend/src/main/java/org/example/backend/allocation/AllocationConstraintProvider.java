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
                groupCapacity(constraintFactory),
                maximizePriorityFulfillment(constraintFactory)
        };
    }

    public Constraint groupCapacity(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(GroupAssignment.class)
                .filter(assignment -> assignment.getAssignedGroup() != null)
                .groupBy(GroupAssignment::getAssignedGroup, ConstraintCollectors.count())
                .filter((group, count) -> count > group.getCapacity())
                .penalize(HardSoftScore.ONE_HARD, (group, count) -> count - group.getCapacity())
                .asConstraint("Group capacity exceeded");
    }

    public Constraint maximizePriorityFulfillment(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(GroupAssignment.class)
                .filter(assignment -> assignment.getAssignedGroup() != null)
                .filter(assignment -> assignment.getPriorityForGroup(assignment.getAssignedGroup()) != null)
                .penalize(HardSoftScore.ONE_SOFT,
                        assignment -> assignment.getPriorityForGroup(assignment.getAssignedGroup()))
                .asConstraint("Maximize priority fulfillment");
    }
}
