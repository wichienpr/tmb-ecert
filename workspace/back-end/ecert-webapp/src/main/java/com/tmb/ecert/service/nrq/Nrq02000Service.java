package com.tmb.ecert.service.nrq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.utils.BeanUtils;
import com.tmb.ecert.persistence.vo.Nrq02000FormVo;

@Service
public class Nrq02000Service {

	private static final Logger logger = LoggerFactory.getLogger(Nrq02000Service.class);

	@Value("${app.datasource.path.upload}")
	private String pathed;

	public void upload(Nrq02000FormVo form) {
		String requestFileName = BeanUtils.isNotEmpty(form.getCopyFile()) ? form.getRequestFile().getOriginalFilename()
				: null;
		String copyFile = BeanUtils.isNotEmpty(form.getCopyFile()) ? form.getCopyFile().getOriginalFilename() : null;
		String changeNameFile = BeanUtils.isNotEmpty(form.getChangeNameFile())
				? form.getChangeNameFile().getOriginalFilename()
				: null;
//		File f = new File(pathed + "Document_Travel_Estimator/" + idProcess + "/" + document); // initial file (folder)
//		if (!f.exists()) { // check folder exists
//			if (f.mkdirs()) {
//				log.info("Directory is created!");
//				// System.out.println("Directory is created!");
//			} else {
//				log.error("Failed to create directory!");
//				// System.out.println("Failed to create directory!");
//			}
//		}
//		try {
//			byte[] data = files.getBytes(); // get data
//			// set path
//			String path = pathed + "Document_Travel_Estimator/" + idProcess + "/" + document + "/" + documentName;
//			OutputStream stream = new FileOutputStream(path);
//			stream.write(data);
//
//			log.info("Created file: " + path);
//			// System.out.println("Created file: " + path);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		// long s = multi.getSize();
		// Long.toString(s/1000); // to megabytes
	}

}
