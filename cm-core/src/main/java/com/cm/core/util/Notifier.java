package com.cm.core.util;

import java.util.Map;

import com.cm.core.aop.events.Events;

public interface Notifier {
	void notifi(Map<String, Object> eventMap, Events events) throws Exception;
}
