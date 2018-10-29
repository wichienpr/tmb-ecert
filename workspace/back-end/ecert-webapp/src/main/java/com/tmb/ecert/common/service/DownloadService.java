package com.tmb.ecert.common.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;

@Service
public class DownloadService {
	
	private static Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_REQFORM);

	@Value("${app.datasource.path.upload}")
	private String PATH_UPLOAD;

	@Value("${app.datasource.path.report}")
	private String PATH_REPORT;
	
	@Autowired
	private EmailService emailService;

	public void download(String name, HttpServletResponse response) {
		try {
			FileInputStream results = null;
			try {
				File file = new File(PATH_UPLOAD + name);
				results = new FileInputStream(file);
				response.setContentType("application/octet-stream");
				response.addHeader("Content-Disposition", "attachment;filename=" + name);
				IOUtils.copy(results, response.getOutputStream());
				response.flushBuffer();
			} catch (Exception e) {
				logger.error("DownloadService::download", e);
			} finally {
				if (results != null) {
					results.close();
				}
			}
		} catch (Exception e) {
			emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_DOWNLOAD, e.toString());
			logger.error("DownloadService::download", e);
		}

	}

	public void pdf(String name, HttpServletResponse response) {
		try {
			FileInputStream results = null;
			try {
				File file = new File(PATH_UPLOAD + name + ".pdf");
				results = new FileInputStream(file);
				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "inline;filename=" + name + ".pdf");
				IOUtils.copy(results, response.getOutputStream());
				response.flushBuffer();
			} catch (Exception e) {
				emailService.sendEmailAbnormal(new Date(), ProjectConstant.EMAIL_SERVICE.FUNCTION_NAME_DOWNLOAD, e.toString());
				logger.error("DownloadService::download", e);
			} finally {
				if (results != null) {
					results.close();
				}
			}
		} catch (Exception e) {
			logger.error("DownloadService::pdf", e);
		}
	}
}
