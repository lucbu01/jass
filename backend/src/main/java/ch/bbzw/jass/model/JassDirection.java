package ch.bbzw.jass.model;

public enum JassDirection {
	UPSIDE_DOWN, DOWNSIDE_UP, GUSTI, MERRY, SLALOM_UP, SLALOM_DOWN;

	/**
	 * 
	 * @param roundIndex
	 * @return the actual direction based on the round index (can be UPSIDE_DOWN or
	 *         DOWNSIDE_UP)
	 */
	JassDirection getActualDirection(short roundIndex) {
		switch (this) {
		case GUSTI:
			if (roundIndex < 4) {
				return UPSIDE_DOWN;
			} else {
				return DOWNSIDE_UP;
			}

		case MERRY:
			if (roundIndex < 4) {
				return DOWNSIDE_UP;
			} else {
				return UPSIDE_DOWN;
			}

		case SLALOM_UP:
			if (roundIndex % 2 == 0) {
				return UPSIDE_DOWN;
			} else {
				return DOWNSIDE_UP;
			}

		case SLALOM_DOWN:
			if (roundIndex % 2 == 0) {
				return DOWNSIDE_UP;
			} else {
				return UPSIDE_DOWN;
			}

		default:
			return this;
		}
	}
}
