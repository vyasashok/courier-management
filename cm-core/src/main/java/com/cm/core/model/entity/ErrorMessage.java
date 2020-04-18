package com.cm.core.model.entity;

import java.io.Serializable;

public class ErrorMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private String message;

	private String code;

	private String detail;

	/**
	 * @param message
	 * @param code
	 * @param detail
	 */
	public ErrorMessage(String message, String code, String detail) {
		super();
		this.message = message;
		this.code = code;
		this.detail = detail;
	}

	/**
	 * @param message
	 */
	public ErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getCode() {
		return code;
	}

	public String getDetail() {
		return detail;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
