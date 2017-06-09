package ru.aisa.rgd.ws.exeption;

public class SyncServiceError {
	private int code;
	private String message;
	private static long etdid;
	public static final SyncServiceError
			ERR_UNDEFINED = new SyncServiceError(-1, "Неопределенная ошибка", etdid),
			ERR_OK = new SyncServiceError(0, "ok", etdid),
			ERR_NO_RZD = new SyncServiceError(1, "В БД нет предприятия ОАО \"РЖД\"", etdid),
			ERR_NO_INN_KPP = new SyncServiceError(2, "В БД нет предприятия с такими ИНН и КПП", etdid),
			ERR_NO_OKPO = new SyncServiceError(3, "В БД нет предприятия с таким кодом ОКПО", etdid),
			ERR_ERROR_PREDID= new SyncServiceError(4, "Ошибка при определении predid", etdid),
			ERR_NO_DOCID_FOR_FORM= new SyncServiceError(5, "No docid for form", etdid),
			ERR_NO_FLOW_FORDOC = new SyncServiceError(6, "На Портале не поддерживается данный документ", etdid);			
							
							
	public SyncServiceError() {
		super();
		code = -1;
		message = new String("An unknown error occurred");
		}

	public SyncServiceError(long etdid) {
		super();
		code = -1;
		message = new String("An unknown error occurred");
		SyncServiceError.etdid = etdid;
		}
	
	public SyncServiceError(int code, String message, long etdid) {
		super();
		this.code = code;
		this.message = message;
		SyncServiceError.etdid = etdid;
		}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	

}
