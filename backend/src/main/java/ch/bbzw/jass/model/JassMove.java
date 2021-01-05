package ch.bbzw.jass.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class JassMove {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@ManyToOne
	private JassRound round;

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
