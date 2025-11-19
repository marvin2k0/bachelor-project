package org.example.backend.groupPreference;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupPreferenceService {
    private final GroupPreferenceRepository repository;

    public List<GroupPreference> getAllGroupPreferences() {
        return repository.findAll();
    }

    public GroupPreference saveGroupPreference(GroupPreference entity) {
        return this.repository.save(entity);
    }

    public GroupPreference getGroupPreferenceById(long id) {
        final Optional<GroupPreference> optional = repository.findById(id);
        GroupPreference entity;
        if (optional.isPresent()) {
            entity = optional.get();
        } else {
            throw new RuntimeException("entity not found for id:" + id);
        }
        return entity;
    }

    public void deleteGroupPreferenceById(long id) {
        this.repository.deleteById(id);
    }

    public GroupPreference updateGroupPreference(Long id, GroupPreference entity) {

        final GroupPreference entityFromDb = repository.findById(id)
                .orElseThrow(() -> new UnsupportedOperationException("GroupPreference not found for this id :: " + id));

        entityFromDb.setId(id);
        this.repository.save(entityFromDb);
        return entityFromDb;
    }
}