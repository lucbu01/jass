package ch.bbzw.jass.model;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JassTeamId implements Serializable {

	private static final long serialVersionUID = -6815092191529313413L;

	UUID game;
	
	Byte index;
}
