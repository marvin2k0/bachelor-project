package org.example.backend.group;

import lombok.RequiredArgsConstructor;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;

import org.example.backend.group.dto.GroupCreationDto;
import org.example.backend.group.dto.GroupUpdateDto;
import org.example.backend.mappers.GroupMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository repository;
    private final GroupMapper mapper;

    public List<Group> getAllGroups() {
        return repository.findAll();
    }

    public Group saveGroup(GroupCreationDto groupCreationDto) {
        System.out.println("groupCreationDto = " + groupCreationDto);
        final Group group = this.mapper.toEntity(groupCreationDto);
        System.out.println("group = " + group);
        return this.repository.save(group);
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

    public Group updateGroup(Long id, GroupUpdateDto entity) {
        final Group entityFromDb = repository.findById(id)
                .orElseThrow(() -> new UnsupportedOperationException("Group not found for this id: " + id));
        this.mapper.updateEntity(entity, entityFromDb);

        return this.repository.save(entityFromDb);
    }
}