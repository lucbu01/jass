package ch.bbzw.jass.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ch.bbzw.jass.model.JassCard;
import ch.bbzw.jass.model.JassColor;
import ch.bbzw.jass.model.JassValue;

public class JassCards {

	private JassCards() {
	}
	
	private static final JassCard[] CARDS = new JassCard[] {
			new JassCard(JassColor.CLAMP, JassValue.SIX),
			new JassCard(JassColor.CLAMP, JassValue.SEVEN),
			new JassCard(JassColor.CLAMP, JassValue.EIGHT),
			new JassCard(JassColor.CLAMP, JassValue.NINE),
			new JassCard(JassColor.CLAMP, JassValue.TEN),
			new JassCard(JassColor.CLAMP, JassValue.UNDER),
			new JassCard(JassColor.CLAMP, JassValue.OVER),
			new JassCard(JassColor.CLAMP, JassValue.KING),
			new JassCard(JassColor.CLAMP, JassValue.ASS),
			new JassCard(JassColor.SHIELD, JassValue.SIX),
			new JassCard(JassColor.SHIELD, JassValue.SEVEN),
			new JassCard(JassColor.SHIELD, JassValue.EIGHT),
			new JassCard(JassColor.SHIELD, JassValue.NINE),
			new JassCard(JassColor.SHIELD, JassValue.TEN),
			new JassCard(JassColor.SHIELD, JassValue.UNDER),
			new JassCard(JassColor.SHIELD, JassValue.OVER),
			new JassCard(JassColor.SHIELD, JassValue.KING),
			new JassCard(JassColor.SHIELD, JassValue.ASS),
			new JassCard(JassColor.ROSE, JassValue.SIX),
			new JassCard(JassColor.ROSE, JassValue.SEVEN),
			new JassCard(JassColor.ROSE, JassValue.EIGHT),
			new JassCard(JassColor.ROSE, JassValue.NINE),
			new JassCard(JassColor.ROSE, JassValue.TEN),
			new JassCard(JassColor.ROSE, JassValue.UNDER),
			new JassCard(JassColor.ROSE, JassValue.OVER),
			new JassCard(JassColor.ROSE, JassValue.KING),
			new JassCard(JassColor.ROSE, JassValue.ASS),
			new JassCard(JassColor.ACORN, JassValue.SIX),
			new JassCard(JassColor.ACORN, JassValue.SEVEN),
			new JassCard(JassColor.ACORN, JassValue.EIGHT),
			new JassCard(JassColor.ACORN, JassValue.NINE),
			new JassCard(JassColor.ACORN, JassValue.TEN),
			new JassCard(JassColor.ACORN, JassValue.UNDER),
			new JassCard(JassColor.ACORN, JassValue.OVER),
			new JassCard(JassColor.ACORN, JassValue.KING),
			new JassCard(JassColor.ACORN, JassValue.ASS)
	};
	
	public static List<JassCard> getJassCards() {
		List<JassCard> cards = new ArrayList<>();
		cards.addAll(Arrays.asList(CARDS));
		return cards;
	}

	public static List<JassCard> sort(List<JassCard> cards) {
		return cards.stream().sorted((x, y) -> x.compareTo(y)).collect(Collectors.toList());
	}
}
