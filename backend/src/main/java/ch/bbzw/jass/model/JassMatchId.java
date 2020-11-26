package ch.bbzw.jass.model;

import java.io.Serializable;
import java.util.UUID;

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
public class JassMatchId implements Serializable {

	private static final long serialVersionUID = -8645927287139607707L;

	private UUID game;

	private Integer index;
}
