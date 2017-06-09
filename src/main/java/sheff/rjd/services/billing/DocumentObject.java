package sheff.rjd.services.billing;

public class DocumentObject {
	private int id;
	private String name;
	
	public DocumentObject() {
		id = 0;
		name = "";
	}
	
	public DocumentObject(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

}
