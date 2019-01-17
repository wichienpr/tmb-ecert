package com.tmb.ecert.common.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.tmb.ecert.common.constant.ProjectConstant;
import com.tmb.ecert.common.service.DownloadService;

import th.co.baiwa.buckwaframework.common.util.EcertFileUtils;
import th.co.baiwa.buckwaframework.support.ApplicationCache;

@RequestMapping("api/manual")
@Controller
public class ManualController {
	
	@Autowired
	private DownloadService downloadService;
	
	@GetMapping("/pdf")
	@ResponseBody
	public void cerByTypeCode(HttpServletResponse response) {
		downloadService.manual(response);
	}
	
	@GetMapping("/video")
	@ResponseBody
	public FileSystemResource getVideo() throws IOException {
		String pathFile = ApplicationCache.getParamValueByName(ProjectConstant.ECERT_MANUAL.MANUAL_PATHVIDEO);
		
		return new FileSystemResource(new File(pathFile));
		
	}
	
	@GetMapping("/videoDownload")
	@ResponseBody
	public  void getfile(HttpServletResponse response) {
		String pathFile = ApplicationCache.getParamValueByName(ProjectConstant.ECERT_MANUAL.MANUAL_PATHVIDEO);
		ByteArrayOutputStream outByteStream = null;
		OutputStream outStream = null;
		FileInputStream file = null;
		byte[] data = null;
		try {
			file = new FileInputStream(new File(pathFile));
			data = IOUtils.toByteArray(file);
			
			outByteStream = new ByteArrayOutputStream();
			outByteStream.write(data);
			byte[] outArray = outByteStream.toByteArray();
			response.setContentType("video/mp4");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
//			response.setHeader("Content-Disposition", "attachment; filename=" +"eee"  + ".xlsx");
			outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			EcertFileUtils.closeStream(outByteStream);
			EcertFileUtils.closeStream(outStream);
		}
	}
	



}
