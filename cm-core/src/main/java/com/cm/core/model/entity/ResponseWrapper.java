package com.cm.core.model.entity;

import java.io.Serializable;
import java.util.List;

public class ResponseWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	private Object data;

	private Object metadata;

	private List<ErrorMessage> errors;

	/**
	 * @param data
	 * @param metadata
	 * @param errors
	 */
	public ResponseWrapper(Object data, Object metadata, List<ErrorMessage> errors) {
		super();
		this.data = data;
		this.metadata = metadata;
		this.errors = errors;
	}

	/**
	 * @param data
	 * @param metadata
	 */
	public ResponseWrapper(Object data, Object metadata) {
		super();
		this.data = data;
		this.metadata = metadata;
	}

	/**
	 * @param data
	 */
	public ResponseWrapper(Object data) {
		super();
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public Object getMetadata() {
		return metadata;
	}

	public List<ErrorMessage> getErrors() {
		return errors;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setMetadata(Object metadata) {
		this.metadata = metadata;
	}

	public void setErrors(List<ErrorMessage> errors) {
		this.errors = errors;
	}

}
