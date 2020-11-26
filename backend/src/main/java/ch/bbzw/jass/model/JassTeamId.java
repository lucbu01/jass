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
public class JassTeamId implements Serializable {

	private static final long serialVersionUID = -6815092191529313413L;

	UUID game;
	
	Byte index;
}
