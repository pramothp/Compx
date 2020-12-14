package com.compx.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("failureEmployee")
public class FailureEmployee {

	String fileName;
	byte[] file;
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
