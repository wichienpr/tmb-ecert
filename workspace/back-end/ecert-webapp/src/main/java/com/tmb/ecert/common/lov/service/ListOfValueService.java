package com.tmb.ecert.common.lov.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.lov.dao.ListOfValueDao;
import com.tmb.ecert.domain.LabelValueBean;

@Service
public class ListOfValueService {

	@Autowired
	private ListOfValueDao listOfValueDao;
	
	public List<LabelValueBean> lov(String type){
		return listOfValueDao.lov(type);
	}
}
