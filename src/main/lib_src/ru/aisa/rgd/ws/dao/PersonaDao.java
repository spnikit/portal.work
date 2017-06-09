package ru.aisa.rgd.ws.dao;


import java.util.List;

import ru.aisa.rgd.ws.domain.Persona;
import ru.aisa.rgd.ws.exeption.ServiceException;

public interface PersonaDao {
	
	Persona getById(int id) throws ServiceException;

	Persona getByCertId(String cert) throws ServiceException;
	
	int getPredId(String inn, String kpp) throws ServiceException;
	
	int getByFIO (String fName, String mName, String lName);
	
	int createFictUser (String fName, String mName, String lName);
	
	int getFictCountByFIO (String fName, String mName, String lName);
	
	int getFictByFIO (String fName, String mName, String lName);

}
