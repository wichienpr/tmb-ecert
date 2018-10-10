package com.tmb.ecert.checkrequeststatus.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.dao.CertificateDao;
import com.tmb.ecert.common.domain.Certificate;
import com.tmb.ecert.common.domain.RequestCertificate;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.service.DownloadService;

@Service
public class CheckRequestDetailService {
	
	private static String PATH = "requestor/";
	
	private static Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_SEARCH_REQFORM);
	
	@Autowired
	private DownloadService download;
	
	@Autowired
	private CheckRequestDetailDao dao;
	
	public List<Certificate> findCertListByReqFormId(String id) {
		Long reqFormId = Long.valueOf(id);
		String cerTypeCode = dao.findReqFormById(reqFormId, false).get(0).getCerTypeCode();
		logger.info(cerTypeCode);
		return dao.findCerByCerTypeCode(cerTypeCode);
	}
	
	public List<RequestCertificate> findCertByReqFormId(String id) {
		Long reqFormId = Long.valueOf(id);
		List<RequestCertificate> reqForm = dao.findCertByReqFormId(reqFormId);
		return reqForm;
	}

	public List<RequestForm> findReqFormById(String id) {
		Long reqFormId = Long.valueOf(id);
		List<RequestForm> reqForm = dao.findReqFormById(reqFormId);
		return reqForm;
	}
	
	public void download(String fileName, HttpServletResponse response) {
		String pathName = PATH + fileName;
		download.download(pathName, response);
	}

	public void pdf(String name, HttpServletResponse response) {
		String pathName = PATH + name;
		download.pdf(pathName, response);
	}
	
}
