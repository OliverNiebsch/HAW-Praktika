package lpon.mps.auftragsverwaltung.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Auftrag {
	public Auftrag(){}
	
	public Auftrag(Angebot angebot) {
		super();
		this.angebot = angebot;
		this.state = AuftragState.ANGELEGT;
	}

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne
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

	public Long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		long result = 1;
		result = prime * result + id;
		return (int)result;
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
