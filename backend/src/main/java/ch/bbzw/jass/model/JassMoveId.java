package ch.bbzw.jass.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JassMoveId implements Serializable {

	private static final long serialVersionUID = -8645927287139607707L;

	private JassRoundId round;

	private Byte index;
}
