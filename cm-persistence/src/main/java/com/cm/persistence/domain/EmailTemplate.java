package com.cm.persistence.domain;

public enum EmailTemplate {
	NEW_REGISTRATION("new_registartion_mail_template",new String[]{"name", "email"});
		
	private String templateName;
	private String[] paramNames;
	
	
	private EmailTemplate(String templateName, String[] paramNames) {
		this.templateName = templateName;
		this.paramNames = paramNames;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}
}
