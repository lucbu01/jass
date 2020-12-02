package ch.bbzw.jass.model;

import lombok.Getter;

@Getter
public enum JassMatchType {
	CLAMP(2, JassColor.CLAMP), //
	SHIELD(2, JassColor.SHIELD), //
	ROSE(4, JassColor.ROSE), //
	ACORN(1, JassColor.ACORN), //
	UPSIDE_DOWN(3, JassDirection.UPSIDE_DOWN), //
	DOWNSIDE_UP(3, JassDirection.DOWNSIDE_UP), //
	GUSTI(5, JassDirection.GUSTI), //
	MERRY(5, JassDirection.MERRY), //
	SLALOM_UP(5, JassDirection.SLALOM_UP), //
	SLALOM_DOWN(5, JassDirection.SLALOM_DOWN);

	private final int multiplicator;
	private final JassDirection direction;
	private final JassColor trumpColor;

	private JassMatchType(int multiplicator, JassDirection direction) {
		this.trumpColor = null;
		this.multiplicator = multiplicator;
		this.direction = direction;
	}

	private JassMatchType(int multiplicator, JassColor trumpColor) {
		this.direction = null;
		this.multiplicator = multiplicator;
		this.trumpColor = trumpColor;
	}
}
