package jsp.springbootclasstask.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jsp.springbootclasstask.entity.Department;
import jsp.springbootclasstask.entity.Employee;
import jsp.springbootclasstask.repository.DepartmentRepository;

@Repository
public class DepartmentDao {

	@Autowired
	private DepartmentRepository deptrepo;

	public Department saveDepartment(Department dept) {

		List<Employee> employees = dept.getEmployee();

		if (employees != null) {
			for (Employee e : employees) {
				e.setDepartment(dept);
			}
		}

		return deptrepo.save(dept);
	}

	public List<Department> fetchAllDepartment() {
		return deptrepo.findAll();
	}

	public Optional<Department> fetchDepartmentById(Integer id) {
		return deptrepo.findById(id);
	}

	public void deleteDepartment(Department dept) {
		deptrepo.delete(dept);
	}

	public List<Department> fetchDepartmentByDeptName(String dname) {
		return deptrepo.findByDepartmentName(dname);
	}

}
