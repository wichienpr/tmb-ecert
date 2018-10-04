package com.tmb.ecert.checkrequeststatus.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestDetailDao;
import com.tmb.ecert.common.domain.Certificate;
import com.tmb.ecert.common.domain.RequestCertificate;
import com.tmb.ecert.common.domain.RequestForm;
import com.tmb.ecert.common.service.DownloadService;
import com.tmb.ecert.common.utils.BeanUtils;

import th.co.baiwa.buckwaframework.support.ApplicationCache;

@Service
public class CheckRequestDetailService {
	
	private static String PATH = "requestor/";
	
	@Autowired
	private DownloadService download;
	
	@Autowired
	private CheckRequestDetailDao dao;
	
	public List<Certificate> findCertListByReqFormId(String id) {
		Long reqFormId = Long.valueOf(id);
		List<RequestForm> reqForm = dao.findReqFormById(reqFormId);
		String code = reqForm.get(0).getCerTypeCode();
		return ApplicationCache.getCerByType(code);
	}
	
	public List<RequestCertificate> findCertByReqFormId(String id) {
		Long reqFormId = Long.valueOf(id);
		List<RequestCertificate> reqForm = dao.findCertByReqFormId(reqFormId);
		return reqForm;
	}

	public List<RequestForm> findReqFormById(String id) {
		Long reqFormId = Long.valueOf(id);
		List<RequestForm> reqForm = dao.findReqFormById(reqFormId);
		if (reqForm.size() > 0) {
			RequestForm req = reqForm.get(0);
			String certDesc = "";
			if (BeanUtils.isNotEmpty(req.getCerTypeCode())) {
				if (BeanUtils.isNotEmpty(ApplicationCache.getCerByType(req.getCerTypeCode()))) {
					if (ApplicationCache.getCerByType(req.getCerTypeCode()).size() > 0) {
						certDesc = ApplicationCache.getCerByType(req.getCerTypeCode()).get(0).getTypeDesc();
					}
				}
			}
			req.setCerTypeCode(certDesc);
			if (BeanUtils.isNotEmpty(req.getDebitAccountType())) {
				req.setDebitAccountType(ApplicationCache.getLovByCode(req.getDebitAccountType()).getName());
			}
			if (BeanUtils.isNotEmpty(req.getStatus())) {
				req.setStatus(ApplicationCache.getLovByCode(req.getStatus()).getName());
			}
			if (BeanUtils.isNotEmpty(req.getPaidTypeCode())) {
				req.setPaidTypeCode(ApplicationCache.getLovByCode(req.getPaidTypeCode()).getName());
			}
			if (BeanUtils.isNotEmpty(req.getCustsegmentCode())) {
				req.setCustsegmentCode(ApplicationCache.getLovByCode(req.getCustsegmentCode()).getName());
			}
			reqForm.set(0, req);
		}
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
