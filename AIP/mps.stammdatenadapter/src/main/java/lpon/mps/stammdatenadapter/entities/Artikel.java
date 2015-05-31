package lpon.mps.stammdatenadapter.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 */
@Entity
public class Artikel {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, unique = true)
	private String bezeichnung;

	@ManyToMany(fetch=FetchType.EAGER)
	private List<Artikel> baugruppe = new ArrayList<Artikel>();

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String username) {
		this.bezeichnung = username;
	}

	public Collection<Artikel> getBaugruppe() {
		return baugruppe;
	}

	public void setBaugruppe(List<Artikel> baugruppe) {
		this.baugruppe = baugruppe;
	}

	@Override
	public String toString() {
		return "Artikel [id=" + id + ", bezeichnung=" + bezeichnung + "]";
	}
	
	
	//must not be used in collections before persisting
	@Override
	public int hashCode() {
		return id==null?0: (int)(id&0xffffffff);
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Artikel)) return false;
		return ((Artikel)other).id == id;
	}
}
