package ru.aisa.rgd.ws.exeption;


/**
 * @deprecated Not used
 */
public class ServiceMessage 
{
	//private String message;
	//private int paramsCount;
	
	public static final String
	
	PATTERN = "\\{param\\}",
	
	ERR_UNDEFINED =  "An unknown error occurred",
	ERR_OK =  "Request successfully processed",
	ERR_TEMPLATE_LOADING = "Unable to upload a document template",
	ERR_STATIONID_LOADING =  "Unable to get the number of stations",
	ERR_DOCUMENT_SAVING =  "Unable to save a document in the database",
	ERR_DOCUMENT_PARSING =  "Wrong format of the document",
	ERR_STATION_CODE_EMPTY =  "Enperprise with the code {param} does not exist",
	ERR_STATION_CODE_TYPE_EMPTY =  "Enperprise with the code {param} and type {param} does not exist",
	ERR_OKPO_CODE_EMPTY =  "Okpo_kod for enterprise with the otr_kod {param} does not exist",
	ERR_TEMPLATE_EMPTY =  "Template {param} does not exist, or more than one copy of the same name",
	ERR_STATEMENT_EXECUTION =  "Database error in the performance of query",
	ERR_ROADID_EMPTY =  "Road with this ID {param} doesn't exist";
}
