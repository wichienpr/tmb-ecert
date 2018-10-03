package com.tmb.ecert.common.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import th.co.baiwa.buckwaframework.support.ApplicationCache;
import com.tmb.ecert.common.constant.ProjectConstant;

@Service
public class ArchiveFileUtil {
	
	private static final Logger log = LoggerFactory.getLogger(ArchiveFileUtil.class);
	
	@Autowired
	private ApplicationCache applicationCache;
	
	/**
	 * @param srcPath
	 * @param tarName
	 * @return
	 * @throws Exception
	 */
	public boolean archiveFile(String srcPath,String tarName) throws Exception {
		
		boolean result = false;
		
		File sourceFile = new File(srcPath);
		
		TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new FileOutputStream(new File(tarName)));
 
		archive(sourceFile, outputStream, "");
 
		// Flushes this output stream and forces any buffered output bytes to be written out
		outputStream.flush();
 
		// Closes the underlying OutputStream
		outputStream.close();
		
		result = true;
		
		return result;
	}
 
	/**
	 * @param sourceFile
	 * @param outputStream
	 * @param basePath
	 * @throws Exception
	 */
	private void archive(File sourceFile,TarArchiveOutputStream outputStream, String basePath) throws Exception {
		if (sourceFile.isDirectory()) {
 
			// Archive Directory
			archiveDirectory(sourceFile, outputStream, basePath);
		} else {
 
			// Archive File
			archiveFile(sourceFile, outputStream, basePath);
		}
	}
 
	/**
	 * @param directory
	 * @param outputStream
	 * @param basePath
	 * @throws Exception
	 */
	private void archiveDirectory(File directory,TarArchiveOutputStream outputStream, String basePath) throws Exception {
	
		String pathFile = applicationCache.getParamValueByName(PARAMETER_CONFIG.BATCH_HOUSEKEEPING_ROOTPATH);
 
		// Returns an array of abstract pathnames denoting the files in the directory denoted by this abstract pathname
		File[] files = directory.listFiles();
 
		if (files != null) {
			if (files.length < 1) {
 
				// Construct an entry with only a name. This allows the programmer to construct the entry's header "by hand". File
				// is set to null
				TarArchiveEntry entry = new TarArchiveEntry(basePath + directory.getName() + pathFile);
 
				// Put an entry on the output stream
				outputStream.putArchiveEntry(entry);
 
				// Close an entry. This method MUST be called for all file entries that contain data
				outputStream.closeArchiveEntry();
			}
 
			// Repeat for all files
			for (File crunchifyFile : files) {
				archive(crunchifyFile, outputStream,basePath + directory.getName() + pathFile);
			}
		}
	}
 
	/**
	 * @param file
	 * @param outputStream
	 * @param directory
	 * @throws Exception
	 */
	private void archiveFile(File file,TarArchiveOutputStream outputStream, String directory) throws Exception {
 
		TarArchiveEntry entry = new TarArchiveEntry(directory + file.getName());
 
		// Set this entry's file size
		entry.setSize(file.length());
 
		outputStream.putArchiveEntry(entry);
 
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		int counter;
 
		// 512: buffer size
		byte byteData[] = new byte[512];
		while ((counter = inputStream.read(byteData, 0, 512)) != -1) {
			outputStream.write(byteData, 0, counter);
		}
 
		inputStream.close();
		outputStream.closeArchiveEntry();
	}
}
