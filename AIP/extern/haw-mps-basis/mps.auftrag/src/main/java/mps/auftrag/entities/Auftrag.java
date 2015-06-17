package mps.auftrag.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import mps.kunde.entities.Kunde;

@Entity
public class Auftrag {

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private Kunde kunde;

	public Long getId() {
		return id;
	}
	
	public Kunde getKunde(){
		return kunde;
	}

	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Auftrag)) return false;
		if(id==null) throw new RuntimeException("Entity has not been saved.");
		return id.equals(((Auftrag)other).getId());
	}
	
	@Override
	public int hashCode() {
		if(id==null) throw new RuntimeException("Entity has not been saved.");
		return id.hashCode();
	}
}
