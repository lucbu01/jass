package ch.bbzw.jass.model;

import java.util.List;

import lombok.Getter;

@Getter
public enum JassMatchType {
	CLAMP(2, JassColor.CLAMP), //
	SHIELD(2, JassColor.SHIELD), //
	ROSE(4, JassColor.ROSE), //
	ACORN(1, JassColor.ACORN), //
	UPSIDE_DOWN(3, JassDirection.UPSIDE_DOWN, false), //
	DOWNSIDE_UP(3, JassDirection.DOWNSIDE_UP, false), //
	GUSTI(5, JassDirection.GUSTI, true), //
	MERRY(5, JassDirection.MERRY, true), //
	SLALOM_UP(5, JassDirection.SLALOM_UP, true), //
	SLALOM_DOWN(5, JassDirection.SLALOM_DOWN, true);

	private final int multiplicator;
	private final JassDirection direction;
	private final JassColor trumpColor;
	private final boolean definitiveAnnouncerCanBegin;

	private JassMatchType(int multiplicator, JassDirection direction, boolean definitiveAnnouncerCanBegin) {
		this.trumpColor = null;
		this.multiplicator = multiplicator;
		this.direction = direction;
		this.definitiveAnnouncerCanBegin = definitiveAnnouncerCanBegin;
	}

	private JassMatchType(int multiplicator, JassColor trumpColor) {
		this.direction = null;
		this.definitiveAnnouncerCanBegin = false;
		this.multiplicator = multiplicator;
		this.trumpColor = trumpColor;
	}

	public int calculatePointsOfTeam(JassTeam team, List<JassRound> rounds) {
		int points = 0;
		for (JassRound round : rounds) {
			points += calculatePointsOfTeam(team, round.getMoves(), round.getColor(), round.getIndex());
		}
		return points == 157 ? 257 * multiplicator : points * multiplicator;
	}

	private int calculatePointsOfTeam(JassTeam team, List<JassMove> moves, JassColor startColor, short roundIndex) {
		JassMove winner = null;

		winner = calculateWinner(moves, startColor, roundIndex);

		if (winner != null && team.getUsers().contains(winner.getUser())) {
			int points = calculatePoints(moves);
			if (roundIndex == 8) {
				points += 5;
			}
			return points;
		}
		return 0;
	}

	public JassMove calculateWinner(List<JassMove> moves, JassColor startColor, short roundIndex) {
		JassMove winner;
		if (direction != null) {
			winner = getHighestOfColor(startColor, moves, roundIndex);
		} else {
			winner = getHighestOfColor(trumpColor, moves, roundIndex);
			if (winner == null) {
				winner = getHighestOfColor(startColor, moves, roundIndex);
			}
		}
		return winner;
	}

	private int calculatePoints(List<JassMove> moves) {
		int points = 0;
		for (JassMove move : moves) {
			if (trumpColor != null) {
				if (move.getColor() == trumpColor) {
					points += move.getValue().getValueTrump();
				} else {
					points += move.getValue().getValue();
				}
			} else {
				switch (direction) {
				case GUSTI:
				case UPSIDE_DOWN:
				case SLALOM_UP:
					points += move.getValue().getValueUpsideDown();
					break;

				default:
					points += move.getValue().getValueDownsideUp();
					break;
				}
			}
		}
		return points;
	}

	private JassMove getHighestOfColor(JassColor color, List<JassMove> moves, short roundIndex) {
		JassDirection actualDirection = JassDirection.UPSIDE_DOWN;
		if (direction != null) {
			actualDirection = direction.getActualDirection(roundIndex);
		}
		if (color == trumpColor) {
			return moves.stream().filter(move -> move.getColor() == color).sorted(
					(x, y) -> Integer.compare(y.getValue().getPostitionTrump(), x.getValue().getPostitionTrump()))
					.findFirst().orElse(null);
		} else if (actualDirection == JassDirection.DOWNSIDE_UP) {
			return moves.stream().filter(move -> move.getColor() == color)
					.sorted((x, y) -> Integer.compare(x.getValue().getPosition(), y.getValue().getPosition()))
					.findFirst().orElse(null);
		} else {
			return moves.stream().filter(move -> move.getColor() == color)
					.sorted((x, y) -> Integer.compare(y.getValue().getPosition(), x.getValue().getPosition()))
					.findFirst().orElse(null);
		}
	}
}
