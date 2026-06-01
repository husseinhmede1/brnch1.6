package com.mdsl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdsl.model.entity.Employee;
import com.mdsl.model.entity.Institution;

@Repository
public interface EmployeeRepository  extends JpaRepository<Employee, Integer>{
	Optional<Employee> findByInstitutionAndStatusAndEmployeeName(Institution institution, Character status, String name);
	List<Employee> findByStatus(char value,Sort sort);
	List<Employee> findByInstitution(Institution institution, Sort sort);
	List<Employee> findByCreatedBy(int createdBy);

	Boolean existsByInstitutionAndEmployeeNameIgnoreCase(Institution institution,String name);
	Boolean existsByInstitutionAndEmployeeNameIgnoreCaseAndEmployeeIdNot(Institution institution,String name,Integer employeeId);
	
}
