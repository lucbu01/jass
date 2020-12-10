package ch.bbzw.jass.exception;

import ch.bbzw.jass.model.dto.MessageDto;
import lombok.Getter;

@Getter
public class MessageException extends RuntimeException {

	private static final long serialVersionUID = 7689203018813526569L;
	
	private final MessageDto messageDto;

	public MessageException(MessageDto dto) {
		super(dto.getMessage());
		this.messageDto = dto;
	}
}
