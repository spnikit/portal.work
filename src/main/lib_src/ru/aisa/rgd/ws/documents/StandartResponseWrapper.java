package ru.aisa.rgd.ws.documents;

public  class  StandartResponseWrapper {
	int code;
	long documentId;
	String description;
	String cardId;
	String date1;
	String date2;
	String num_z;
	String term_num;
	String oper;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getDocumentId() {
		return documentId;
	}
	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}
	
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public String getDate2() {
		return date2;
	}

	public void setDate2(String date2) {
		this.date2 = date2;
	}

	public String getNum_z() {
		return num_z;
	}

	public void setNum_z(String num_z) {
		this.num_z = num_z;
	}

	public String getTerm_num() {
		return term_num;
	}

	public void setTerm_num(String term_num) {
		this.term_num = term_num;
	}

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}
}
