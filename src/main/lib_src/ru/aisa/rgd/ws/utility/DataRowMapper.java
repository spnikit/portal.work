package ru.aisa.rgd.ws.utility;

import java.util.Map;

public interface DataRowMapper<T> 
{
	Map<String, Object>  mapRow(T obj) ;
}
