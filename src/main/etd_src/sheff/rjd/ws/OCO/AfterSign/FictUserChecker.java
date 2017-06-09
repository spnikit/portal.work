package sheff.rjd.ws.OCO.AfterSign;

import java.io.IOException;
import org.apache.log4j.Logger;
import ru.aisa.rgd.ws.dao.PersonaDao;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;

public class FictUserChecker {

	private PersonaDao personaDao; 

	public PersonaDao getPersonaDao() {
		return personaDao;
	}

	public void setPersonaDao(PersonaDao personaDao) {
		this.personaDao = personaDao;
	}

	protected final Logger	log	= Logger.getLogger(getClass());
	
	private DataBinder getBinder (ETDForm form) {
		
		DataBinder binder = null;
		try {
			binder = form.getBinder();
		} catch (ServiceException e) {
			log.error(TypeConverter.exceptionToString(e));
		}
		
		return binder;
	}
	
	public Integer getFictUser (ETDForm form, String action) throws IOException, InternalException {
		DataBinder binder = getBinder(form);		
		String fio = null;
		Integer id = null;

		if(action.equals("sign")) {
			fio = binder.getValue("P_55v");
		}
		if(action.equals("decline")) {
			fio = binder.getValue("decline_person");
		}
		if (fio == null || fio.length() < 1) return 0;
		
		try {
		String[] array = getArray(fio);
		String fName = array[0].trim();
		String mName = array[1].trim();
		String lName = array[2].trim();
		Integer count = getPersonaDao().getFictCountByFIO(fName, mName, lName);
		if (count == null || count < 1) {
			getPersonaDao().createFictUser(fName, mName, lName);
		}
			id = getPersonaDao().getFictByFIO(fName, mName, lName);
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
			return 0;
		}
		return id;
	}
	
	private String[] getArray (String fio) {
		String [] array = fio.trim().split(" ");
		if (array.length != 3) return null;
		return array;
	}

}
