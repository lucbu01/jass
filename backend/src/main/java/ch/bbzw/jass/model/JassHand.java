package ch.bbzw.jass.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@IdClass(JassHandId.class)
public class JassHand {

	@ManyToOne
	@Id
	private JassMatch match;

	@ManyToOne
	private JassUser user;

	@Id
	@Enumerated(EnumType.STRING)
	private JassColor color;

	@Id
	@Enumerated(EnumType.STRING)
	private JassValue value;

	public JassHand(JassMatch match, JassUser user, JassCard card) {
		this.match = match;
		this.user = user;
		setCard(card);
	}

	public void setCard(JassCard card) {
		this.color = card.getColor();
		this.value = card.getValue();
	}

	public JassCard getCard() {
		return new JassCard(color, value);
	}

}
