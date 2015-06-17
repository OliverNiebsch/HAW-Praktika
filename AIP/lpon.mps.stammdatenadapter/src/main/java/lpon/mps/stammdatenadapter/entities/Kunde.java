package lpon.mps.stammdatenadapter.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Kunde {
	
	public Kunde() {}
	
	public Kunde(String name, String stadt) {
		this.name = name;
		this.stadt = stadt;
	}

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private String stadt;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStadt() {
		return stadt;
	}

	public void setStadt(String stadt) {
		this.stadt = stadt;
	}

	@Override
	public String toString() {
		return "Kunde [id=" + id + "]";
	}

}
