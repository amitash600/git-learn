package jsp.springbootclasstask.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jsp.springbootclasstask.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	
	boolean existsByContactNumber(Long contactNumber);
	
	boolean existsByContactNumberAndEmployeeIdNot(Long contactNumber, Integer employeeId);
	
	List<Employee> findByDepartment_DepartmentId(Integer departmentId);
	
	List<Employee> findByDepartment(Integer departmentId);
	
	Optional<Employee> findByContactNumber(Long contactNumber);
	
	List<Employee> findBySalaryGreaterThan(Double salary);

	Long countByDepartment_DepartmentId(Integer departmentId);
	
	
	

}
