package ru.aisa.rgd.etd.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportTableData 
{

	Map<String, String> data;
	List<ReportDataMapper> tables;
	
	public ReportTableData() {
		super();
		this.data = new HashMap<String, String>();
		this.tables = new ArrayList<ReportDataMapper>();
	}
	
	public ReportDataMapper addNewTable(String id)
	{
		ReportDataMapper table = new ReportDataMapper(id);
		tables.add(table);
		return table;
	}
	public void put(String field, String value)
	{
		data.put(field, value);
	}
	
	public String get(String field)
	{
		return data.get(field);
	}
	
	public Set<String> getFields()
	{
		return data.keySet();
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}
	
	public List<ReportDataMapper> getTables() {
		return tables;
	}

	public void setTables(List<ReportDataMapper> tables) {
		this.tables = tables;
	}
}
