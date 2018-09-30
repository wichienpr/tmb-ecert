package com.tmb.ecert.service.crs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.persistence.dao.Crs01000Dao;
import com.tmb.ecert.persistence.vo.Crs01000Vo;





@Service
public class Crs01000Service {

	private Logger logger = LoggerFactory.getLogger(Crs01000Service.class);
	
	@Autowired
	private Crs01000Dao crs01000Dao;
	
	
	public List<Crs01000Vo> findAllReqForm(Crs01000Vo dataAll) {
		//Date date=new Date();
		//SimpleDateFormat df= new SimpleDateFormat();
		//String  date1 = df.applyPattern("dd/mm/yyyy");
		//dataAll.setReqDateS(date1);	
		

		return crs01000Dao.findAllReqForm(dataAll);
	}
}
