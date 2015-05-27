package lpon.mps.auftrag.persistence;

public enum AuftragState {
	ANGELEGT, IN_FERTIGUNG, GEFERTIGT;
	
	public String toString() {
		switch (this) {
		case ANGELEGT:
			return "wurde angelegt";
		case GEFERTIGT:
			return "ist in der Fertigung";
		case IN_FERTIGUNG:
			return "wurde gefertigt";
		default:
			return "";
		}
	};
}
