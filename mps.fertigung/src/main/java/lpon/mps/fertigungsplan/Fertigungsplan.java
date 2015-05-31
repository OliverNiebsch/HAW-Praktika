package lpon.mps.fertigungsplan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lpon.mps.stammdatenadapter.entities.Artikel;

@Entity
public class Fertigungsplan {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false)
	private Map<Long, Boolean> artikelFertigungsStatus;
	
	public Fertigungsplan(List<Artikel> artikel) {
		artikelFertigungsStatus = new HashMap<Long, Boolean>();
		for (Artikel a : artikel) {
			artikelFertigungsStatus.put(a.getId(), false);
		}
	}
}
