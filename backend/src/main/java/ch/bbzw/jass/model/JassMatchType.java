package ch.bbzw.jass.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum JassMatchType {
	CLAMP(2), SHIELD(2), ROSE(4), ACORN(1), UPSIDE_DOWN(3), DOWNSIDE_UP(3), GUSTI(5), MERRY(5), SLALOM(5);

	private final int multiplicator;
}
