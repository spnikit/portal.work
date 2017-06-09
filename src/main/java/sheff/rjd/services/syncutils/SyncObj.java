package sheff.rjd.services.syncutils;

public class SyncObj {

private long docid;
private long etdid;	
private String id_pak;
private byte[] bldoc;
private String docdata;
private String doctype;
private int predid;	
private long fpuactid;
private String repdate;
private String vagnum;
private String content;
private String dogovor;
private int typeid;	
private int predcreator;
private int order;
private String no;
private int lwrkid;
private int parent;
private int signlvl;
private String di = "";
private String rem_pred="";
private int mark;
private boolean update  = false;
private int visible;
private int sf_type;
private int drop;
String mess1354 = ""; 
String mess4624 = "";
boolean isEtdSecondVU36 = false;
private String dropreason ="";
private long vu36_etdid;
private boolean no_sign = false;

public boolean isUpdate() {
	return update;
}
public void setUpdate(boolean update) {
	this.update = update;
}
public int getMark() {
	return mark;
}
public void setMark(int mark) {
	this.mark = mark;
}
public String getDi() {
	return di;
}
public void setDi(String di) {
	this.di = di;
}
public String getRem_pred() {
	return rem_pred;
}
public void setRem_pred(String rem_pred) {
	this.rem_pred = rem_pred;
}
public long getDocid() {
	return docid;
}
public void setDocid(long docid) {
	this.docid = docid;
}
public long getEtdid() {
	return etdid;
}
public void setEtdid(long etdid) {
	this.etdid = etdid;
}
public String getId_pak() {
	return id_pak;
}
public void setId_pak(String id_pak) {
	this.id_pak = id_pak;
}
public byte[] getBldoc() {
	return bldoc;
}
public void setBldoc(byte[] bldoc) {
	this.bldoc = bldoc;
}
public String getDocdata() {
	return docdata;
}
public void setDocdata(String docdata) {
	this.docdata = docdata;
}
public String getDoctype() {
	return doctype;
}
public void setDoctype(String doctype) {
	this.doctype = doctype;
}
public int getTypeid() {
	return typeid;
}
public void setTypeid(int typeid) {
	this.typeid = typeid;
}
public int getPredcreator() {
	return predcreator;
}
public void setPredcreator(int predcreator) {
	this.predcreator = predcreator;
}
public int getPredid() {
	return predid;
}
public void setPredid(int predid) {
	this.predid = predid;
}
public long getFpuactid() {
	return fpuactid;
}
public void setFpuactid(long fpuactid) {
	this.fpuactid = fpuactid;
}
public String getRepdate() {
	return repdate;
}
public void setRepdate(String repdate) {
	this.repdate = repdate;
}
public String getVagnum() {
	return vagnum;
}
public void setVagnum(String vagnum) {
	this.vagnum = vagnum;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public String getDogovor() {
	return dogovor;
}
public void setDogovor(String dogovor) {
	this.dogovor = dogovor;
}
public int getOrder() {
	return order;
}
public void setOrder(int order) {
	this.order = order;
}
public String getNo() {
	return no;
}
public void setNo(String no) {
	this.no = no;
}
public int getLwrkid() {
	return lwrkid;
}
public void setLwrkid(int lwrkid) {
	this.lwrkid = lwrkid;
}
public int getParent() {
	return parent;
}
public void setParent(int parent) {
	this.parent = parent;
}
public int getSignlvl() {
	return signlvl;
}
public void setSignlvl(int signlvl) {
	this.signlvl = signlvl;
}
public int getVisible() {
	return visible;
}
public void setVisible(int visible) {
	this.visible = visible;
}
public int getSf_type() {
	return sf_type;
}
public void setSf_type(int sf_type) {
	this.sf_type = sf_type;
}
public String getMess1354() {
	return mess1354;
}
public void setMess1354(String mess1354) {
	this.mess1354 = mess1354;
}
public String getMess4624() {
	return mess4624;
}
public void setMess4624(String mess4624) {
	this.mess4624 = mess4624;
}
public boolean isEtdSecondVU36() {
	return isEtdSecondVU36;
}
public void setEtdSecondVU36(boolean isEtdSecondVU36) {
	this.isEtdSecondVU36 = isEtdSecondVU36;
}
public int getDrop() {
	return drop;
}
public void setDrop(int drop) {
	this.drop = drop;
}
public String getDropreason() {
	return dropreason;
}
public void setDropreason(String dropreason) {
	this.dropreason = dropreason;
}
public long getVu36_etdid() {
	return vu36_etdid;
}
public void setVu36_etdid(long vu36_etdid) {
	this.vu36_etdid = vu36_etdid;
}
public boolean isNo_sign() {
	return no_sign;
}
public void setNo_sign(boolean no_sign) {
	this.no_sign = no_sign;
}
}
