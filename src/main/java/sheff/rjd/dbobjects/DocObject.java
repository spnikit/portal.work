package sheff.rjd.dbobjects;


public class DocObject {
	Integer id;
	String name;
	String number;
	String crdatetime;
	Integer prfid;
	String prfirstsignature;
	String fsdatetime;
	Integer prsid;
	String prsecondsignature;
	String ssdatetime;
	String doc;
	//String doc;
	String declinedText;
	String inuseName;
	String inuseWrkName;
	boolean isInUse;
	boolean IsGroupSgn;
	String lastPersonSGN;
	boolean isDrooped;
	public void setisDrooped(boolean isDrooped) {
		this.isDrooped = isDrooped;
	}
	public boolean getisDrooped() {
		return isDrooped;
	}
	public void setlastPersonSGN(String lastPersonSGN) {
		this.lastPersonSGN = lastPersonSGN;
	}
	public String getlastPersonSGN() {
		return lastPersonSGN;
	}
	
	public boolean getIsGroupSgn() {
		return IsGroupSgn;
	}
	public void setIsGroupSgn(boolean IsGroupSgn) {
		this.IsGroupSgn = IsGroupSgn;
	}
	public boolean getisInUse() {
		return isInUse;
	}
	public void setisInUse(boolean isInUse) {
		this.isInUse = isInUse;
	}
	public String getWkrInuseName() {
		return inuseWrkName;
	}
	public void setWrkInuseName(String inuseWrkName) {
		this.inuseWrkName = inuseWrkName;
	}
	public String getInuseName() {
		return inuseName;
	}
	public void setInuseName(String inuseName) {
		this.inuseName = inuseName;
	}
	public String getDeclinedText() {
		return declinedText;
	}
	public void setDeclinedText(String declinedText) {
		this.declinedText = declinedText;
	}
	public Integer getPrfid() {
		return prfid;
	}
	public void setPrfid(Integer prfid) {
		this.prfid = prfid;
	}
	public Integer getPrsid() {
		return prsid;
	}
	public void setPrsid(Integer prsid) {
		this.prsid = prsid;
	}
	/*public byte[] getDoc() {
		return doc;
	}
	public void setDoc(byte[] doc) {
		this.doc = doc;
	}*/
	public String getDoc() {
		return doc;
	}
	public void setDoc(String bs) {
		this.doc = bs;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getCrdatetime() {
		return crdatetime;
	}
	public void setCrdatetime(String crdatetime) {
		this.crdatetime = crdatetime;
	}
	public String getPrfirstsignature() {
		return prfirstsignature;
	}
	public void setPrfirstsignature(String prfirstsignature) {
		this.prfirstsignature = prfirstsignature;
	}
	public String getFsdatetime() {
		return fsdatetime;
	}
	public void setFsdatetime(String fsdatetime) {
		this.fsdatetime = fsdatetime;
	}
	public String getPrsecondsignature() {
		return prsecondsignature;
	}
	public void setPrsecondsignature(String prsecondsignature) {
		this.prsecondsignature = prsecondsignature;
	}
	public String getSsdatetime() {
		return ssdatetime;
	}
	public void setSsdatetime(String ssdatetime) {
		this.ssdatetime = ssdatetime;
	}

	
}
