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
public class JassMoveId implements Serializable {

	private static final long serialVersionUID = -8645927287139607707L;

	private JassRoundId round;

	private Byte index;
}
