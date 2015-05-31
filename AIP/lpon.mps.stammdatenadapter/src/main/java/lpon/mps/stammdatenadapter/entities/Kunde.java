package lpon.mps.stammdatenadapter.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Kunde {
	
	public Kunde() {}
	
	@Id
	@GeneratedValue
	private Long id;

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Kunde [id=" + id + "]";
	}

}
