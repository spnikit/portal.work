package ru.aisa.rgd.ws.dao;

import ru.aisa.rgd.ws.domain.River;

public interface RiverDao {
	
	River getById(int id);
	River getByCode(int id);
	String getNameById(int id);
	String getNameByCode(int code);

}
