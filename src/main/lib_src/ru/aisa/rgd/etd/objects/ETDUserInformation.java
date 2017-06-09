package ru.aisa.rgd.etd.objects;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ETDUserInformation {
	Integer id;
	String name;
	String departmentName;
	Integer isnews;
	String news;
	List<ETDWorkInformation> workInfo;
	boolean autosgn;
	boolean issgn;
	public boolean isAutosgn() {
		return autosgn;
	}

	public void setAutosgn(boolean autosgn) {
		this.autosgn = autosgn;
	}

	public boolean isIssgn() {
		return issgn;
	}

	public void setIssgn(boolean issgn) {
		this.issgn = issgn;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public List<ETDWorkInformation> getWorkInfo() {
		return workInfo;
	}

	public void setWorkInfo(List<ETDWorkInformation> workInfo) {
		this.workInfo = workInfo;
	}
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	public String getNews() {
		return news;
	}

	public void setNews(String news) {
		this.news = news;
	}
	
	public Integer getIsNews() {
		return isnews;
	}

	public void setIsNews(Integer isnews) {
		this.isnews = isnews;
	}
	
	@Override
	public String toString() 
	{
		char NL = Character.LINE_SEPARATOR;
		StringBuffer buff = new StringBuffer();
		try
		{
			Field[] fields = getClass().getDeclaredFields();
	
			for(Field f : fields)
			{
				buff.append(f.getName() + " = " + f.get(this) + NL);
			}
		}
		catch(Exception e){}
		return buff.toString();
	}

	public ETDUserInformation() {
		super();
		this.workInfo = new ArrayList<ETDWorkInformation>();
	}
	public ETDWorkInformation addWorkInformation(){
		ETDWorkInformation wi = new ETDWorkInformation();
		this.workInfo.add(wi);
		return wi;
	}
}
