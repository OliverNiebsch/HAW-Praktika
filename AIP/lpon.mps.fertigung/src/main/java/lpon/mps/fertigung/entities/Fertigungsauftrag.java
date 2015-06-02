package lpon.mps.fertigung.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lpon.mps.auftragsverwaltung.entities.Auftrag;
import lpon.mps.auftragsverwaltung.entities.AuftragState;
import lpon.mps.stammdatenadapter.entities.Artikel;

@Entity
public class Fertigungsauftrag {
	public Fertigungsauftrag() {}

	public Fertigungsauftrag(Auftrag auftrag) {
		this.auftrag = auftrag;
		fertigungsplan = new Fertigungsplan(auftrag.getAngebot()
				.getPositionen());
	}

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne(fetch=FetchType.EAGER)
	private Auftrag auftrag;

	@OneToOne(fetch=FetchType.EAGER)
	private Fertigungsplan fertigungsplan;

	public Long getId() {
		return id;
	}

	public Auftrag getAuftrag() {
		return auftrag;
	}

	public Fertigungsplan getFertigungsplan() {
		return fertigungsplan;
	}
	
	public boolean signalGefertigtesTeil(Artikel a) {
		boolean all = fertigungsplan.signalGefertigtesTeil(a);
		
		if (all) {
			// alle Bauteile fertig gefertigt
			auftrag.setState(AuftragState.GEFERTIGT);
		}
		
		return all;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Fertigungsauftrag))
			return false;
		if (id == null)
			throw new RuntimeException("Entity has not been saved.");
		return id.equals(((Fertigungsauftrag) other).getId());
	}

	@Override
	public int hashCode() {
		if (id == null)
			throw new RuntimeException("Entity has not been saved.");
		return id.hashCode();
	}

	@Override
	public String toString() {
		return "Fertigungsauftrag [id=" + id + ", auftrag=" + auftrag + "]";
	};
}
