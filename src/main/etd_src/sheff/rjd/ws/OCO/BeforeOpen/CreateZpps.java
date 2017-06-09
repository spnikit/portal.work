package sheff.rjd.ws.OCO.BeforeOpen;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.DOMException;

import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowFiller;
import ru.aisa.rgd.ws.utility.RowHandler;

public class CreateZpps {
	
	
	public byte[] CreateZpss (byte[] template, long baseid , ETDFacade facade) throws UnsupportedEncodingException, ServiceException, IOException, InternalException{
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("etdid", baseid);
		byte[] mobytes = facade.getNpjt().queryForObject("select bldoc from snt.docstore where etdid = :etdid", pp, byte[].class);
		ETDForm MOform=ETDForm.createFromArchive(mobytes);
		DataBinder db = MOform.getBinder();
		ETDForm Zppsform=new ETDForm(template);
		DataBinder Zppsdb = Zppsform.getBinder();
		
		
		Zppsdb.setNodeValue("P_3", db.getValue("P_3"));
		Zppsdb.setNodeValue("P_4", db.getValue("P_4"));
		Zppsdb.setNodeValue("P_5", db.getValue("P_5"));
		Zppsdb.setNodeValue("P_6", db.getValue("P_6"));
		Zppsdb.setNodeValue("P_3_1", db.getValue("P_3_1"));
		Zppsdb.setNodeValue("P_3_2", db.getValue("P_3_2"));
		Zppsdb.setNodeValue("P_3_3", db.getValue("P_3_3"));
		Zppsdb.setNodeValue("P_13", db.getValue("P_16"));
		Zppsdb.setNodeValue("mo_id", baseid);
		Table bt = new Table();
		db.handleTable("table1", "row", new RowHandler<Table>() {
			public void handleRow(DataBinder db, int rowNum, Table bt)
					throws InternalException {
				TableEntry t = new TableEntry();
				t.numvag = db.getValue("P_7");
				t.iz_prod = db.getValue("P_8");
				t.iz_code = db.getValue("P_8_1");
				t.pod_prod= db.getValue("P_9");
				t.pod_code= db.getValue("P_9_1");
				bt.rowList.add(t);
			}

		}, bt);
		try{
		TableEntry[] tea = bt.rowList.toArray(new TableEntry[bt.rowList
				.size()]);
		Zppsdb.setRootElement("data");
		Zppsdb.fillTable(tea, new RowFiller<TableEntry, Object>() {
			public void fillRow(DataBinder Zppsdb, TableEntry te,
					int numRow, Object opt) throws DOMException,
					InternalException {
				Zppsdb.setNodeValue("P_7", te.numvag);
				Zppsdb.setNodeValue("P_8", te.iz_prod);
				Zppsdb.setNodeValue("P_8_1", te.iz_code);
				Zppsdb.setNodeValue("P_9", te.pod_prod);
				Zppsdb.setNodeValue("P_9_1", te.pod_code);
				
			}
		}, "table1", "row");
		} catch (Exception e){
			e.printStackTrace();
		}
		return Zppsform.toString().getBytes("UTF-8");
		}
	
	private class Table
	{
		public List<TableEntry> rowList = new ArrayList<TableEntry>();

	}
	private class TableEntry {
		public String numvag;
		public String iz_prod;
		public String iz_code;
		public String pod_prod;
		public String pod_code;
		

		@Override
		public String toString() {
			return "TableEntry [numvag=" + numvag + ", iz_prod="
					+ iz_prod + ",iz_code=" + iz_code + ", pod_prod="
					+ pod_prod + ", pod_code=" + pod_code + "]";
		}
}
}
