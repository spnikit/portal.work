package ru.aisa.rgd.ws.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ru.aisa.rgd.ws.utility.Mappable;

//Участок пути
public class WayPart implements Serializable, Mappable{

	private static final long serialVersionUID = -6593547549083537333L;
	/*CREATE TABLE LETD.UP(
		    ID          INT NOT NULL PRIMARY KEY,
		    MagCode     SMALLINT,
		    Code        SMALLINT,
		    Name        CHAR(100),
		    StID1       INT REFERENCES LETD.Pred(ID),
		    StID2       INT REFERENCES LETD.Pred(ID)
		   --StartDate   DATE,
		   --EndDate     DATE
		);
		*/
	Integer id;
	Integer magCode;
	Integer code;
	String name;
	Integer stId1;
	Integer stId2;
	//Date startDate;
   // Date endDate;
    
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMagCode() {
		return magCode;
	}

	public void setMagCode(Integer magCode) {
		this.magCode = magCode;
	}

	public Integer getStId1() {
		return stId1;
	}

	public void setStId1(Integer stId1) {
		this.stId1 = stId1;
	}

	public Integer getStId2() {
		return stId2;
	}

	public void setStId2(Integer stId2) {
		this.stId2 = stId2;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, Object> map() 
	{
		Map<String, Object> map = new HashMap<String, Object>(6);
		map.put("id", id);
		map.put("magCode", magCode);
		map.put("code", code);
		map.put("name", name);
		map.put("stId1", stId1);
		map.put("stId2", stId2);
		return map;
	}
	
	@Override
	public String toString() 
	{
		char NL = Character.LINE_SEPARATOR;
		StringBuffer buff = new StringBuffer();
		Map map = map();
		
		for(String key : map().keySet())
		{
			buff.append(key + " = " + map.get(key) + NL);
		}
		return buff.toString();
	}

	

}
