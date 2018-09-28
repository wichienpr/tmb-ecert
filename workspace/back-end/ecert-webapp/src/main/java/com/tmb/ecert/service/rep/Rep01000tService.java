package com.tmb.ecert.service.rep;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.persistence.dao.Rep01000Repository;
import com.tmb.ecert.persistence.vo.Rep01000FormVo;
import com.tmb.ecert.persistence.vo.Rep01000Vo;

@Service
public class Rep01000tService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Rep01000Repository rep01000Repository;
	
	public List<Rep01000Vo> findAll(Rep01000FormVo formVo){
		List<Rep01000Vo> rep01000Vo = new ArrayList<Rep01000Vo>();
		rep01000Repository.getData(formVo);
		return rep01000Vo;
	}
}
