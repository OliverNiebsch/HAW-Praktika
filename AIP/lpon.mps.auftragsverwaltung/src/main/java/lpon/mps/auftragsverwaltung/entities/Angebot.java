package lpon.mps.auftragsverwaltung.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lpon.mps.stammdatenadapter.entities.Artikel;
import lpon.mps.stammdatenadapter.entities.Kunde;


@Entity
public class Angebot {
	
	public Angebot() {}
	
	public Angebot(Kunde kunde, List<Artikel> positionen) {
		super();
		this.kundeId = kunde.getId();
		
		this.positionen = new ArrayList<Long>();
		for (Artikel a : positionen) {
			this.positionen.add(a.getId());
		}
	}

	@Id
	@GeneratedValue
	private Long id;

	private Long kundeId;
	
	@ElementCollection
	private List<Long> positionen;

	public Long getKunde() {
		return kundeId;
	}

	public void setKunde(Long kundeId) {
		this.kundeId = kundeId;
	}

	public List<Long> getPositionen() {
		return positionen;
	}

	public void setPositionen(List<Long> positionen) {
		this.positionen = positionen;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Angebot [id=" + id + ", kunde=" + kundeId + ", positionen="
				+ positionen + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Angebot other = (Angebot) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
