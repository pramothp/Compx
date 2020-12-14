package com.compx.service;

import com.compx.model.*;
import com.compx.repository.EmployeeRepository;
import com.compx.repository.LoggingRepository;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileImportService {
	org.slf4j.Logger logger = LoggerFactory.getLogger(FileImportService.class);
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired 
	LoggingRepository loggingRepository;
	
	public String fileImport(Path path, String fileName, byte[] bytes) {
		List<Employee> employees = new ArrayList<>();
		String response = "Success";
		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.US_ASCII)) { 
			String line = br.readLine();
			 while (line != null ) {
				 try {
					 if(!line.startsWith("Username")) {
						 String[] attributes = line.split(",");
						 Employee employee = importUser(attributes);
						 if(employee.getEmailaddress()!=null && !employee.getEmailaddress().isEmpty()) {
							 employees.add(employee);
							 employeeRepository.save(employee);
						 } else {
							saveFile(fileName, bytes); 
							response = "Failure";
						 }
						
					 }
					 line = br.readLine();
				 }catch(Exception ex) {
					 logger.error("Error in data Uploading"+ex.getMessage());
				 }
			 }
			 return response;
		}  catch (IOException ioe){
			saveFile(fileName, bytes);
			logger.error("Error in FileImport Service:"+ioe.getMessage());
			return "Failure";
		}
	}
	
	public Employee importUser(String[] data) {
		Employee employee = new Employee();
		employee.setUsername(data[0]);
		employee.setFirstname(data[1]);
		employee.setLastname(data[2]);
		employee.setEmailaddress(data[3]);
		employee.setPassword(data[4]);
		employee.setAddress(data[5]);
		return employee;
	}
	
	public void saveFile(String fileName, byte[] bytes) {
		List<FailureEmployee> failureEmployee = loggingRepository.findByFileName(fileName);
		 FailureEmployee femployee = new FailureEmployee();
		 if(failureEmployee!= null && failureEmployee.size()>0) {
			 femployee.setId(failureEmployee.get(0).getId());
			 femployee.setFileName(fileName);
			 femployee.setFile(bytes);
		 } else {
			 femployee.setFileName(fileName); 
			 femployee.setFile(bytes);
		 }
		 loggingRepository.save(femployee);
	}
}
