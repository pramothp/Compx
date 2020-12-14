package com.compx.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.compx.model.FailureEmployee;

public interface LoggingRepository extends MongoRepository<FailureEmployee, String>  {

	List<FailureEmployee> findByFileName(String filename);

}
