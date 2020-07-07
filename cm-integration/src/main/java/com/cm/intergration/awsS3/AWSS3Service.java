package com.cm.intergration.awsS3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;

@Configuration
@Service
public class AWSS3Service {
	
	private static final Logger logger = LoggerFactory.getLogger(AWSS3Service.class);
	
	@Value("${s3_bucket_name}")
	private String bucketName;
	@Value("${s3_access_key_id}")
	private String awsId;
	@Value("${s3_secret_access_key}")
	private String awsKey;
	
	AmazonS3 s3Client;

	public AmazonS3 connectToS3() {
		if (s3Client == null) {
			BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
			s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1)
					.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
		}
		return s3Client;
	}
	
	public void store(String key, File file) {
		connectToS3().putObject(
				new PutObjectRequest(bucketName, key, file).withCannedAcl(CannedAccessControlList.PublicRead));
	}
	
	public void store(String key, InputStream is) throws IOException {
		byte[] contentData = IOUtils.toByteArray(is);
		ObjectMetadata objectMetaData = new ObjectMetadata();
		objectMetaData.setContentLength(contentData.length);
		logger.debug("content Data: " + contentData.length);
		PutObjectRequest request = new PutObjectRequest(bucketName, key, new ByteArrayInputStream(contentData),
				objectMetaData);
		connectToS3().putObject(request);
	}

}
