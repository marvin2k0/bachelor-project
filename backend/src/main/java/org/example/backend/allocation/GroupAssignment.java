package org.example.backend.allocation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend.group.Group;
import org.example.backend.groupPreference.GroupPreference;
import org.example.backend.user.User;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PlanningEntity
public class GroupAssignment {
    @PlanningId
    private long id;
    private User user;
    @PlanningVariable(valueRangeProviderRefs = "groupRange")
    private Group assignedGroup;

    private List<GroupPreference> preferences;

    public GroupAssignment(long id, User user, List<GroupPreference> preferences) {
        this.user = user;
        this.id = id;
        this.preferences = preferences;
    }

    public Integer getPriorityForGroup(Group group) {
        if (group == null || preferences == null)
            return null;

        return preferences.stream()
                .filter(pref -> pref.getGroup().getId().equals(group.getId()))
                .map(GroupPreference::getPriority)
                .findFirst()
                .orElse(null);
    }
}
