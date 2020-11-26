package ch.bbzw.jass.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JassValue {
	SIX(0, 0, 0, 0, 11), //
	SEVEN(1, 0, 0, 0, 0), //
	EIGHT(2, 0, 0, 0, 0), //
	NINE(3, 0, 14, 0, 0), //
	TEN(4, 10, 10, 10, 10), //
	UNDER(5, 2, 20, 2, 2), //
	OVER(6, 3, 3, 3, 3), //
	KING(7, 4, 4, 4, 4), //
	ASS(8, 11, 11, 11, 0);

	private int position;
	private int value;
	private int valueTrump;
	private int valueUpsideDown;
	private int valueDownsideUp;
}
