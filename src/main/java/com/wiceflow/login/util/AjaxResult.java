package com.wiceflow.login.util;

/**
 * 封装返回前台的数据
 */
public class AjaxResult {
	/*
	 * 正常为200
	 * 出错为0
	 */
	private Integer status;
	private Object result;
	private String message;
	
	public AjaxResult() {
		//默认不设置就是成功
		status = 200;
		message = "success";
	}
	
	
	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}




}
