package ch.bbzw.jass.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class JassUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	private String name;
	
	public JassUser(String name ) {
		this.name = name;
	}
}
