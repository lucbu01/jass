package ch.bbzw.jass.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(JassTeamId.class)
public class JassTeam {

	@ManyToOne
	@Id
	private JassGame game;

	@Id
	private Byte index;

	@ManyToMany
	private List<JassUser> users = new ArrayList<JassUser>();

	public JassTeam(JassGame game, Byte index) {
		this.game = game;
		this.index = index;
	}
}
