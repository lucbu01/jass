package ch.bbzw.jass.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@IdClass(JassTeamId.class)
public class JassTeam {

	@ManyToOne
	@Id
	private JassGame game;

	@Id
	private Byte index;

	@ManyToMany
	@OrderBy("id")
	private List<JassUser> users = new ArrayList<>();

	public JassTeam(JassGame game, Byte index) {
		this.game = game;
		this.index = index;
	}
}
