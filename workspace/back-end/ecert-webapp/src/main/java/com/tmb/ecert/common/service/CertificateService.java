package com.tmb.ecert.common.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.dao.CertificateDao;
import com.tmb.ecert.common.domain.Certificate;

@Service
public class CertificateService {

	private static final Logger logger = LoggerFactory.getLogger(CertificateService.class);

	@Autowired
	CertificateDao certificateDao;

	public List<Certificate> cerAllTypeCode() {
		logger.info("CertificateService::cerAllTypeCode");
		return certificateDao.findAllTypeCode();
	}

	public List<Certificate> cerByTypeCode(String typeCode) {
		logger.info("CertificateService::cerByTypeCode => params: [ typeCode:{} ]", typeCode);
		return certificateDao.findByTypeCode(typeCode);
	}

}
