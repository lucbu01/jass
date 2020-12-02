package ch.bbzw.jass.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(JassMoveId.class)
public class JassMove {

	@ManyToOne
	@Id
	private JassRound round;

	@Id
	private Byte index;

	@ManyToOne
	private JassUser user;

	@Enumerated(EnumType.STRING)
	private JassColor color;

	@Enumerated(EnumType.STRING)
	private JassValue value;

	public JassMove(JassRound round, Byte index, JassUser user, JassCard card) {
		this.round = round;
		this.index = index;
		this.user = user;
		this.setCard(card);
	}

	public void setCard(JassCard card) {
		this.color = card.getColor();
		this.value = card.getValue();
	}

	public JassCard getCard() {
		return new JassCard(color, value);
	}

}
