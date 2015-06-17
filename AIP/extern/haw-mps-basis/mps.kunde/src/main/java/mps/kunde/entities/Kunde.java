package mps.kunde.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Kunde {

	@Id
	@GeneratedValue
	private Long id;

	public Long getId() {
		return id;
	}


	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Kunde)) return false;
		if(id==null) throw new RuntimeException("Entity has not been saved.");
		return id.equals(((Kunde)other).getId());
	}
	
	@Override
	public int hashCode() {
		if(id==null) throw new RuntimeException("Entity has not been saved.");
		return id.hashCode();
	}
}
