package com.cm.core.model.entity;



import static java.util.Arrays.asList;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;

/**
 * @author paaverma
 *
 */

public class RestErrorList extends ArrayList<ErrorMessage> {

	private static final long serialVersionUID = 1L;
	private HttpStatus status;

	public RestErrorList(HttpStatus status, ErrorMessage... errors) {
		this(status.value(), errors);
	}

	public RestErrorList(int status, ErrorMessage... errors) {
		super();
		this.status = HttpStatus.valueOf(status);
		addAll(asList(errors));
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

}