package jsp.springbootclasstask.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jsp.springbootclasstask.dao.EmployeeDao;
import jsp.springbootclasstask.dto.ResponseStructure;
import jsp.springbootclasstask.entity.Department;
import jsp.springbootclasstask.entity.Employee;
import jsp.springbootclasstask.error.IdNotFoundException;
import jsp.springbootclasstask.repository.DepartmentRepository;
import jsp.springbootclasstask.repository.EmployeeRepository;
import jsp.springbootclasstask.error.NoRecordFoundException;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeDao empdao;



	@Autowired
	private DepartmentRepository deptrepo;

	@Autowired
	private EmployeeRepository emprepo;
//    @Cacheable("emp")
	public ResponseEntity<ResponseStructure<Employee>> saveOneEmployee(Employee emp) {

		Integer deptId = emp.getDepartment().getDepartmentId();

		Optional<Department> opt = deptrepo.findById(deptId);

		ResponseStructure<Employee> res = new ResponseStructure<>();

		if (opt.isPresent()) {

			emp.setDepartment(opt.get());

			Employee e = empdao.saveEmployee(emp);

			res.setStatusCode(HttpStatus.CREATED.value());
			res.setMessage("Employee saved successfully");
			res.setData(e);

			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} else
			throw new IdNotFoundException(" Department ID " + deptId + " Does Not Exists in Department");

	}

	public ResponseEntity<ResponseStructure<List<Employee>>> getAllEmployees() {
		List<Employee> emps = empdao.fetchAllEmployee();
		ResponseStructure<List<Employee>> res = new ResponseStructure<>();
		if (!emps.isEmpty()) {
			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Fetched");
			res.setData(emps);
			return new ResponseEntity<ResponseStructure<List<Employee>>>(res, HttpStatus.OK);

		} else
			throw new NoRecordFoundException("DB IS EMPTY");
	}

	@Cacheable(value="emp",key="#p0")
	public ResponseEntity<ResponseStructure<Employee>> getEmployeeByContactNumber(Long cnumber) {

		ResponseStructure<Employee> res = new ResponseStructure<>();

		Optional<Employee> opt = empdao.fetchEmployeeByContactNumber(cnumber);
//		System.out.println("Done");

		if (opt.isPresent()) {

			res.setMessage("Employee Found");
			res.setStatusCode(HttpStatus.OK.value());
			res.setData(opt.get());

			return new ResponseEntity<ResponseStructure<Employee>>(res, HttpStatus.OK);
		}

		else
			throw new IdNotFoundException("Employee not found with " + cnumber + " in the DB");

	}


	@Cacheable(value = "empById", key = "#p0")
	public ResponseEntity<ResponseStructure<Employee>> getEmployeeById(Integer id) {
		ResponseStructure<Employee> res = new ResponseStructure<Employee>();
		Optional<Employee> opt = empdao.fetchEmployeeById(id);
		if (opt.isPresent()) {
			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Fetched By Id");
			res.setData(opt.get());
			return new ResponseEntity<ResponseStructure<Employee>>(res, HttpStatus.OK);
		} else
			throw new IdNotFoundException("Employee not found with " + id + " in the DB");
	}

