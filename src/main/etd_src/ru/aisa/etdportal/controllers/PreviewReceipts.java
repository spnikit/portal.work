package ru.aisa.etdportal.controllers;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PreviewReceipts extends AbstractPortalSimpleController {
    private static final String GET_STATUS = "select fl.SF_FVS1,fl.SF_FVS2,fl.SF_FVS3,fl.SF_FVS4,fl.SF_FVS5,fl.SF_FVS6,fl.SF_FVS7,fl.SF_FVS8,"
    		+ "case when df.UV_D1 is null then 0 else 1 end as SF_FVS9,"
    		+ "case when df.UV_D2 is null then 0 else 1 end as SF_FVS10,"
    		+"case when df.ID is null then 0 else 1 end as DF_ID "
    		+ "from snt.dfflow fl left join snt.dfkorr df on fl.ID=df.ID where fl.id = :docid";
    private static final String ID_PARAM = "id";
    private static final String COUNT = "count";
    private static final String ACTION = "action";
    
    public PreviewReceipts() throws JSONException {
        super();
    }
    
    @Override
    public JSONArray get(HttpServletRequest request) {
        JSONArray json = new JSONArray();
        JSONArray jsonFor = new JSONArray();
        JSONObject jsonObj = new JSONObject();
        String countRt = request.getParameter(COUNT);
        String strId = request.getParameter(ID_PARAM);
        String action = request.getParameter(ACTION);
        int counter = 0;
        
        
        if(action.equals("1")){
      
        if (strId != null) {
            try {
                Long id = Long.parseLong(strId);
                List status = status(id);   
                Map egrpo = (HashMap) status.get(0);
                if(Integer.valueOf(egrpo.get("DF_ID").toString())==1){
                	counter = 11;
                }else{
                	counter = 9;
                }
                for (int i = 1; i < counter; i++) {
                    JSONObject row = new JSONObject();
//                    row.put("NAME", "Квитанция №" + i);
                    row.put("NAME", InvoiceName.get(i));
                    row.put("STATUS", Integer.valueOf(egrpo.get("SF_FVS" + (i) + "").toString()));
                    row.put("STATUSNAME", Integer.valueOf(egrpo.get("SF_FVS" + (i) + "").toString()) == 1 ? "Подписана" : "Не оформлена");
                    row.put("ID", id);
                    row.put("NO", i);
       
                    jsonFor.put(row);
                }
                jsonObj.put("notice", jsonFor);
                json.put(jsonObj);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        }else{
     
        	 Long id = Long.parseLong(strId);
        	 int count = Integer.parseInt(countRt);
        	 try {
				String xml = xml(id, count);       	
        	 jsonObj.put("xml", xml);
        	 json.put(jsonObj);
        	 
        	 
        		} catch (UnsupportedEncodingException e) {
    				e.printStackTrace();
    			}catch (Exception e) {
                    e.printStackTrace();
                }
        	
        }
        
        return json;
    }

    private List status(long docid) {
        HashMap pp = new HashMap();
        pp.put("docid", docid);
        List aa = getNpjt().queryForList(GET_STATUS, pp);
        return aa;
    }
    
  
    
    private static HashMap<Integer, String> SF = new HashMap<Integer,String>();
    static{
    	SF.put(1, "SF_D1");
    	SF.put(2, "SF_D2");
    	SF.put(3, "SF_D3");
    	SF.put(4, "SF_D4");
    	SF.put(5, "SF_D5");
    	SF.put(6, "SF_D6");
    	SF.put(7, "SF_D7");
    	SF.put(8, "SF_D8");
    	SF.put(9, "UV_D1");
    	SF.put(10, "UV_D2");
    }

    private static HashMap<Integer, String> InvoiceName = new HashMap<Integer,String>();
    static{
    	
    	InvoiceName.put(1, "1. Подтверждение Оператором ЭДО поступления файла СФ от Продавца");
    	InvoiceName.put(2, "2. Извещение о получении подтверждения Оператора ЭДО Продавцом");
    	InvoiceName.put(3, "3. Подтверждение Оператором ЭДО отправки файла СФ Покупателю");
    	InvoiceName.put(4, "4. Извещение о получении СФ Покупателем");
    	InvoiceName.put(5, "5. Извещение о получении Покупателем подтверждения об отправке СФ Оператором ЭДО");
    	InvoiceName.put(6, "6. Подтверждение Оператора ЭДО о получении СФ");
    	InvoiceName.put(7, "7. Извещение о получении СФ Покупателем");
    	InvoiceName.put(8, "8. Извещение о получении Покупателем подтверждения о получении СФ Оператором ЭДО");
    	InvoiceName.put(9, "9. Уведомление об уточнении СФ Покупателем");
    	InvoiceName.put(10, "10. Извещение о получении уточнения СФ Продавцом");
    }
    

    public String xml(long docid, int count) throws UnsupportedEncodingException{
    	String getxml = "select #param from snt.dfsigns where id = :docid";
    	String getxmlnew = "select #param from snt.dfkorr where id = :docid";
    	String xml = "";
    	int mark = 0;
    	String param = SF.get(count);

    	if(param.equals("UV_D1")||param.equals("UV_D2")){
    		getxmlnew = getxmlnew.replaceAll("#param", param);
    		mark = 1;	
    	}else{
    		getxml = getxml.replaceAll("#param", param);
    	}

    	HashMap pp = new HashMap();
    	pp.put("docid", docid);

    	if(mark == 1){
    		byte[] xmlbyte = (byte[])getNpjt().queryForObject(getxmlnew, pp, byte[].class);
    		xml = new String(xmlbyte, "windows-1251");
    	}else{
    		byte[] xmlbyte = (byte[])getNpjt().queryForObject(getxml, pp, byte[].class);
    		xml = new String(xmlbyte, "windows-1251");
    	}

    	return xml;

    }    
    
}