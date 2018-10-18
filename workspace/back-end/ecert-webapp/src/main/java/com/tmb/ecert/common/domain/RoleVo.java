package com.tmb.ecert.common.domain;

public class RoleVo {
	private Long roldId;
	private String roleName;
	private Integer status;
	private String rolePermissionId;
	private String functionCode;

	public Long getRoldId() {
		return roldId;
	}

	public void setRoldId(Long roldId) {
		this.roldId = roldId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRolePermissionId() {
		return rolePermissionId;
	}

	public void setRolePermissionId(String rolePermissionId) {
		this.rolePermissionId = rolePermissionId;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
}
