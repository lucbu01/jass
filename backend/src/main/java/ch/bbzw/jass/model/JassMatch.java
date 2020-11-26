package ch.bbzw.jass.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(JassMatchId.class)
public class JassMatch {

	@ManyToOne
	@Id
	private JassGame game;

	@Id
	private Integer index;

	@OneToMany
	private List<JassRound> round;

	public JassMatch(JassGame game, Integer index) {
		this.game = game;
		this.index = index;
	}
}
