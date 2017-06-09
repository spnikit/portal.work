package com.aisa.portal.invoice.integration.errors;

import ru.aisa.rgd.ws.exeption.ServiceError;

public class TTKErrors extends  ServiceError{
public static final ServiceError
	
	ERR_SIGN_NOT_VALID = new ServiceError(-22, "Invalid signature"),
    ERR_NO_SF_OR_WRONG_STAGE = new ServiceError(-23, "Invalid SF docid or wrong stage")	,
    ERR_XML_VALID_NOTICE = new ServiceError(-24, "Invalid incoming xml structure");
}
