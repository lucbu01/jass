package ch.bbzw.jass.model.dto;

import lombok.Data;

@Data
public class MessageDto {

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