//	@CachePut(value="emp",key="#result.body.data.contactNumber")
	@CachePut(value = "empById", key = "#result.body.data.employeeId")
	public ResponseEntity<ResponseStructure<Employee>> updateEmployee(Employee emp) {
		ResponseStructure<Employee> res = new ResponseStructure<Employee>();
		if (emp.getEmployeeId() == null) {
			throw new IdNotFoundException("ID is Mandatory to Update the Data");
		}

		Optional<Employee> opt = empdao.fetchEmployeeById(emp.getEmployeeId());
		if (opt.isPresent()) {
			empdao.updateEmployee(emp);
			res.setMessage("Updated");
			res.setStatusCode(HttpStatus.OK.value());
			res.setData(opt.get());
			return new ResponseEntity<ResponseStructure<Employee>>(res, HttpStatus.OK);

		} else
			throw new IdNotFoundException("Employee not found with " + emp.getEmployeeId() + " in the DB");
	}

	@CacheEvict(value="empById",key="#p0")
	public ResponseEntity<ResponseStructure<String>> deleteEmployee(Integer id) {
		ResponseStructure<String> res = new ResponseStructure<>();

		Optional<Employee> opt = empdao.fetchEmployeeById(id);


		if (opt.isPresent()) {

			empdao.deleteEmployee(opt.get());

			res.setMessage("Record Deleted");
			res.setStatusCode(HttpStatus.OK.value());
			res.setData("Success");

			return new ResponseEntity<>(res, HttpStatus.OK);
		}

		else {
			throw new IdNotFoundException("Employee not found with " + id + " in the DB");
		}
	}



	public ResponseEntity<ResponseStructure<List<Employee>>> getEmployeeBySalaryMoreThan(Double sal) {
		List<Employee> empls = empdao.fetchEmployeesBySalaryMoreThan(sal);
		ResponseStructure<List<Employee>> res = new ResponseStructure<>();

		if (!empls.isEmpty()) {
			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Employees records with salary  " + "greater than" + sal + " is retrieved");
			res.setData(empls);

			return new ResponseEntity<ResponseStructure<List<Employee>>>(res, HttpStatus.OK);

		} else
			throw new NoRecordFoundException("No Matching Record Found");
	}

	public ResponseEntity<ResponseStructure<Long>> countNumberOfEmployeeInDepartment(Integer id) {

		Long count = empdao.countEmployeeByDepartment(id);
		ResponseStructure<Long> res = new ResponseStructure<>();

		res.setStatusCode(HttpStatus.OK.value());
		res.setMessage("Employees in DepartmentId " + id + " are " + count);
		res.setData(count);

		return new ResponseEntity<ResponseStructure<Long>>(res, HttpStatus.OK);

	}

	public ResponseEntity<ResponseStructure<List<Employee>>> getAllEmployeesByDepartmentId(Integer id) {

		ResponseStructure<List<Employee>> res = new ResponseStructure<>();
		Optional<Department> opt = deptrepo.findById(id);

		if (opt.isPresent()) {
			List<Employee> emps = empdao.fetchEmployeeByDepartmentId(id);
			if (!emps.isEmpty()) {
				res.setMessage("Employess Present in Department with Id " + id + " are");
				res.setStatusCode(HttpStatus.OK.value());
				res.setData(emps);
				return new ResponseEntity<ResponseStructure<List<Employee>>>(res, HttpStatus.OK);
			}

			else
				throw new NoRecordFoundException("No Employees are there in given Department Id " + id);

		}

		else
			throw new IdNotFoundException("Invalid Id");

	}

	public ResponseEntity<ResponseStructure<Employee>> transferEmployee(Integer eid, Integer did) {

	    Optional<Employee> opt = emprepo.findById(eid);

	    if (opt.isPresent()) {

	        Optional<Department> opt2 = deptrepo.findById(did);

	        if (opt2.isPresent()) {

	            Employee emp = opt.get();

	            emp.setDepartment(opt2.get());

	            Employee e = empdao.saveEmployee(emp);

	            ResponseStructure<Employee> res = new ResponseStructure<>();

	            res.setStatusCode(HttpStatus.OK.value());
	            res.setMessage("Employee transferred successfully");
	            res.setData(e);

	            return new ResponseEntity<ResponseStructure<Employee>>(res, HttpStatus.OK);
	        }

	        else {
	            throw new IdNotFoundException("Department ID " + did + " does not exist");
	        }
	    }

	    else {
	        throw new IdNotFoundException("Invalid Employee ID " + eid + " to transfer");
	    }
	}

	public ResponseEntity<ResponseStructure<Page<Employee>>> getEmployeeBySortingAndPaging(Integer pN, Integer pS,
			String field) {
		
		Page<Employee> emps=empdao.getEmployeeBySortingAndPage(pN,pS,field);
		ResponseStructure<Page<Employee>> res = new ResponseStructure<>();
		if(!emps.isEmpty())
		{
			res.setStatusCode(HttpStatus.FOUND.value());
			res.setMessage("Record Retrieved By Sorting");
			res.setData(emps);
			return new ResponseEntity<ResponseStructure<Page<Employee>>>(res,HttpStatus.FOUND);
			
		}
		else throw new NoRecordFoundException("DB Is Empty");
	}

}
