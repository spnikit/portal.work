package ru.aisa.rgd.ws.dao;

import ru.aisa.rgd.ws.domain.TerritorialObject;

public interface TerritorialObjectDao {
	
	TerritorialObject getById(int id);
	TerritorialObject getByCode(int id);
	String getNameById(int id);
	String getNameByCode(int code);
	

}
