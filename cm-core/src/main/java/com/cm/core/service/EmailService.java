package com.cm.core.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.cm.core.aop.events.Events;
import com.cm.core.util.Notifier;
import com.cm.persistence.domain.EmailTemplate;
import com.cm.persistence.domain.MailDTO;
import com.cm.persistence.domain.RegistrationDTO;
import com.cm.persistence.domain.courierManagementDTO;

@Service
public class EmailService implements Notifier{
	
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	
	private static final String EMAIL_DELIMITER = ";";
	
	@Value("${vre.email.from}")
	private String quotationMailFrom;
	
	@Value("${vre.email.to}")
	private String quotationMailTo;

	@Value("${vre.email.cc}")
	private String quotationMailCc;
	
	@Value("${vre.email.test.mode}")
	private boolean emailTestMode;
	
	@Value("${vre.email.subjectForAccountCreation}")
	private String subjectForAccountCreation;
	
	@Autowired
	private JavaMailSender emailSender;
	
	@Autowired
	private SpringTemplateEngine templateEngine;

	@Override
	public void notifi(Map<String, Object> eventMap, Events events) throws Exception {
		Object dto = null;
		courierManagementDTO cmDTO = null;
		
		switch(events){
		 case REGISTRATION:
			 dto = eventMap.get("notificationEmail");
			 cmDTO = new courierManagementDTO();
			 cmDTO.setName(((RegistrationDTO)dto).getName());
			 cmDTO.setEmail(((RegistrationDTO)dto).getEmail());
			 sendEmailToUser(cmDTO, "registration");
			 
		}
		
	}
	
	public String  sendEmailToUser(Object cmDTO, String notificationName) throws Exception{
		
		String[] emailIds = {((courierManagementDTO)cmDTO).getEmail()} ;
		return sendMail(createEmailContent(EmailTemplate.NEW_REGISTRATION, cmDTO,  subjectForAccountCreation, emailIds), EmailTemplate.NEW_REGISTRATION.getTemplateName());
		
	}
	
	public MailDTO createEmailContent(EmailTemplate template, Object cmDTO, String subject, String[] emailIds) throws Exception {
		
		logger.debug("creating email content for email template: " + template.name());
		
		String[] toEmail = emailIds;
		String[] ccEmail = emailIds;
		
		Class classOfCourierManagement = cmDTO.getClass();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		
		List<Field> listOfFields = new ArrayList<Field>();
		boolean isMethodExistInClass = false;
		boolean isMethodExistInSuperClass = false;
		Method method = null;
		
		for (String paramName : template.getParamNames()) {
			for(Field f: getAllFields(listOfFields, classOfCourierManagement)){
				String methodNameOfDTO = "get" + f.getName().substring(0,1).toUpperCase()+f.getName().substring(1);
				String templateMethodName = "get" + paramName.substring(0,1).toUpperCase()+paramName.substring(1);
				if(methodNameOfDTO.equals(templateMethodName)){
					isMethodExistInClass = checkMethodExist(classOfCourierManagement, templateMethodName);
					if(isMethodExistInClass){
						method = classOfCourierManagement.getDeclaredMethod(templateMethodName);
						paramsMap.put(paramName,method.invoke(cmDTO));
					}else{
						isMethodExistInSuperClass = checkMethodExist(classOfCourierManagement.getSuperclass(), templateMethodName);
						if(isMethodExistInSuperClass){
							method = (classOfCourierManagement.getSuperclass()).getDeclaredMethod(templateMethodName);
							paramsMap.put(paramName,method.invoke(cmDTO));
						}
					}
					if(method == null) {
						throw new RuntimeException(templateMethodName + " not found in class: " + classOfCourierManagement.getName());
					}
					
					if(isMethodExistInClass || isMethodExistInSuperClass){
						if(subject.contains("{"+paramName+"}")){
							String patt = "\\{"+paramName+"}";
							Pattern r = Pattern.compile(patt);
							Matcher m = r.matcher(subject);
							subject = m.replaceAll(method.invoke(cmDTO).toString());
						}

					}
					method = null;
				}
			}
		}
		
		MailDTO mail = new MailDTO();
		mail.setFromEmail(quotationMailFrom);
		mail.setToEmail(toEmail);
		mail.setCcEmail(ccEmail);
		mail.setModel(paramsMap);
		mail.setSubject(subject);
		
		return mail;
	}
	
	
	private List<Field> getAllFields(List<Field> fields, Class<?> type) {
	    fields.addAll(Arrays.asList(type.getDeclaredFields()));

	    if (type.getSuperclass() != null) {
	        getAllFields(fields, type.getSuperclass());
	    }

	    return fields;
	}
	
	private boolean checkMethodExist(Class cls, String methodName){
		boolean result = false;
		for (Method method : cls.getDeclaredMethods()) {
	        if (method.getName().equals(methodName)) {
	            result = true;
	            break;
	        }
	    }
		return result;
	}
	
	
	private synchronized String sendMail(MailDTO mail, String emailTemplateName)
			 throws MessagingException{
				MimeMessage message = emailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
						StandardCharsets.UTF_8.name());
				Context context = new Context();
				context.setVariables(mail.getModel());
				
				String html = templateEngine.process(emailTemplateName, context);
				if(null != mail.getToEmail()){
					helper.setTo(mail.getToEmail());
				}
				
				if(null != mail.getCcEmail()){
					helper.setCc(mail.getCcEmail());
				}
				
				helper.setText(html, true);
				helper.setSubject(mail.getSubject());
				helper.setFrom(mail.getFromEmail());
				
				helper.setCc(quotationMailCc.split(EMAIL_DELIMITER));
				
				logger.debug("==============================================");
				logger.debug("FROM: " + mail.getFromEmail());
				logger.debug("TO: " + StringUtils.arrayToCommaDelimitedString(mail.getToEmail()));
				logger.debug("CC: " + StringUtils.arrayToCommaDelimitedString(mail.getToEmail()));
				logger.debug("----------------------------------------------");
				logger.debug("SUBJECT: " + mail.getSubject());
				logger.debug("----------------------------------------------");
				logger.debug(html);
				logger.debug("----------------------------------------------");
				try {
					if(emailTestMode) {
						html += "<br/>";
						html += "<hr/>";
						html += "debug content:";
						html += "<br/>FROM: " + mail.getFromEmail();
						html += "<br/>TO: " + StringUtils.arrayToCommaDelimitedString(mail.getToEmail());
						html += "<br/>CC: " + StringUtils.arrayToCommaDelimitedString(mail.getToEmail());
						html += "<hr/>";
						helper.setText(html, true);
						//helper.setTo(new InternetAddress(quotationMailTo));
						//helper.setCc(new InternetAddress(quotationMailCc));
						helper.setTo(quotationMailTo.split(EMAIL_DELIMITER));
						helper.setCc(quotationMailCc.split(EMAIL_DELIMITER));
					}
					if(null != mail.getToEmail() || emailTestMode){
						emailSender.send(message);
						
					}
					
					logger.debug("email sent successfully");
				} catch (MailException e) {
	
					
					e.printStackTrace();
					logger.debug("email sending failed");
				}
				logger.debug("==============================================");
				
				return html;
	}

}
