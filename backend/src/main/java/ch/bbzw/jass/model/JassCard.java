package ch.bbzw.jass.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class JassCard {

	private final JassColor color;
	private final JassValue value;
}
