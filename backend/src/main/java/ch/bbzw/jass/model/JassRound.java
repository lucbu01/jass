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
@IdClass(JassRoundId.class)
public class JassRound {

	@ManyToOne
	@Id
	private JassMatch match;

	@Id
	private Short index;

	@OneToMany
	private List<JassMove> moves;

	public JassRound(JassMatch match, Short index) {
		this.match = match;
		this.index = index;
	}
}
