package com.tmb.ecert.common.domain;

import java.io.File;

public class SftpFileVo {

	private File file;
	private String path;
	private String fileName;
	
	public SftpFileVo() {
		super();
	}
	
	/**
	 * Using Get File Sftp 
	 */
	public SftpFileVo(String path, String fileName) {
		super();
		this.path = path;
		this.fileName = fileName;
	}

	/**
	 * Using Put File Sftp 
	 */
	public SftpFileVo(File file, String path, String fileName) {
		super();
		this.file = file;
		this.path = path;
		this.fileName = fileName;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
