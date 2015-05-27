package lpon.mps.angebot.persistence;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lpon.mps.artikel.persistence.Artikel;
import lpon.mps.kunde.persistence.Kunde;

@Entity
public class Angebot {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Kunde kunde;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<Artikel> positionen;

	public Kunde getKunde() {
		return kunde;
	}

	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}

	public List<Artikel> getPositionen() {
		return positionen;
	}

	public void setPositionen(List<Artikel> positionen) {
		this.positionen = positionen;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Angebot [id=" + id + ", kunde=" + kunde + ", positionen="
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
