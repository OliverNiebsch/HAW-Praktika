package lpon.mps.auftrag.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lpon.mps.angebot.persistence.Angebot;

@Entity
public class Auftrag {
	@Id
	@GeneratedValue
	private int id;
	
	@Column(nullable = false)
	private Angebot angebot;
	
	@Column(nullable = false)
	private AuftragState state;

	public Angebot getAngebot() {
		return angebot;
	}

	public void setAngebot(Angebot angebot) {
		this.angebot = angebot;
	}

	public AuftragState getState() {
		return state;
	}

	public void setState(AuftragState state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Auftrag other = (Auftrag) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Auftrag [id=" + id + "]";
	}
}
