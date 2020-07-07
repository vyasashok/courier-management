package com.cm.integration.sqs;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Configuration
@Service
public class SQSListener {
	
	private static final Logger logger = LoggerFactory.getLogger(SQSListener.class);
	
	@JmsListener(destination = "${aws.sqs.queue.name}")
	public void onMessage(String message) {
		logger.debug("job message received: " + message);
	}

}
