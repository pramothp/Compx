package com.compx.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("failureEmployee")
public class FailureEmployee {

	String Id;
	String fileName;
	byte[] file;
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
}
