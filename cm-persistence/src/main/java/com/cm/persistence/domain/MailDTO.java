package com.cm.persistence.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MailDTO {
	private String fromEmail;
	private String[] toEmail;
	private String[] ccEmail;
	private String subject;
	private List<Object> attachmentsEmail;
	private Map<String, Object> model;
	private String content;
	
	
	public MailDTO() {
		super();
	}

	public String getFromEmail() {
		return fromEmail;
	}



	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}



	public String[] getToEmail() {
		return toEmail;
	}



	public void setToEmail(String[] toEmail) {
		this.toEmail = toEmail;
	}



	public String[] getCcEmail() {
		return ccEmail;
	}



	public void setCcEmail(String[] ccEmail) {
		this.ccEmail = ccEmail;
	}



	public String getSubject() {
		return subject;
	}



	public void setSubject(String subject) {
		this.subject = subject;
	}



	public List<Object> getAttachmentsEmail() {
		return attachmentsEmail;
	}



	public void setAttachmentsEmail(List<Object> attachmentsEmail) {
		this.attachmentsEmail = attachmentsEmail;
	}



	public Map<String, Object> getModel() {
		return model;
	}



	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Mail [fromEmail=" + fromEmail + ", toEmail=" + Arrays.toString(toEmail) + ", ccEmail="
				+ Arrays.toString(ccEmail) + ", subject=" + subject + ", attachmentsEmail=" + attachmentsEmail
				+ ", model=" + model + "]";
	}
}
