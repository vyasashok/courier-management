package com.cm.core.aop.events;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cm.core.aop.customannotations.Notification;
import com.cm.core.service.EmailService;
import com.cm.core.util.Notifier;

@Component
public class NotificationManager {
	
	@Autowired
	EmailService emailService;

	public void sendNotification(Notification notification, Object responseOfActualMethod) throws Exception{
		
		Notifier notifier = null;
		
		Map<String, Object> notificationMap = new HashMap<String, Object>();
		for (Notifiers notifiers : notification.notifiers()) {
			notifier = getNotifier(notifiers);
			if(notifier instanceof EmailService){
				notificationMap.put("notificationEmail", responseOfActualMethod);
				notifier.notifi(notificationMap, notification.event());
			}
			notifier = null;
		}
	}
	
	private Notifier getNotifier(Notifiers notifier) {
		
		if(notifier == Notifiers.EMAIL) {
			return (Notifier) emailService;
		}
		else{
			return null;
		}
		
	}
}
