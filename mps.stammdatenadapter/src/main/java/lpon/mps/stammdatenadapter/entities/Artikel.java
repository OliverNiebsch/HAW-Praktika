package lpon.mps.stammdatenadapter.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Artikel {
	
	public Artikel() { }
	
	public Artikel(Long id, List<Artikel> baugruppe, String bezeichnung) {
		super();
		this.id = id;
		this.baugruppe = baugruppe;
		this.bezeichnung = bezeichnung;
	}

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToMany
	private List<Artikel> baugruppe;
	
	@Column(nullable = false, unique = true)
	private String bezeichnung;

	public List<Artikel> getBaugruppe() {
		return baugruppe;
	}

	public void setBaugruppe(List<Artikel> baugruppe) {
		this.baugruppe = baugruppe;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Artikel: " + bezeichnung;
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
		Artikel other = (Artikel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
