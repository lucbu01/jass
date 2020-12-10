package ch.bbzw.jass.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
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

	@Enumerated(EnumType.STRING)
	private JassMatchType type;

	@ManyToOne
	private JassUser announcer;

	public JassMatch(JassGame game, Integer index, JassMatchType type, JassUser announcer) {
		this.game = game;
		this.index = index;
		this.type = type;
		this.announcer = announcer;
	}
}
