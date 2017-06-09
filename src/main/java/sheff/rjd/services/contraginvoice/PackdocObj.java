package sheff.rjd.services.contraginvoice;

public class PackdocObj {
	Long docid;
	String docdata;
	String bldoc;
	int status;
	String formname;
	public Long getDocid() {
		return docid;
	}
	public void setDocid(Long docid) {
		this.docid = docid;
	}
	public String getDocdata() {
		return docdata;
	}
	public void setDocdata(String docdata) {
		this.docdata = docdata;
	}
	public String getBldoc() {
		return bldoc;
	}
	public void setBldoc(String bldoc) {
		this.bldoc = bldoc;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getFormname() {
		return formname;
	}
	public void setFormname(String formname) {
		this.formname = formname;
	}
}
