package jsp.springbootclasstask.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jsp.springbootclasstask.dao.DepartmentDao;
import jsp.springbootclasstask.dao.EmployeeDao;
import jsp.springbootclasstask.dto.ResponseStructure;
import jsp.springbootclasstask.entity.Department;
import jsp.springbootclasstask.entity.Employee;
import jsp.springbootclasstask.error.IdNotFoundException;
import jsp.springbootclasstask.error.NoRecordFoundException;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentDao deptdao;

	@Autowired
	private EmployeeDao empdao;

	public ResponseEntity<ResponseStructure<Department>> saveOneDepartment(Department dept) {

		Department d = deptdao.saveDepartment(dept);
		ResponseStructure<Department> res = new ResponseStructure<>();

		res.setStatusCode(HttpStatus.CREATED.value());
		res.setMessage("Saved");
		res.setData(d);
		return new ResponseEntity<ResponseStructure<Department>>(res, HttpStatus.CREATED);
	}

	public ResponseEntity<ResponseStructure<List<Department>>> getAllDepartments() {
		List<Department> depts = deptdao.fetchAllDepartment();
		ResponseStructure<List<Department>> res = new ResponseStructure<>();
		if (!depts.isEmpty()) {
			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Fetched");
			res.setData(depts);
			return new ResponseEntity<ResponseStructure<List<Department>>>(res, HttpStatus.OK);

		} else
			throw new NoRecordFoundException("DB IS EMPTY");
	}

	public ResponseEntity<ResponseStructure<Department>> getDepartmentById(Integer id) {
		ResponseStructure<Department> res = new ResponseStructure<Department>();
		Optional<Department> opt = deptdao.fetchDepartmentById(id);
		if (opt.isPresent()) {
			res.setStatusCode(HttpStatus.OK.value());
			res.setMessage("Fetched By Id");
			res.setData(opt.get());
			return new ResponseEntity<ResponseStructure<Department>>(res, HttpStatus.OK);
		} else
			throw new IdNotFoundException("Department not found with " + id + " in the DB");

	}

	public ResponseEntity<ResponseStructure<Department>> updateDepartment(Department dept) {
		ResponseStructure<Department> res = new ResponseStructure<Department>();
		if (dept.getDepartmentId() == null) {
			throw new IdNotFoundException("ID is Mandatory to Update the Data");
		}

		Optional<Department> opt = deptdao.fetchDepartmentById(dept.getDepartmentId());
		if (opt.isPresent()) {
			deptdao.saveDepartment(dept);
			res.setMessage("Updated");
			res.setStatusCode(HttpStatus.OK.value());
			res.setData(opt.get());
			return new ResponseEntity<ResponseStructure<Department>>(res, HttpStatus.OK);

		} else
			throw new IdNotFoundException("Department not found with " + dept.getDepartmentId() + " in the DB");
	}

	public ResponseEntity<ResponseStructure<String>> deleteDepartment(Integer id) {

	    ResponseStructure<String> res = new ResponseStructure<>();

	    Optional<Department> opt = deptdao.fetchDepartmentById(id);

	    if (opt.isPresent()) {

	        List<Employee> employees = empdao.fetchEmployeeByDepartmentId(id);

	        if (!employees.isEmpty()) {
	            throw new DataIntegrityViolationException("Department cannot be deleted. Employees are working in this department.");
	        }

	        deptdao.deleteDepartment(opt.get());

	        res.setMessage("Department Deleted");
	        res.setStatusCode(HttpStatus.OK.value());
	        res.setData("Success");

	        return new ResponseEntity<ResponseStructure<String>>(res, HttpStatus.OK);
	    }

	    else {
	        throw new IdNotFoundException("Department not found with " + id + " in the DB");
	    }
	}

	public ResponseEntity<ResponseStructure<List<Department>>> getDepartmentByName(String dname) {
		List<Department> depts = deptdao.fetchDepartmentByDeptName(dname);
		ResponseStructure<List<Department>> res = new ResponseStructure<>();

		if (!depts.isEmpty()) {
			res.setStatusCode(HttpStatus.FOUND.value());
			res.setMessage("Department record with Departname  " + dname + " is retrieved");
			res.setData(depts);

			return new ResponseEntity<ResponseStructure<List<Department>>>(res, HttpStatus.FOUND);

		} else
			throw new NoRecordFoundException("No Matching Record Found");
	}

}
