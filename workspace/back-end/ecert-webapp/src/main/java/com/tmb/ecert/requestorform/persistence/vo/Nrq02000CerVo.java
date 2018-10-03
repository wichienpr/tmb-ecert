package com.tmb.ecert.requestorform.persistence.vo;

import com.tmb.ecert.common.domain.Certificate;

public class Nrq02000CerVo extends Certificate {
	Boolean check;
	Integer value;

	public Boolean getCheck() {
		return check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
