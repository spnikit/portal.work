package ru.aisa.etdadmin.controllers.MultipartImpl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;

public class HeadidSave extends AbstractMultipartController{

	public HeadidSave() throws JSONException{
		
		super();
	}

	@Override
	protected void export(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String headidData="";		
		headidData = request.getParameter("id");
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	        Date currDate = new Date();
	        
		request.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Disposition",
				"attachment; filename="+sdf.format(currDate)+".csv");
		response.setContentType("application/octet-strem");
			System.out.println();
		if(headidData!=null){
			String[] headidMass =headidData.split(";");
			String sql = "select etdid, vagnum, (select rtrim(name) name from snt.doctype where id = ds.typeid), (select rtrim(vname) vname from snt.pred where id = ds.predid) from snt.docstore ds where etdid = :ETDID";
			StringBuffer writer = new StringBuffer();
		    writer.append("ETDID");
		    writer.append(',');
		    writer.append("VAGNUM");
		    writer.append(',');
		    writer.append("NAME");
		    writer.append(',');
		    writer.append("VNAME");
		    writer.append('\n');
			
			
			
			for(int j =0; j < headidMass.length;j++){
			HashMap in = new HashMap();
			in.put("ETDID", Integer.parseInt(headidMass[j]));

			
		    
		    
		    
		List<HeadidDoc> list =	getNpjt().query(sql, in, new ParameterizedRowMapper<Object>() {

				public HeadidDoc mapRow(ResultSet rs, int numrow) throws SQLException {
					
					HeadidDoc doc = new HeadidDoc();
					
					doc.setEtdid(rs.getString("etdid"));
					doc.setVagnum(rs.getString("vagnum"));
					doc.setName(rs.getString("name"));
					doc.setVname(rs.getString("vname"));
				  
				   
					return doc;
				}
			});

	    for(int i =0; i<list.size();i++){
	    	
	    	writer.append(list.get(i).getEtdid());
	    	writer.append(',');
	    	writer.append(list.get(i).getVagnum());
	    	writer.append(',');
	    	writer.append(list.get(i).getName());
	    	writer.append(',');
	    	writer.append(list.get(i).getVname());
	    	 writer.append('\n');
	    }
		
	}
		
			byte[] csv = writer.toString().getBytes("UTF-8");
			
			response.setContentLength(csv.length);
			response.getWriter().write(writer.toString());
		
		}	
	}
	
	public class HeadidDoc{
		String etdid;
		String vagnum;
		String name;
		String vname;
		public String getEtdid() {
			return etdid;
		}
		public void setEtdid(String etdid) {
			this.etdid = etdid;
		}
		public String getVagnum() {
			return vagnum;
		}
		public void setVagnum(String vagnum) {
			this.vagnum = vagnum;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getVname() {
			return vname;
		}
		public void setVname(String vname) {
			this.vname = vname;
		}
		
		
	}
	
	
}



