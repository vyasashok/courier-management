package com.cm.integration.sns;

import java.net.URISyntaxException;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;




@Service
public class SNSService {
	
	
	
	@Value("${aws.sqs.accesskey}")
	private String sqsAccessKey;
	
	@Value("${aws.sqs.secretkey}")
	private String sqsSecretKey;
	
	
    public AmazonSNS getSnsClient(){
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(sqsAccessKey, sqsSecretKey));

        return AmazonSNSClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(Regions.EU_WEST_1)
                .build();
    }
	

}
