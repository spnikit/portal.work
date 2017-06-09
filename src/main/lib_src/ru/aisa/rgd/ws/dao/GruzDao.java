package ru.aisa.rgd.ws.dao;

import ru.aisa.rgd.ws.domain.Gruz;

public interface GruzDao {
	Gruz getByCode(int id);
	public String getVNameByCode(int code); 
	public String getSNameByCode(int code);
}
