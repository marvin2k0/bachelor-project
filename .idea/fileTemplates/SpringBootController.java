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
@RequestMapping("/api/v1/$NAME.toLowerCase()")
@RequiredArgsConstructor
public class ${NAME}Controller {
    private final ${NAME}Service service;
	
	@GetMapping("/")
	public ResponseEntity<List<${NAME}>> getAll${NAME}s() {
		return ResponseEntity.ok(service.getAll${NAME}s());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<${NAME}> get${NAME}Details(@PathVariable Long id) {
		return ResponseEntity.ok(service.get${NAME}ById(id));
	}

	@PostMapping("/")
	public ResponseEntity<${NAME}> add${NAME}(@RequestBody ${NAME} ${CamelCaseName}) {
		return ResponseEntity.ok(service.save${NAME}(${CamelCaseName}));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<${NAME}> update${NAME}(@PathVariable Long id, @RequestBody ${NAME} ${CamelCaseName}) {
		return ResponseEntity.ok(service.update${NAME}(id, ${CamelCaseName}));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete${NAME}(@PathVariable Long id) {
	    service.delete${NAME}ById(id);
		return ResponseEntity.ok().build();
	}
}