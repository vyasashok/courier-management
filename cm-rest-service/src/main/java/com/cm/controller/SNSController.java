package com.cm.controller;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.cm.integration.sns.SNSService;

import software.amazon.awssdk.services.sns.SnsClient;



@RestController
@RequestMapping("/sns")
public class SNSController {
	
	@Autowired
	private SNSService snsService;
	
	
	@PostMapping("/createTopic")
	private String createTopic(@RequestParam("topic_name") String topicName) throws URISyntaxException {

	    // Topic name cannot contain spaces
	    final CreateTopicRequest topicCreateRequest = new CreateTopicRequest(topicName);

	    // Helper method makes the code more readable
	    AmazonSNS snsClient = snsService.getSnsClient();

	    final CreateTopicResult topicCreateResponse = snsClient.createTopic(topicCreateRequest);

	    if (topicCreateResponse.getSdkResponseMetadata().getRequestId() != null) {
	        System.out.println("Topic creation successful");
	        System.out.println("Topic ARN: " + topicCreateResponse.getTopicArn());
	        System.out.println("Topics: " + snsClient.listTopics());
	        
	        final SubscribeRequest subscribeRequest = new SubscribeRequest(topicCreateResponse.getTopicArn(), "email", "vyas.ashok17@gmail.com");
	        snsClient.subscribe(subscribeRequest);
	     
	        
	    } else {
	        throw new ResponseStatusException(
	            HttpStatus.INTERNAL_SERVER_ERROR, topicCreateResponse.getSdkResponseMetadata().toString()
	        );
	    }

	    return "Topic ARN: " + topicCreateResponse.getTopicArn();
	}
	
	
	@PostMapping("/publish")
	public String publishSNSMessage(@RequestParam("message") String message){
		
		  AmazonSNS snsClient = snsService.getSnsClient();
		  
		  snsClient.publish("arn:aws:sns:eu-west-1:965416788576:sns-test", message, "sns messsage");
		  
		  return message;
	}

}
