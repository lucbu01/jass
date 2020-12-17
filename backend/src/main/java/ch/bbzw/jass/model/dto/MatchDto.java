package ch.bbzw.jass.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class MatchDto {
	private Integer index;
	private PlayerDto announcer;
	private PlayerDto definitiveAnnouncer;
	private Boolean pushed;
}
