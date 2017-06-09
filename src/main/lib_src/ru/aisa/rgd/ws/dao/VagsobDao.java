package ru.aisa.rgd.ws.dao;

import ru.aisa.rgd.ws.domain.Vagsob;

public interface VagsobDao {
	Vagsob getByLocKod(int code);
	public String getSNameByLocKod(int code); 
}
