package sheff.rjd.services.repair;

import java.util.HashMap;
import java.util.List;

public class TOREK_DocumentObject {

	
private static HashMap<String,String> forms = new HashMap<String, String>();
	
private static HashMap<String,String> formcodes = new HashMap<String, String>();

	static{
		forms.put("B01", "Акт приема-передачи КП в ремонт");
		forms.put("B02", "Акт приема-передачи ЗЧ в ремонт");
		forms.put("B03", "ВУ-50");
		forms.put("B04", "Акт выбраковки");
		forms.put("B05", "Акт освидетельствования");
		forms.put("B06", "Уведомление об окончании ремонта");
		forms.put("B07", "Акт приема-передачи КП из ремонта");
		forms.put("B08", "Акт приема-передачи ЗЧ из ремонта");
		forms.put("B09", "МХ-1");
		forms.put("B10", "МХ-3");
		forms.put("B11", "ФПУ-26");
		forms.put("B12", "ГУ-45");
		forms.put("B13", "ФПУ-26 ремонтопригодность");
		forms.put("C04", "Комплект на пересылку в ремонт");
		forms.put("C05", "Комплект ремонтопригодности");
		forms.put("C06", "Комплект завершение ремонта");
		
		formcodes.put("Акт приема-передачи КП в ремонт","B01");
		formcodes.put("Акт приема-передачи ЗЧ в ремонт","B02");
		formcodes.put("ВУ-50","B03");
		formcodes.put("Акт выбраковки","B04");
		formcodes.put("Акт освидетельствования","B05");
		formcodes.put("Уведомление об окончании ремонта","B06");
		formcodes.put("Акт приема-передачи КП из ремонта","B07");
		formcodes.put("Акт приема-передачи ЗЧ из ремонта","B08");
		formcodes.put("МХ-1", "B09");
		formcodes.put("МХ-3", "B10");
		formcodes.put("ФПУ-26", "B11");
		formcodes.put("ГУ-45", "B12");
		formcodes.put("ФПУ-26 ремонтопригодность", "B13");
		formcodes.put("Комплект на пересылку в ремонт", "C04");
		formcodes.put("Комплект ремонтопригодности", "C05");
		formcodes.put("Комплект завершение ремонта", "C06");
	}
	
	/**
	 * Для документов из ТОРЕК
	 * @param id - ид документа (для pr_package = 1 ид ТОР ЭК, 2 и 3 - портал)
	 * @param docspec - код типа документа
	 * @param doctype - тип документа (S - скан, E или W - xml)
	 * @param docstatus - Статус документа:
1- документ отклонен получателем (при отклонении документа ВЧДЭ)
2 – документ подписан получателем (при подписании ранее направленного на согласование документа в  ВЧДЭ)
3 -  документ подписан отправителем (при отправке С04,В01,В02 в ВРК)
	 * @param signs - массив подписей
	 */
	public TOREK_DocumentObject(String torekid, String packid, String docspec, String doctype, int docstatus, List<SignInfoObject> signs) {
		super();
		this.torekid = torekid;
		this.packid = packid;
		this.docspec = docspec;
		this.formname = forms.get(docspec);
		this.doctype = doctype;
		this.docstatus = docstatus;
		this.signs = signs;
	}
	
	public TOREK_DocumentObject(){
		super();
	};
	
	/**Для документов из ВАРЕКС
	 * @param docspec
	 * @param doctype
	 * @param binary
	 * @param wareksid
	 * @param mark
	 */
	public TOREK_DocumentObject(String docspec, String doctype, byte[] binary,
			String wareksid, int mark) {
		super();
		this.docspec = docspec;
		this.doctype = doctype;
		this.binary = binary;
		this.wareksid = wareksid;
		this.mark = mark;
		this.formname = forms.get(docspec);
		
	}

	/**Для запроса пакета из ТОРЭК
	 * @param formname
	 * @param doctype
	 * @param binary
	 * @param signs - набор подписе
	 * @param torekid - для входящих torekid, для исходящих portalid
	 * @param docstatus - код статуса
	 * @param reason - причина отклонения (для неотклоненных "")
	 */
	public TOREK_DocumentObject(String formname, String doctype, byte[] binary,
			List<SignInfoObject> signs, String torekid, int docstatus, String reason) {
		super();
		this.docspec = formcodes.get(formname);
		this.doctype = doctype;
		this.binary = binary;
		this.signs = signs;
		this.torekid = torekid;
		this.docstatus = docstatus;
		this.reason = reason;
	}
	
	
	
	
	
	
	public TOREK_DocumentObject(String formname, long portalid, SignInfoObject sign) {
		super();
		this.docspec = formcodes.get(formname);
		this.portalid = portalid;
		this.sign = sign;
		
	}
	
	public TOREK_DocumentObject(long docid, String date, String fio, String reason, String docName) {
		super();
		this.docspec = formcodes.get(docName);
		this.portalid = docid;
		this.droptime = date;
		this.fio = fio;
		this.reason = reason;
	}






	String torekid;
	String packid;
	String docspec;
	String formname;
	String doctype;
	String filename;
	byte[] binary;
	SignInfoObject sign;
	List<SignInfoObject> signs;
	int docstatus;
	long portalid=-1;
	String wareksid;
	int mark;
	String reason;
	String droptime;
	String fio;
	boolean isscan = false;
	
	public String getTorekid() {
		return torekid;
	}

	public void setTorekid(String torekid) {
		this.torekid = torekid;
	}

	public String getPackid() {
		return packid;
	}

	public void setPackid(String packid) {
		this.packid = packid;
	}

	public String getDocspec() {
		return docspec;
	}

	public void setDocspec(String docspec) {
		this.docspec = docspec;
	}

	public String getFormname() {
		return formname;
	}
	
	public String getDoctype() {
		return doctype;
	}

	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}

	public byte[] getBinary() {
		return binary;
	}

	public void setBinary(byte[] binary) {
		this.binary = binary;
	}

	public List<SignInfoObject> getSigns() {
		return signs;
	}

	public void setSigns(List<SignInfoObject> signs) {
		this.signs = signs;
	}

	public int getDocstatus() {
		return docstatus;
	}

	public void setDocstatus(int docstatus) {
		this.docstatus = docstatus;
	}

	public long getPortalid() {
		return portalid;
	}

	public void setPortalid(long portalid) {
		this.portalid = portalid;
	}

	public String getWareksid() {
		return wareksid;
	}

	public void setWareksid(String wareksid) {
		this.wareksid = wareksid;
	}


	public int getMark() {
		return mark;
	}


	public void setMark(int mark) {
		this.mark = mark;
	}

	
	
	public SignInfoObject getSign() {
		return sign;
	}

	public void setSign(SignInfoObject sign) {
		this.sign = sign;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	public String getDroptime() {
		return droptime;
	}

	public void setDroptime(String droptime) {
		this.droptime = droptime;
	}

	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

	public boolean isIsscan() {
		return isscan;
	}

	public void setIsscan(boolean isscan) {
		this.isscan = isscan;
	}
	
	
	
	
}
