package sheff.rjd.gvcservice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.aisa.rgd.ws.utility.TypeConverter;
import rzd.util.PrednStanFullInfoResponseDocument;
import rzd.util.PrednStanFullInfoResponseDocument.PrednStanFullInfoResponse;
import sheff.rjd.utils.XMLUtil;

public class GetPrednStanFullInfo extends AbstractMarshallingPayloadEndpoint {
    private NamedParameterJdbcTemplate npjt;
    private static Logger	log	= Logger.getLogger(GetPrednStanFullInfo.class);
	public  GetPrednStanFullInfo(Marshaller marshaller) {
		super(marshaller);
	}
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	protected Object invokeInternal(final Object inn)  {
		final ArrayList<String> sqlArr = new ArrayList<String>();
		// 0 - getById
		int getById = 0;
		sqlArr
				.add("SELECT ID AS ID, KOD AS CODE, DORID AS DORID, TYPE AS TYPE, rtrim(VNAME) AS VNAME,"
						+ " rtrim(NAME) AS NAME, rtrim(ONAME) AS ONAME, ISSELF AS ISSELF, ACTIV AS ACTIV FROM"
						+ " letd.pred WHERE ID = :ID and ACTIV = 'Y' FETCH FIRST row only");
		
		// 1 - getByIdAndType
		int getByIdAndType = 1;
		sqlArr
				.add("SELECT ID AS ID, KOD AS CODE, DORID AS DORID, TYPE AS TYPE, rtrim(VNAME) AS VNAME,"
						+ " rtrim(NAME) AS NAME, rtrim(ONAME) AS ONAME, ISSELF AS ISSELF, ACTIV AS ACTIV FROM"
						+ " letd.pred WHERE ID = :ID AND TYPE = :TYPE and ACTIV = 'Y' FETCH FIRST row only");
		
		// 2 - getByCode
		int getByCode = 2;
		sqlArr
				.add("SELECT ID AS ID, KOD AS CODE, DORID AS DORID, TYPE AS TYPE, rtrim(VNAME) AS VNAME,"
						+ " rtrim(NAME) AS NAME, rtrim(ONAME) AS ONAME, ISSELF AS ISSELF, ACTIV AS ACTIV FROM"
						+ " letd.pred WHERE KOD = :CODE and ACTIV = 'Y' FETCH FIRST row only");
		
		// 3 - getByCodeAndType
		int getByCodeAndType = 3;
//		sqlArr
//				.add("SELECT ID AS ID, KOD AS CODE, DORID AS DORID, TYPE AS TYPE, rtrim(VNAME) AS VNAME,"
//						+ " rtrim(NAME) AS NAME, rtrim(ONAME) AS ONAME, ISSELF AS ISSELF, ACTIV AS ACTIV FROM"
//						+ " letd.pred WHERE KOD = :CODE AND TYPE = :TYPE and ACTIV = 'Y' FETCH FIRST row only");
		sqlArr.add("SELECT STAN_ID AS ID, ST_KOD AS CODE, DOR_KOD AS DORID, 0 AS TYPE, rtrim(VNAME) AS VNAME,"
				+ " rtrim(NAME) AS NAME, '' AS ONAME, 0 AS ISSELF, ACTIV AS ACTIV FROM "
				+"snt.stan WHERE ST_KOD = :CODE and ACTIV = 'Y' FETCH FIRST row only");
		
		
		
		// 4 - getByIdAndDorId
		int getByIdAndDorId = 4;
		sqlArr
				.add("SELECT ID AS ID, KOD AS CODE, DORID AS DORID, TYPE AS TYPE, rtrim(VNAME) AS VNAME,"
						+ " rtrim(NAME) AS NAME, rtrim(ONAME) AS ONAME, ISSELF AS ISSELF, ACTIV AS ACTIV FROM"
						+ " letd.pred WHERE ID = :ID AND DORID = :DORID and ACTIV = 'Y' FETCH FIRST row only");
		
		// 5 - getByIdAndTypeAndDorId
		int getByIdAndTypeAndDorId = 5;
		sqlArr
				.add("SELECT ID AS ID, KOD AS CODE, DORID AS DORID, TYPE AS TYPE, rtrim(VNAME) AS VNAME,"
						+ " rtrim(NAME) AS NAME, rtrim(ONAME) AS ONAME, ISSELF AS ISSELF, ACTIV AS ACTIV FROM"
						+ " letd.pred WHERE ID = :ID AND TYPE = :TYPE AND DORID = :DORID and ACTIV = 'Y' FETCH FIRST row only");
		
		// 6 - getByCodeAndDorId
		int getByCodeAndDorId = 6;
		sqlArr
				.add("SELECT ID AS ID, KOD AS CODE, DORID AS DORID, TYPE AS TYPE, rtrim(VNAME) AS VNAME,"
						+ " rtrim(NAME) AS NAME, rtrim(ONAME) AS ONAME, ISSELF AS ISSELF, ACTIV AS ACTIV FROM"
						+ " letd.pred WHERE KOD = :CODE AND DORID = :DORID and ACTIV = 'Y' FETCH FIRST row only");
		
		// 7 - getByCodeAndTypeAndDorId
		int getByCodeAndTypeAndDorId = 7;
		sqlArr
				.add("SELECT ID AS ID, KOD AS CODE, DORID AS DORID, TYPE AS TYPE, rtrim(VNAME) AS VNAME,"
						+ " rtrim(NAME) AS NAME, rtrim(ONAME) AS ONAME, ISSELF AS ISSELF, ACTIV AS ACTIV FROM"
						+ " letd.pred WHERE KOD = :CODE AND TYPE = :TYPE AND DORID = :DORID and ACTIV = 'Y' FETCH FIRST row only");
		
		// 8 - getByDorId
		int getByDorId = 8;
		sqlArr
				.add("SELECT rtrim(NAME) AS DORNAME FROM letd.dor WHERE id = :DORID FETCH FIRST row only");
		
		PrednStanFullInfoResponseDocument response = PrednStanFullInfoResponseDocument.Factory
				.newInstance();
		final PrednStanFullInfoResponse resp = response
				.addNewPrednStanFullInfoResponse();
		
		String req = inn.toString();
		
		Integer id = -1;
		String code = "";
		Integer type = -1;
		Integer dorid = -1;
		
		try
		{
			if (XMLUtil.parseXmlWithSax(req, "ID").length() > 0)
			{
				id = Integer.parseInt(XMLUtil.parseXmlWithSax(req, "ID"));
			}
			
			if (XMLUtil.parseXmlWithSax(req, "CODE").length() > 0)
			{
				code = XMLUtil.parseXmlWithSax(req, "CODE");
			}
			
			if (XMLUtil.parseXmlWithSax(req, "TYPE").length() > 0)
			{
				type = Integer.parseInt(XMLUtil.parseXmlWithSax(req, "TYPE"));
			}
			
			if (XMLUtil.parseXmlWithSax(req, "DORID").length() > 0)
			{
				dorid = Integer.parseInt(XMLUtil.parseXmlWithSax(req, "DORID"));
			}
			
		}
		catch (Exception e1)
		{
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter(outError);
			e1.printStackTrace(errorWriter);
			log.error(outError.toString());
		}
		
		List resPredList = new ArrayList();
		List resDorList = new ArrayList();
		
		try
		{
			// Выбираем один из вариантов ответа
			if (id != -1)
			{
				if (type != -1 && dorid != -1)
				{
					Map map = new HashMap();
					map.put("ID", id);
					map.put("TYPE", type);
					map.put("DORID", dorid);
					
					resPredList = getNpjt().queryForList(
							sqlArr.get(getByIdAndTypeAndDorId), map);
				}
				else if (type != -1)
				{
					Map map = new HashMap();
					map.put("ID", id);
					map.put("TYPE", type);
					
					resPredList = getNpjt().queryForList(
							sqlArr.get(getByIdAndType), map);
				}
				else if (dorid != -1)
				{
					Map map = new HashMap();
					map.put("ID", id);
					map.put("DORID", dorid);
					
					resPredList = getNpjt().queryForList(
							sqlArr.get(getByIdAndDorId), map);
				}
				else
				{
					Map map = new HashMap();
					map.put("ID", id);
					
					resPredList = getNpjt().queryForList(sqlArr.get(getById),
							map);
				}
			}
			else if (code != "")
			{
				if (type != -1 && dorid != -1)
				{
					Map map = new HashMap();
					map.put("CODE", code);
					map.put("TYPE", type);
					map.put("DORID", dorid);
					
					resPredList = getNpjt().queryForList(
							sqlArr.get(getByCodeAndTypeAndDorId), map);
				}
				else if (type != -1)
				{
					
					if (code.toString().length()==5){
						code = ("0"+code.toString()).substring(0, 5);
					}
					
					
					else if (code.toString().length()==6){
						code = code.toString().substring(0, 5);
					}				
					
					Map map = new HashMap();
					map.put("CODE", code);
					map.put("TYPE", type);
					try{
					resPredList = getNpjt().queryForList(
							sqlArr.get(getByCodeAndType), map);
					} catch (Exception e){
						log.error(TypeConverter.exceptionToString(e));
					}
					
										
				}
				else if (dorid != -1)
				{
					Map map = new HashMap();
					map.put("CODE", code);
					map.put("DORID", dorid);
					
					resPredList = getNpjt().queryForList(
							sqlArr.get(getByCodeAndDorId), map);
				}
				else
				{
					Map map = new HashMap();
					map.put("CODE", code);
					
					resPredList = getNpjt().queryForList(sqlArr.get(getByCode),
							map);
				}
			}
			else if (dorid != -1)
			{
				Map map = new HashMap();
				map.put("DORID", dorid);
				
				resDorList = getNpjt()
						.queryForList(sqlArr.get(getByDorId), map);
			}
			else
			{
				// Просто так =)
			}
		}
		catch (Exception e)
		{

//			ConsoleNk.println(e);
			log.error(e.toString());

			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter(outError);
			e.printStackTrace(errorWriter);
			log.error(outError.toString());

		}
		
		try
		{
			// Заполняем респонс полученными данными
			if (resPredList.size() > 0)
			{
				Map map = (Map) resPredList.get(0);
				
				resp.setID(map.get("ID").toString());
				if (map.get("CODE") != null) resp.setCode(map.get("CODE")
						.toString());
				if (map.get("TYPE") != null) resp.setType(map.get("TYPE")
						.toString());
				if (map.get("VNAME") != null) resp.setVName(map.get("VNAME")
						.toString());
				if (map.get("NAME") != null) resp.setName(map.get("NAME")
						.toString());
				if (map.get("ONAME") != null) resp.setOName(map.get("ONAME")
						.toString());
				
				if (map.get("DORID") != null)
				{
					resp.setDorId(map.get("DORID").toString());
					
					Map sqlMap = new HashMap();
					sqlMap.put("DORID", map.get("DORID"));
					
					Map map2 = (Map) (getNpjt().queryForList(sqlArr
							.get(getByDorId), sqlMap)).get(0);
					
					if (map2.get("DORNAME") != null) resp.setDorName(map2.get(
							"DORNAME").toString());
				}
			}
			else if (resDorList.size() > 0)
			{
				Map map = (Map) resDorList.get(0);
				
				if (map.get("DORNAME") != null) resp.setDorName(map.get(
						"DORNAME").toString());
			}
			else
			{
				// Просто так =)
			}
		}
		catch (Exception e)
		{
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter(outError);
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
		}
		
		return response;
	}
	
	
}

