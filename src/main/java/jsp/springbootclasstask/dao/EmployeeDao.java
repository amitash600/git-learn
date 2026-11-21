package jsp.springbootclasstask.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import jsp.springbootclasstask.entity.Employee;
import jsp.springbootclasstask.repository.EmployeeRepository;

@Repository
public class EmployeeDao {

	@Autowired
	private EmployeeRepository emprepo;

	public void display() {
		System.out.println("Working on feature branch");
	}

	public Employee saveEmployee(Employee emp) {

		Long contactNo = emp.getContactNumber();

		if (emp.getSalary() == null || emp.getSalary() <= 0) {
			throw new DataIntegrityViolationException("Salary must be greater than zero");
		}

		if (String.valueOf(contactNo).length() != 10) {
			throw new DataIntegrityViolationException("Contact number must be exactly 10 digits");
			
		}

		  if (emprepo.existsByContactNumberAndEmployeeIdNot(contactNo, emp.getEmployeeId())) {
		        throw new DataIntegrityViolationException("Contact number already exists");
		    }

		return emprepo.save(emp);
	}

	public List<Employee> fetchAllEmployee() {
		return emprepo.findAll();
	}

	public Optional<Employee> fetchEmployeeById(Integer id) {
		return emprepo.findById(id);
	}

	public Employee updateEmployee(Employee emp) {

		if (emp.getSalary() == null || emp.getSalary() <= 0) {
			throw new DataIntegrityViolationException("Salary must be greater than zero");
		}

		return emprepo.save(emp);

	}

	public List<Employee> fetchEmployeeByDepartmentId(Integer id) {
		return emprepo.findByDepartment_DepartmentId(id);
	}

	public void deleteEmployee(Employee employee) {
		emprepo.delete(employee);

	}

	public Optional<Employee> fetchEmployeeByContactNumber(Long cnumber) {
		return emprepo.findByContactNumber(cnumber);
	}

	public List<Employee> fetchEmployeesBySalaryMoreThan(Double salary) {
		return emprepo.findBySalaryGreaterThan(salary);
	}

	public Long countEmployeeByDepartment(Integer id) {
		return emprepo.countByDepartment_DepartmentId(id);
	}

	public Page<Employee> getEmployeeBySortingAndPage(Integer pN, Integer pS, String field) {
		return emprepo.findAll(PageRequest.of(pN, pS,Sort.by(field).descending()));
	}

}
