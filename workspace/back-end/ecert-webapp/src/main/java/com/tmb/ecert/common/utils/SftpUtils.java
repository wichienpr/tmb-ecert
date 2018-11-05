package com.tmb.ecert.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.tmb.aes256.TmbAesUtil;
import com.tmb.ecert.common.domain.SftpFileVo;
import com.tmb.ecert.common.domain.SftpVo;

public class SftpUtils {

	private static final Logger log = LoggerFactory.getLogger(SftpUtils.class);
	
	public static final int SFTP_PORT = 22;
	
	@Value("${aes256.keystore.path}")
	private static String keystorePath;
	
	public static File getFile(SftpVo vo) {
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		OutputStream outputStream = null;
		try {
			Security.insertProviderAt(new BouncyCastleProvider(), 1);

			JSch jsch = new JSch();
			session = jsch.getSession(vo.getUsername(), vo.getHost(), SFTP_PORT);
			session.setPassword(vo.getPassword());
			
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			
			log.info("Host Session connected");

			channel = session.openChannel("sftp");
			channel.connect();
			log.info("Channel connected");

			channelSftp = (ChannelSftp) channel;
			
			if (vo.getGetFile() != null) {
				SftpFileVo getFile = vo.getGetFile();
				channelSftp.cd(getFile.getPath());

				File newFile = new File(vo.getArchivePath());
				outputStream = new FileOutputStream(newFile);
				channelSftp.get(getFile.getFileName(), outputStream);
				return newFile;
			}
		} catch (Exception e) {
			vo.setErrorMessage(e.getMessage());
			log.error("exception in getFile : {}", e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					log.error("close outputStream ", e.getMessage());
				}
			}
			if (channelSftp != null) {
				channelSftp.exit();
				log.debug("sftp Channel exited");
				channel.disconnect();
				log.info("Channel disconnected");
				session.disconnect();
				log.info("Host Session disconnected");
			}
		}
		
		return null;
	}
	
	public static boolean putFile(SftpVo vo) {

		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		boolean isSuccess = true;
		try {
			Security.insertProviderAt(new BouncyCastleProvider(), 1);
			
			JSch jsch = new JSch();
			session = jsch.getSession(vo.getUsername(), vo.getHost(), SFTP_PORT);
			session.setPassword(TmbAesUtil.decrypt(keystorePath,vo.getPassword()));
//			session.setPassword(vo.getPassword());
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			log.info("Host Session connected");
			
			channel = session.openChannel("sftp");
			channel.connect();
			log.info("Channel connected");
			
			channelSftp = (ChannelSftp) channel;
			
			for (SftpFileVo file : vo.getPutFiles()) {
				channelSftp.cd(file.getPath());
				inputStream = new FileInputStream(file.getFile());
				channelSftp.put(inputStream, file.getFileName());
				log.info("Put file FileName : {} Completed ", file.getFileName());
			}
		} catch (Exception e) {
			log.error("exception in putFile : {}", e);
			vo.setErrorMessage(e.getMessage());
			isSuccess = false;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error("exception close inputStream ", e.getMessage());
					isSuccess = false;
				}
			}
			
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					log.error("exception close outputStream ", e.getMessage());
					isSuccess = false;
				}
				
			}
			
			if (channelSftp != null) {
				channelSftp.exit();
				log.debug("sftp Channel exited");
				channel.disconnect();
				log.info("Channel disconnected");
				session.disconnect();
				log.info("Host Session disconnected");
			}
		}

		return isSuccess;
	}
	
	public static boolean putFile(SftpVo vo, String dir) {

		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		boolean isSuccess = true;
		try {
			Security.insertProviderAt(new BouncyCastleProvider(), 1);
			
			JSch jsch = new JSch();
			session = jsch.getSession(vo.getUsername(), vo.getHost(), SFTP_PORT);
			session.setPassword(TmbAesUtil.decrypt(keystorePath,vo.getPassword()));
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			log.info("Host Session connected");
			
			channel = session.openChannel("sftp");
			channel.connect();
			log.info("Channel connected");
			
			channelSftp = (ChannelSftp) channel;
			
			channelSftp.mkdir(dir);
			
			for (SftpFileVo file : vo.getPutFiles()) {
				channelSftp.cd(file.getPath());
				inputStream = new FileInputStream(file.getFile());
				channelSftp.put(inputStream, file.getFileName());
				log.info("Put file FileName : {} Completed ", file.getFileName());
			}
		} catch (Exception e) {
			log.error("exception in putFile : {}", e);
			vo.setErrorMessage(e.getMessage());
			isSuccess = false;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error("exception close inputStream ", e.getMessage());
					isSuccess = false;
				}
			}
			
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					log.error("exception close outputStream ", e.getMessage());
					isSuccess = false;
				}
				
			}
			
			if (channelSftp != null) {
				channelSftp.exit();
				log.debug("sftp Channel exited");
				channel.disconnect();
				log.info("Channel disconnected");
				session.disconnect();
				log.info("Host Session disconnected");
			}
		}

		return isSuccess;
	}
	
}
