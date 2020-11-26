package ch.bbzw.jass.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class JassRoundId implements Serializable {
	private static final long serialVersionUID = 7633468092926679769L;

	JassMatchId match;

	Short index;
}
