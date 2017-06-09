package ru.aisa.rgd.etd.report;

import java.util.ArrayList;
import java.util.List;

public class ReportDataMapper 
{
	public String xformsTag = "xforms:repeat";
	public String idTag = "id";
	public String repeatTag = "pane";
	public String fieldTag = "label";
	public String fieldNameAttr= "sid";
	
	public String id;

	List<ReportTableData> rows;

	public ReportDataMapper(String id) {
		super();
		this.id = id;
		this.rows = new ArrayList<ReportTableData>();
	}
	
	public ReportTableData addNewRow()
	{
		ReportTableData row = new ReportTableData();
		rows.add(row);
		return row;
	}

	public List<ReportTableData> getRows() {
		return rows;
	}

	public void setRows(List<ReportTableData> rows) {
		this.rows = rows;
	}

}
