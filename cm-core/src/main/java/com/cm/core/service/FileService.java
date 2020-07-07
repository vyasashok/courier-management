package com.cm.core.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cm.intergration.awsS3.AWSS3Service;

@Service
public class FileService {
	
	private static final Logger logger = LoggerFactory.getLogger(FileService.class);
	
	@Autowired
	private AWSS3Service s3Service;
	
	public void uploadFile(String fileKey, MultipartFile multiPartFile){
		
		try {
			logger.debug("uploaing multipart file for key: " + fileKey);
			s3Service.store(fileKey, multiPartFile.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
