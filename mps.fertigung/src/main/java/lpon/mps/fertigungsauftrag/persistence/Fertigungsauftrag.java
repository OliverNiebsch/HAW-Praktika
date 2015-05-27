package lpon.mps.fertigungsauftrag.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lpon.mps.auftrag.persistence.Auftrag;
import lpon.mps.fertigungsplan.Fertigungsplan;

@Entity
public class Fertigungsauftrag {
	@Id
	@GeneratedValue
	private int id;
	
	@OneToOne
	private Auftrag auftrag;
	
	@OneToOne
	private Fertigungsplan fertigungsplan;
	
	public Fertigungsauftrag(Auftrag auftrag) {
		fertigungsplan = new Fertigungsplan(auftrag.getAngebot().getPositionen());
	}
}
