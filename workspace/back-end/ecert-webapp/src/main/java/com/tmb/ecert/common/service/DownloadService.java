package com.tmb.ecert.common.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DownloadService {
	
	@Value("${app.datasource.path.upload}")
	private String PATH_UPLOAD;
	
	@Value("${app.datasource.path.report}")
	private String PATH_REPORT;
	
	public void download(String name, HttpServletResponse response) {
		byte[] result;
		try {
			File file = new File(PATH_UPLOAD + name);
			result = IOUtils.toByteArray(new FileInputStream(file));
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition",
	                "attachment;filename=" + name);
			response.setContentLength(result.length);
			OutputStream responseOutputStream = response.getOutputStream();
			for (byte bytes : result) {
				responseOutputStream.write(bytes);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void pdf(String name, HttpServletResponse response) throws Exception {
//		File file = new File(PATH_EXPORT + name + ".pdf");
//		byte[] reportFile = IOUtils.toByteArray(new FileInputStream(file)); // null
//
//		response.setContentType("application/pdf");
//		response.addHeader("Content-Disposition", "inline;filename=" + name + ".pdf");
//		response.setContentLength(reportFile.length);
//
//		OutputStream responseOutputStream = response.getOutputStream();
//		for (byte bytes : reportFile) {
//			responseOutputStream.write(bytes);
//		}
	}
}
