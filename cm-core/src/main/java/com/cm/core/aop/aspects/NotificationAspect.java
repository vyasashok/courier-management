package com.cm.core.aop.aspects;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cm.core.aop.customannotations.Notification;
import com.cm.core.aop.events.NotificationManager;

@Aspect
@Component
public class NotificationAspect {

	
	Logger logger = LoggerFactory.getLogger(NotificationAspect.class);
	@Autowired
	NotificationManager notificationManager;
	
	@AfterReturning(pointcut="execution(@com.cm.core.aop.customannotations.Notification * *(..)) && @annotation(notificationAnnotation)", returning="res")
	public void notificationHandle(Notification notificationAnnotation, Object res) {
		
		logger.info("Notification Event: "+notificationAnnotation.event());
		
		try{
			notificationManager.sendNotification(notificationAnnotation, res);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
