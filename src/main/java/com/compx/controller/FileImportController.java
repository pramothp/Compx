package com.compx.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream; 
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.compx.model.FailureEmployee;
import com.compx.repository.LoggingRepository;
import com.compx.service.FileImportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class FileImportController {
	org.slf4j.Logger logger = LoggerFactory.getLogger(FileImportController.class);
	
	@Autowired
	ObjectMapper om;

	@Autowired
	FileImportService fileImportService;
	
	@Autowired
	LoggingRepository loggingRepository; 
	
	@Autowired
	HttpServletResponse response;


    @PostMapping(value = "/compx/fileImport", consumes = "multipart/form-data")
    public String fileImportMulti(@RequestParam("file") MultipartFile file) {
    	String response ="";
    	try {
			byte[] bytes=file.getBytes();
			logger.info("FileName:"+file.getOriginalFilename());
			Path path = Paths.get("./"+file.getOriginalFilename());
			Files.write( path, bytes);
			response = fileImportService.fileImport(path, file.getOriginalFilename(), bytes);
			Files.delete(path);
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = "Failure";
		}
    	
    	return response;
    }
	
	/*
	 * @PostMapping(value = "/compx/download") public void
	 * filedownload(@RequestParam String filename, HttpServletResponse response) {
	 * 
	 * FailureEmployee failureEmployee = loggingRepository.findByFileName(filename);
	 * try (OutputStream outStream = response.getOutputStream()){
	 * response.setContentType("application/csv"); String headerKey =
	 * "content-Disposition"; String headerValue =
	 * String.format("attachment; fileName=\"%s\"", filename);
	 * response.setHeader(headerKey, headerValue);
	 * 
	 * outStream.write(failureEmployee.getFile()); outStream.flush();
	 * }catch(Exception ex) {
	 * 
	 * } }
	 */    
    @RequestMapping(path = "/compx/download", method = RequestMethod.POST)
    public ResponseEntity<ByteArrayResource> download(@RequestBody String param) 
 {
    	try {
    		FailureEmployee failureEmployee = loggingRepository.findByFileName(param);
        	Path path = Paths.get("./"+failureEmployee.getFileName());
    		Files.write( path, failureEmployee.getFile());

    		OutputStream os = new FileOutputStream(failureEmployee.getFileName()); 
    		os.write(failureEmployee.getFile()); 
        System.out.println("Successfully"
                           + " byte inserted"); 

        // Close the file 
        os.close(); 

            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes
    (path));
            HttpHeaders header = new HttpHeaders();
            
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + 
    param);
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");
            return ResponseEntity.ok().headers(header).
                    contentType(MediaType.parseMediaType("text/csv")).body(resource);

    	}catch(Exception ex) {
    		logger.error(ex.getMessage());
    	}
    	return null;
    
 	}

	
}
