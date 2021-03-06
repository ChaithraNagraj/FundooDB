package com.bridgelabz.fundoonotes.response;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 
 * @author chaithra B N
 * 
 */

/*
 * Class that is used for the secure of the email data that has to be sent
 */
@Data
@Component
public class MailObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private String email;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private String subject;
	private String message;

}
