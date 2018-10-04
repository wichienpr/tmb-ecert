package com.tmb.ecert.common.domain;

import java.util.ArrayList;
import java.util.List;

public class SftpVo {

	private String host;
	private String username;
	private String password;
	private SftpFileVo getFile = new SftpFileVo();
	private List<SftpFileVo> putFiles = new ArrayList<>();
	
	public SftpVo() {
		super();
	}

	public SftpVo(SftpFileVo getFile, String host, String username, String password) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
		this.getFile = getFile;
	}

	public SftpVo(List<SftpFileVo> putFiles, String host, String username, String password) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
		this.putFiles = putFiles;
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

	public SftpFileVo getGetFile() {
		return getFile;
	}

	public void setGetFile(SftpFileVo getFile) {
		this.getFile = getFile;
	}

	public List<SftpFileVo> getPutFiles() {
		return putFiles;
	}

	public void setPutFiles(List<SftpFileVo> putFiles) {
		this.putFiles = putFiles;
	}
	
}
