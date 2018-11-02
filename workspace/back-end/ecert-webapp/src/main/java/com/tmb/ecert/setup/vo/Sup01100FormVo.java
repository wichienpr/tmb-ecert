package com.tmb.ecert.setup.vo;

import java.util.List;

import com.tmb.ecert.common.domain.RoleVo;

import th.co.baiwa.buckwaframework.common.bean.DatatableRequest;

public class Sup01100FormVo  extends DatatableRequest {
	private Long roleId;
	private String roleName;
	private int status;
	private List<RoleVo> rolePermission;
	
	
	
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<RoleVo> getRolePermission() {
		return rolePermission;
	}
	public void setRolePermission(List<RoleVo> rolePermission) {
		this.rolePermission = rolePermission;
	} 
	
	
	

}
