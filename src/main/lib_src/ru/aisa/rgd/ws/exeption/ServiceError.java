package ru.aisa.rgd.ws.exeption;

public class ServiceError {
	private int code;
	private String message;

	public static final ServiceError
			ERR_UNKNOW = new ServiceError(1,"Неизвестная ошибка"),
			OK_WHILE_SAVING_TOREK = new ServiceError(0,"Запрос успешно обработан"),
			ERR_UNDEFINED = new ServiceError(-1, "Внутренняя ошибка приложения"),
			ERR_NON_DATA = new ServiceError(400,"Данный пакет отсутствует на портале"),
			
			ERR_NOT_IMPLEMENTED = new ServiceError(-2, "Not Implemented"),
			ERR_UNDEFINED_DOCTYPE = new ServiceError(-1, "Данный тип документа не поддерживается"),
			ERR_OK = new ServiceError(0, "Request successfully processed"),
			ERR_TEMPLATE_LOADING = new ServiceError(1,
					"Unable to upload a document template"),
			ERR_STATIONID_LOADING = new ServiceError(2,
					"Unable to get the number of stations"),
			ERR_DOCUMENT_SAVING = new ServiceError(3,
					"Unable to save a document in the database"),
			ERR_DOCUMENT_PARSING = new ServiceError(4,
					"Wrong format of the document"),
			ERR_STATIONID_EMPTY = new ServiceError(5,
					"Stations with the code does not exist"),
			ERR_TEMPLATE_EMPTY = new ServiceError(6,
					"Template does not exist, or more than one copy of the same name"),
			ERR_STATEMENT_EXECUTION = new ServiceError(7,
					"Database error in the performance of query"),
			ERR_ROADID_EMPTY = new ServiceError(8,
					"Road with this ID doesn't exist"),
			ERR_DOCUMENT_IS_SIGNED = new ServiceError(9,
					"Document is signed or rejected"),
			ERR_PICTURE_SIZE = new ServiceError(10,
					"Pictures are exceeded allowable size"),
			ERR_WRONG_ORDER_CURRENT_SIGN = new ServiceError(11,
					"Wrong order of current sign"),
			ERR_WRONG_NAME_ENTERPRISE = new ServiceError(12,
					"Wrong name enterprise"),
			ERR_DOCUMENT_IS_NOT_SIGNED = new ServiceError(13,
					"Document is not signed"),
			ERR_CHECK_CERT = new ServiceError(-3,
					"Сертификат пользователя не зарегистрирован на Портале ЭДО"),
			ERR_CHECK_PRED = new ServiceError(-4,
					"На Портале ЭДО нет предприятия с такими ИНН и КПП"),
			ERR_CHECK_PRED_BY_CERT = new ServiceError(-5,
					"Сертификат пользователя не зарегистрирован на предприятии с данными ИНН и КПП"),
			ERR_WRONG_DOCID = new ServiceError(2,
					"Документа с данным идентификатором не найдено"),
			ERR_WRONG_IDPAK = new ServiceError(2,
							"Пакета с данным идентификатором не найдено"),
			ERR_ARCHIVE_SF = new ServiceError(2,
									"Документ подписан или отклонен"),
			ERR_DOCDATA = new ServiceError(-7,
					"Ошибка при получении xml документа"),
			ERR_NOTARCHIVE = new ServiceError(3,
			"Документ находится в работе или отклонен"),
			ERR_CHECK_DOCSTORE = new ServiceError(3,
				"Данный идентификатор не является Пакетом документов ТОР"),
			ERR_NOTWORKING_SF = new ServiceError(3,
						"Документ не подлежит оформлению"),
			ERR_PACK_NEW = new ServiceError(2,
			"Пакет документов подписан или отклонен"),
			ERR_ACTTMC_NOTARCHIVE = new ServiceError(3,
					"Акт приема передачи ТМЦ не подписан или забракован"),
			ERR_WRONG_CODE_OKPO_OP = new ServiceError(14,
							"Wrong OKPO operator code"),
			ERR_WRONG_INN_OP = new ServiceError(15,
						"Wrong operator INN"),
			ERR_WRONG_KPP_OP = new ServiceError(16,
							"Wrong operator KPP"),	
			ERR_WRONG_CODE_OKPO = new ServiceError(17,
							"Wrong OKPO code"),
			ERR_WRONG_INN = new ServiceError(18,
							"Wrong INN"),
			ERR_WRONG_KPP = new ServiceError(19,
							"Wrong KPP"),			
			ERR_WRONG_TYPEFORM = new ServiceError(20,
							"Wrong typeform"),
			ERR_WRONG_CODESTATION = new ServiceError(21,
									"Wrong code station"),
			ERR_WRONG_MARK = new ServiceError(-1,
								"Не указано клеймо депо получателя"),
            ERR_WHILE_SAVING_TOREK = new ServiceError(-1,
										"Один или несколько документов не сохранены на Портале"),
			ERR_WRONG_CONTENT = new ServiceError(-1,
								"Wrong CONTENT");
														
	public ServiceError() {
		super();
		code = -1;
		message = new String("An unknown error occurred");
	}

	public ServiceError(int code, String message) {
		super();
		this.code = code;
		this.message = message;
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
