package sheff.rjd.syncing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

 

import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractSimpleController;

public class Transfering extends AbstractSimpleController {

	public Transfering() throws JSONException {
		super();
	}
	@Override
	public JSONArray get(HttpServletRequest request) {
		final JSONArray response = new JSONArray();

		try{
			final Map pp = new HashMap();
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			Connection db2ConnFrom = 
				DriverManager.getConnection("jdbc:db2://10.200.7.2:5420/RURZDGVCDC0D", "DETDoC01", 
				"DETD5071");
			Statement stfrom = db2ConnFrom.createStatement();
			Statement stfrom2 = db2ConnFrom.createStatement();
			Statement stfromDOR = db2ConnFrom.createStatement();
			Statement stfrom2DOR = db2ConnFrom.createStatement();
			Statement stfromADM = db2ConnFrom.createStatement();
			Statement stfrom2ADM = db2ConnFrom.createStatement();

			String count = "SELECT count(*) FROM LETD.PRED"; 
			String query = 
				"SELECT ID,TYPE,DORID,KOD,VNAME,NAME,URL,ISSELF,STPRIM,ZONECNT,ACTIV" +
				" FROM LETD.PRED where TYPE in (0,1)"; 
			String countDor = "SELECT count(*) FROM LETD.DOR"; 
			String queryDor = 
				"SELECT ID,ADMID,NAME,SNAME" +
				" FROM LETD.DOR"; 
			String countADM = "SELECT count(*) FROM LETD.ADMJD"; 
			String queryADM = 
				"SELECT ID,COUNTY,NAME,SNAMER,SNAMEA" +
				" FROM LETD.ADMJD"; 

			ArrayList<Object[]> lst2 = new ArrayList<Object[]>();
			ArrayList<Object[]> lst2DOR = new ArrayList<Object[]>();
			ArrayList<Object[]> lst2ADM = new ArrayList<Object[]>();
			ResultSet rs = stfrom.executeQuery(query); 		  
			ResultSet rsCnt=stfrom2.executeQuery(count);			
			ResultSet rsDOR = stfromDOR.executeQuery(queryDor); 		  
			ResultSet rsCntDOR=stfrom2DOR.executeQuery(countDor);
			ResultSet rsADM = stfromADM.executeQuery(queryADM); 		  
			ResultSet rsCntADM=stfrom2ADM.executeQuery(countADM);
			int amount=0;
			while(rsCnt.next()){
				amount=rsCnt.getInt(1);
			}
			int amountDOR=0;
			while(rsCntDOR.next()){
				amountDOR=rsCntDOR.getInt(1);
			}
			int amountADM=0;
			while(rsCntADM.next()){
				amountADM=rsCntADM.getInt(1);
			}
			System.out.println("amount to insert PRED "+amount);
			System.out.println("amount to insert DOR "+amountDOR);
			System.out.println("amount to insert ADM "+amountADM);


			while(rs.next()){
				lst2.add(new Object[]{rs.getLong("ID"),rs.getInt("TYPE"),rs.getInt("DORID"),
						rs.getLong("KOD")
						,rs.getString("VNAME"),rs.getString("NAME"),rs.getString("URL").trim(),
						rs.getString("ISSELF").trim(),
						rs.getLong("STPRIM"),rs.getInt("ZONECNT"),rs.getString("ACTIV").trim()});							
			}			
			System.out.println("pred added to lst ");
			while(rsDOR.next()){
				lst2DOR.add(new Object[]{rsDOR.getInt("ID"),rsDOR.getInt("ADMID"),rsDOR.getString("NAME"),rsDOR.getString("SNAME")});
			}
			System.out.println("dor added to lst ");
			while(rsADM.next()){
				lst2ADM.add(new Object[]{rsADM.getInt("ID"),rsADM.getString("COUNTY"),rsADM.getString("NAME"),rsADM.getString("SNAMER"),rsADM.getString("SNAMEA")});
			}
			System.out.println("adm added to lst ");
			String sqlDOR = "insert into SNT.DOR values(:IDDOR,:ADMID,:NAMED," +
			":SNAME)";
			String sqlADM = "insert into SNT.ADMJD values(:IDADM,:COUNTY,:NAMEDD," +
			":SNAMER,:SNAMEA)";
			String sql = "insert into SNT.PRED(ID,TYPE," +
			"DORID,KOD,VNAME,NAME,URL,ISSELF,STPRIM,ZONECNT,ACTIV" +
			") values" +
			"(:IDPRED,:TYPE,:DORID," +
			":KOD,Cast(':VNAME' as char(120)),CAST(':NAME' AS CHAR(120)),:URL,:ISSELF,:STPRIM,:ZONECNT,:ACTIV)";
			int rescount=0;
			int rescountDOR=0;
			int rescountADM=0;
			for(int i=0;i<lst2ADM.size();i++)
			{
				pp.put("IDADM",lst2ADM.get(i)[0]);
				pp.put("COUNTY",lst2ADM.get(i)[1]);
				pp.put("NAMEDD",lst2ADM.get(i)[2]);
				pp.put("SNAMER",lst2ADM.get(i)[3]);	
				pp.put("SNAMEA",lst2ADM.get(i)[4]);	
				int tmpcount= getNpjt().queryForInt("select count(*) from SNT.ADMJD where id=:IDADM", pp);
				if(tmpcount==0 )
					rescountADM+=getNpjt().update(sqlADM, pp);		

			}	
			System.out.println("adm inserted ");
			for(int i=0;i<lst2DOR.size();i++)
			{
				pp.put("IDDOR",lst2DOR.get(i)[0]);
				pp.put("ADMID",lst2DOR.get(i)[1]);
				pp.put("NAMED",lst2DOR.get(i)[2]);
				pp.put("SNAME",lst2DOR.get(i)[3]);
				int tmpcount=getNpjt().queryForInt("select count(*) from SNT.DOR where id=:IDDOR", pp);

				if( tmpcount==0 )
					rescountDOR+=getNpjt().update(sqlDOR, pp);				

			}	
			System.out.println("dor inserted ");
			for(int i=0;i<lst2.size();i++)
			{

				pp.put("IDPRED",lst2.get(i)[0]);
				pp.put("TYPE",lst2.get(i)[1]);
				pp.put("DORID",lst2.get(i)[2]);
				pp.put("KOD",lst2.get(i)[3]);
				pp.put("VNAME",lst2.get(i)[4]);
				pp.put("NAME",lst2.get(i)[5]);

				pp.put("URL",lst2.get(i)[6]);

				pp.put("ISSELF",lst2.get(i)[7]);
				pp.put("STPRIM",lst2.get(i)[8]);
				pp.put("ZONECNT",lst2.get(i)[9]);
				pp.put("ACTIV",lst2.get(i)[10]);
				int tmpcount=getNpjt().queryForInt("select count(*) from SNT.PRED where id=:IDPRED", pp);

				try{
					if( tmpcount==0 )
					{
						rescount+=getNpjt().update(sql, pp);	
						//System.out.println(pp.get("IDPRED"));
					}
					//System.out.println("ID: "+pp.get("IDPRED")+" amount:"+tmpcount);
				}catch(Exception eee){
					eee.printStackTrace(); 

					//System.out.println(pp.get("IDPRED")+" "+pp.get("TYPE")+" "+pp.get("DORID")+" "+
					//		pp.get("KOD")+" "+pp.get("VNAME")+" "+pp.get("NAME")+" "+pp.get("URL")+" "+
					//		pp.get("ISSELF")+" "+pp.get("STPRIM")+" "+pp.get("ZONECNT")+" "+pp.get("ACTIV"));
				}

				pp.clear();				
			}	
			System.out.println("pred inserted ");

			rs.close();
			rsCnt.close();
			stfrom.close();
			stfrom2.close();
			rsDOR.close();
			rsCntDOR.close();
			stfromDOR.close();
			stfrom2DOR.close();	
			rsADM.close();
			rsCntADM.close();
			stfromADM.close();
			stfrom2ADM.close();
			db2ConnFrom.close();
			System.out.println("amount rows inserted ADM: "+rescountADM);
			System.out.println("amount rows inserted DOR: "+rescountDOR);
			System.out.println("amount rows inserted PRED: "+rescount);
		}catch(Exception e){e.printStackTrace();}
		return response;
	}


}
