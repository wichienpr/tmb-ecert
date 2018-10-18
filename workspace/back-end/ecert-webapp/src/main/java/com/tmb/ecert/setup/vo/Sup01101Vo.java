package com.tmb.ecert.setup.vo;

import java.util.List;

public class Sup01101Vo {
	private String roleName ;
	private int status;
	private String fuctioncode;
	private List<Sup01102Vo> chliddata;
	
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
	public String getFuctioncode() {
		return fuctioncode;
	}
	public void setFuctioncode(String fuctioncode) {
		this.fuctioncode = fuctioncode;
	}
	public List<Sup01102Vo> getChliddata() {
		return chliddata;
	}
	public void setChliddata(List<Sup01102Vo> chliddata) {
		this.chliddata = chliddata;
	}
	
	
}
