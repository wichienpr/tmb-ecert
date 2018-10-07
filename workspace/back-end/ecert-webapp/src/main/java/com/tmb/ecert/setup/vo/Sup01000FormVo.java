package com.tmb.ecert.setup.vo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.tmb.ecert.common.domain.RoleVo;

public class Sup01000FormVo {
	private List<RoleVo> listRole;
	private MultipartFile fileUpload;

	public List<RoleVo> getListRole() {
		return listRole;
	}

	public void setListRole(List<RoleVo> listRole) {
		this.listRole = listRole;
	}

	public MultipartFile getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(MultipartFile fileUpload) {
		this.fileUpload = fileUpload;
	}
	
	

}
