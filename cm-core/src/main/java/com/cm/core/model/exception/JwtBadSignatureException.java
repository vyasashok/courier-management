package com.cm.core.model.exception;

public class JwtBadSignatureException  extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public JwtBadSignatureException(String message) {
        super(message);
    }
}
