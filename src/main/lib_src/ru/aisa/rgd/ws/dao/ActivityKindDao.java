package ru.aisa.rgd.ws.dao;

import ru.aisa.rgd.ws.domain.ActivityKind;

public interface ActivityKindDao {
	
	ActivityKind getById(int id);
	String getNameById(int id);

}
