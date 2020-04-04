package com.cm.core.aop.customannotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.cm.core.aop.events.Notifiers;
import com.cm.core.aop.events.Events;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Notification {
	Events event();
	Notifiers[] notifiers() default {Notifiers.EMAIL};
	
}