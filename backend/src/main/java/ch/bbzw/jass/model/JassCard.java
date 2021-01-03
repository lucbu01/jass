package ch.bbzw.jass.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JassCard implements Comparable<JassCard> {

	private JassColor color;
	private JassValue value;

	@Override
	public int compareTo(JassCard o) {
		if (this.getColor().equals(o.getColor())) {
			return this.getValue().compareTo(o.getValue());
		} else {
			return this.getColor().compareTo(o.getColor());
		}
	}
}
