package jsp.springbootclasstask.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jsp.springbootclasstask.dto.ResponseStructure;
import jsp.springbootclasstask.entity.Department;
import jsp.springbootclasstask.service.DepartmentService;

@RestController
@RequestMapping("/api/deptartment")
public class DepartmentController {
	@Autowired
	private DepartmentService deptservice;

	@PostMapping
	public ResponseEntity<ResponseStructure<Department>> saveDepartment(@RequestBody Department dept) {
		return deptservice.saveOneDepartment(dept);
	}
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Department>>> getAllDepartments() {

		return deptservice.getAllDepartments();

	}
	
	@GetMapping("/{Id}")
	public ResponseEntity<ResponseStructure<Department>> getDepartmentById(@PathVariable Integer Id) {

		return deptservice.getDepartmentById(Id);

	}
	
	@GetMapping("/dname/{dname}")
	public ResponseEntity<ResponseStructure<List<Department>>> getDepartmentByName(@PathVariable String dname) {

		return deptservice.getDepartmentByName(dname);
	}
	
	
	
	
	@PutMapping
	public ResponseEntity<ResponseStructure<Department>> updateDepartment(@RequestBody Department dept) {
		return deptservice.updateDepartment(dept);

	}
	
	@DeleteMapping("/{Id}")
	public ResponseEntity<ResponseStructure<String>> deleteDepartment(@PathVariable Integer Id) {
		return deptservice.deleteDepartment(Id);

	}

}
