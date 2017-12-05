package com.framework.net;

public abstract class AbstractBaseBean {
	protected int code;
	protected String message;
	public AbstractBaseBean() {
		
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	

}
