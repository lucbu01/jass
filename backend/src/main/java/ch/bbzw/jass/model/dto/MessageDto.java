package ch.bbzw.jass.model.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class MessageDto implements Serializable {

	private static final long serialVersionUID = -8240981900504229073L;

	private String message;

	private String redirectTo;

	public MessageDto(String message) {
		this.message = message;
	}

	public MessageDto(String message, String redirectTo) {
		this.message = message;
		this.redirectTo = redirectTo;
	}
}
