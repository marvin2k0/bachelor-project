#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end
#set( $CamelCaseName = "$NAME.substring(0,1).toLowerCase()$NAME.substring(1)" )

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ${NAME}Controller {
    private final ${NAME}Service service;
	
	@GetMapping("/")
	public ResponseEntity<List<${NAME}>> getAll${NAME}s() {
		return ResponseEntity.ok(service.getAll${NAME}s());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> get${NAME}Details(@PathVariable Long ${CamelCaseName}Id) {
		return service.get${NAME}Details(${CamelCaseName}Id);
	}

	@PostMapping("/")
	public ResponseEntity<${NAME}> add${NAME}(@RequestBody ${NAME} ${CamelCaseName}) {
		return service.add${NAME}(${CamelCaseName});
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<${NAME}> update${NAME}(@PathVariable Long ${CamelCaseName}Id, @RequestBody ${NAME} ${CamelCaseName}) {
		return ResponseEntity.ok(service.update${NAME}(${CamelCaseName}Id, ${CamelCaseName}));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<${NAME}> delete${NAME}(@PathVariable Long ${CamelCaseName}Id) {
		return ResponseEntity.ok(service.delete${NAME}(${CamelCaseName}Id));
	}
}