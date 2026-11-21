package jsp.springbootclasstask.controller;

import java.util.List;

import jsp.springbootclasstask.service.CacheInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
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
import jsp.springbootclasstask.entity.Employee;
import jsp.springbootclasstask.service.EmployeeService;

@RestController
@RequestMapping("/api/emp")
public class EmployeeController {
	
	@Autowired
	private EmployeeService empservice;

	@Autowired
	private CacheInspectionService cacheInspectionService;

	@PostMapping
	public ResponseEntity<ResponseStructure<Employee>> saveEmployee(@RequestBody Employee emp) {
		return empservice.saveOneEmployee(emp);
	}
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Employee>>> getAllEmployees() {

		return empservice.getAllEmployees();

	}
	
	@GetMapping("/{Id}")
	public ResponseEntity<ResponseStructure<Employee>> getEmployeeById(@PathVariable Integer Id) {

		return empservice.getEmployeeById(Id);

	}
	
	@GetMapping("/contact/{cnumber}")
	public ResponseEntity<ResponseStructure<Employee>> getEmployeeByContactNumber(@PathVariable Long cnumber) {

		return empservice.getEmployeeByContactNumber(cnumber);

	}



	@GetMapping("/cache")
	public String inspectCache() {

//		cacheInspectionService.printCacheContents("emp");
		cacheInspectionService.printCacheContents("empById");

		return "Check IntelliJ console for cache contents";
	}
	
	@GetMapping("/salary/{salary}")
	public ResponseEntity<ResponseStructure<List<Employee>>> getEmployeeBySalaryMoreThan(@PathVariable Double salary) {

		return empservice.getEmployeeBySalaryMoreThan(salary);

	}
	
	@GetMapping("/count/{Id}")
	public ResponseEntity<ResponseStructure<Long>> getEmployeeBySalaryMoreThan(@PathVariable Integer Id) {

		return empservice.countNumberOfEmployeeInDepartment(Id);

	}
	
	
	@GetMapping("/deptId/{Id}")
	public ResponseEntity<ResponseStructure<List<Employee>>> getAllEmployees(@PathVariable Integer Id) {

		return empservice.getAllEmployeesByDepartmentId(Id);

	}
	
	
	@GetMapping("/page/{pN}/{pS}/field/{field}")
	 public ResponseEntity<ResponseStructure<Page<Employee>>> getEmployeeByPagingAndSorting(@PathVariable Integer pN, @PathVariable Integer pS, @PathVariable String field)
	 {
		return empservice.getEmployeeBySortingAndPaging(pN,pS,field);
	 }
	
	
	

	@PutMapping
	public ResponseEntity<ResponseStructure<Employee>> updateEmployee(@RequestBody Employee emp) {
		return empservice.updateEmployee(emp);

	}
	
	@PutMapping("/transfer/empId/{Id}/dId/{dId}")
	public ResponseEntity<ResponseStructure<Employee>> transferEmployee(@PathVariable Integer Id,@PathVariable Integer dId) {
		return empservice.transferEmployee(Id,dId);

	}
	
	
	
	@DeleteMapping("/{Id}")
	public ResponseEntity<ResponseStructure<String>> deleteEmployee(@PathVariable Integer Id) {
		return empservice.deleteEmployee(Id);

	}
	
	

}
