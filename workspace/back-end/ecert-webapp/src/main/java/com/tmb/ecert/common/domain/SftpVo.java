package com.tmb.ecert.common.domain;

import java.util.ArrayList;
import java.util.List;

public class SftpVo {

	private String host;
	private String username;
	private String password;
	private String path;
	private String fileName;
	private List<SftpFileVo> files = new ArrayList<>();
	
	public SftpVo() {
		super();
	}
	public SftpVo(String host, String username, String password, String path, String fileName) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
		this.path = path;
		this.fileName = fileName;
	}
	
	public SftpVo(String host, String username, String password, List<SftpFileVo> files) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
		this.files = files;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public List<SftpFileVo> getFiles() {
		return files;
	}
	public void setFiles(List<SftpFileVo> files) {
		this.files = files;
	}
	
}
