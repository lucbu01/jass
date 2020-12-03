package ch.bbzw.jass.model;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JassMatchId implements Serializable {

	private static final long serialVersionUID = -8645927287139607707L;

	private UUID game;

	private Integer index;
}
