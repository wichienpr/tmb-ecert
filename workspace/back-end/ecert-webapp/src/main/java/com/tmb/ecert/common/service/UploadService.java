package com.tmb.ecert.common.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import th.co.baiwa.buckwaframework.common.util.EcertFileUtils;

@Service
public class UploadService {

	private static final Logger logger = LoggerFactory.getLogger(UploadService.class);

	@Value("${app.datasource.path.upload}")
	private String pathed;

	public void createFolder(String folder) {
		File f = new File(pathed + folder); // initial file (folder)
		if (!f.exists()) { // check folder exists
			if (f.mkdirs()) {
				logger.info("Directory is created!");
				// System.out.println("Directory is created!");
			} else {
				logger.error("Failed to create directory!");
				// System.out.println("Failed to create directory!");
			}
		}
	}

	public void createFile(byte[] file, String pathName, String fileName) throws IOException {
		byte[] data = file; // get data
		// set path
		OutputStream stream = null;
		try {
			String path = pathed + fileName;
			stream = new FileOutputStream(path);
			stream.write(data);
			logger.info("Created file: " + path);

			
		} finally {
			EcertFileUtils.closeStream(stream);
		}

	}
}
