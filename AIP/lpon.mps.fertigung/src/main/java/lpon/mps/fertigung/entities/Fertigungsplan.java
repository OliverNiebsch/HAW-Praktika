package lpon.mps.fertigung.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lpon.mps.stammdatenadapter.entities.Artikel;


@Entity
public class Fertigungsplan {
	public Fertigungsplan() {}
	
	public Fertigungsplan(List<Artikel> artikel) {
		artikelFertigungsStatus = new HashMap<Long, Boolean>();
		for (Artikel a : artikel) {
			artikelFertigungsStatus.put(a.getId(), false);
		}
	}
	
	@Id
	@GeneratedValue
	private Long id;

	@ElementCollection(fetch=FetchType.EAGER)
	private Map<Long, Boolean> artikelFertigungsStatus;

	public Long getId() {
		return id;
	}
	
	public boolean signalGefertigtesTeil(Artikel a) {
		if (artikelFertigungsStatus.containsKey(a.getId()))
			artikelFertigungsStatus.put(a.getId(), true);	
		
		return !artikelFertigungsStatus.containsValue(false);
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
		Fertigungsplan other = (Fertigungsplan) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Fertigungsplan [id=" + id + "]";
	}
}
