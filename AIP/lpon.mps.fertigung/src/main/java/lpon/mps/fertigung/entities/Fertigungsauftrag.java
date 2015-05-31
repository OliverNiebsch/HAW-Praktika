package lpon.mps.fertigung.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lpon.mps.auftragsverwaltung.entities.Auftrag;

@Entity
public class Fertigungsauftrag {
	public Fertigungsauftrag() {}

	public Fertigungsauftrag(Auftrag auftrag) {
		this.auftragId = auftrag.getId();
		fertigungsplan = new Fertigungsplan(auftrag.getAngebot()
				.getPositionen());
	}

	@Id
	@GeneratedValue
	private Long id;

	private Long auftragId;

	@OneToOne
	private Fertigungsplan fertigungsplan;

	public Long getId() {
		return id;
	}

	public Long getAuftrag() {
		return auftragId;
	}

	public Fertigungsplan getFertigungsplan() {
		return fertigungsplan;
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
		return "Fertigungsauftrag [id=" + id + ", auftrag=" + auftragId + "]";
	};
}
