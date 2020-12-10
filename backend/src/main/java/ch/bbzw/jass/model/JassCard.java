package ch.bbzw.jass.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JassCard {

	private final JassColor color;
	private final JassValue value;
}
