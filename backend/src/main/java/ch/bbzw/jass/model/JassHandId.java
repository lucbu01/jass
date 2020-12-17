package ch.bbzw.jass.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JassHandId implements Serializable {

	private static final long serialVersionUID = -7044351387378441303L;
	
	private JassMatchId match;
	
	private JassColor color;
	
	private JassValue value;
}
