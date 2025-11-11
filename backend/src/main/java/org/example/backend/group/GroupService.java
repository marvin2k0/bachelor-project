package org.example.backend.group;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository repository;

    public List<Group> getAllGroups() {
        return repository.findAll();
    }

    public Group saveGroup(Group entity) {
        return this.repository.save(entity);
    }

    public Group getGroupById(long id) {
        final Optional<Group> optional = repository.findById(id);
        Group entity;
        if (optional.isPresent()) {
            entity = optional.get();
        } else {
            throw new RuntimeException("entity not found for id:" + id);
        }
        return entity;
    }

    public void deleteGroupById(long id) {
        this.repository.deleteById(id);
    }

    public Group updateGroup(Long id, Group entity) {

        final Group entityFromDb = repository.findById(id)
                .orElseThrow(() -> new UnsupportedOperationException("Group not found for this id :: " + id));

        entityFromDb.setId(id);
        this.repository.save(entityFromDb);
        return entityFromDb;
    }
}