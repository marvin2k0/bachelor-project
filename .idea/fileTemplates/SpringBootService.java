#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ${NAME}Service{
	private final ${NAME}Repository repository;
	
	public List<${NAME}> getAll${NAME}s() {
		return repository.findAll();
	}

	public ${NAME} save${NAME}(${NAME} entity) {		
		return this.repository.save(entity);
	}

	public ${NAME} get${NAME}ById(long id) {
		final Optional<${NAME}> optional = repository.findById(id);
		${NAME} entity;
		if(optional.isPresent()) {
			entity = optional.get();
		} else {
			throw new RuntimeException("entity not found for id:"+id);
		}
		return entity;
	}

	public void delete${NAME}ById(long id) {
		this.repository.deleteById(id);		
	}
	
	public ${NAME} update${NAME}(Long id, ${NAME} entity) {
	
		final ${NAME} entityFromDb = repository.findById(id)
                .orElseThrow(() -> new UnsupportedOperationException("${NAME} not found for this id :: " + id));
	
		entityFromDb.setId(id);
		this.repository.save(entityFromDb);
		return entityFromDb;
	}
}