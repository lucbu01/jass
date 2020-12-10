package ch.bbzw.jass.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JassRoundId implements Serializable {
	private static final long serialVersionUID = 7633468092926679769L;

	JassMatchId match;

	Short index;
}
