package com.cm.integration.sqs;

import javax.jms.Session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

@Configuration
@EnableJms
public class SQSConfig {
	
	@Value("${aws.sqs.accesskey}")
	private String sqsAccessKey;
	
	@Value("${aws.sqs.secretkey}")
	private String sqsSecretKey;
   
	
	@Bean
	protected SQSConnectionFactory getConnectionFactory() {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(sqsAccessKey, sqsSecretKey);
		AmazonSQSClientBuilder builder = AmazonSQSClientBuilder.standard().withRegion(Regions.EU_WEST_1).withCredentials(new AWSStaticCredentialsProvider(awsCreds));

		AmazonSQS sqsClient = builder.build();
		SQSConnectionFactory sqsConnectionFactory = new SQSConnectionFactory(new ProviderConfiguration(),sqsClient);
		return sqsConnectionFactory;
	}
	
	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(SQSConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory jmsListenerConnectionFactory = new DefaultJmsListenerContainerFactory();
		jmsListenerConnectionFactory.setConnectionFactory(connectionFactory);
		jmsListenerConnectionFactory.setDestinationResolver(new DynamicDestinationResolver());
		jmsListenerConnectionFactory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
		return jmsListenerConnectionFactory;
	}
	
	@Bean
	public JmsTemplate createJMSTemplate(SQSConnectionFactory connectionFactory) {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		return jmsTemplate;

	}
}
