#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end
#set( $CamelCaseName = "$NAME.substring(0,1).toLowerCase()$NAME.substring(1)" )

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import ${PACKAGE_NAME}.repositories.${NAME}Repository;

@Service
@Transactional
@RequiredArgsConstructor
public class ${Name}Service{

	@Autowired
	private ${Name}Repository ${CamelCaseName}Repository;
	
	public List<${Name}> getAll${Name}s() {
		return ${CamelCaseName}Repository.findAll();
	}

	public void save${Name}(${Name} ${CamelCaseName}) {		
		this.${CamelCaseName}Repository.save(${CamelCaseName});
	}

	public ${Name} get${Name}ById(long id) {
		final Optional<${Name}> optional = ${CamelCaseName}Repository.findById(id);
		${Name} ${CamelCaseName} = null;
		if(optional.isPresent()) {
			${CamelCaseName} = optional.get();
		} else {
			throw new RuntimeException("${CamelCaseName} not found for id:"+id);
		}
		return ${CamelCaseName};
	}

	public void delete${Name}ById(long id) {
		this.${CamelCaseName}Repository.deleteById(id);		
	}
	
	public static ${NAME} update${NAME}(Long ${CamelCaseName}Id, ${NAME} ${CamelCaseName}) {
		${NAME} ${CamelCaseName} = ${CamelCaseName}Repository.findById(${CamelCaseName}Id)
                .orElseThrow(() -> new ResourceNotFoundException("${NAME} not found for this id :: " + ${CamelCaseName}Id));
	
		${CamelCaseName}.setId(${CamelCaseName}Id);
		this.${CamelCaseName}Repository.save(${CamelCaseName});
		return ${CamelCaseName};
	}
}