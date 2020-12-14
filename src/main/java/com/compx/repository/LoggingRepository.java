package com.compx.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.compx.model.FailureEmployee;

public interface LoggingRepository extends MongoRepository<FailureEmployee, String>  {

	FailureEmployee findByFileName(String filename);

}
