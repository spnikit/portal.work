package sheff.rgd.ws.Controllers.RTK;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HTMLParser {
	
  private static Logger log = Logger.getLogger(HTMLParser.class);
  
  public static String fillHTML(List<Map<String,String>> list, byte[] file) 
		  throws IOException {
	      Long start =  System.currentTimeMillis();
	      System.out.println("Start " + start);
	      Document doc = Jsoup.parse(new String(file), "UTF-8");
	      boolean flag = false;
	      List<Map<String,String>> listmaps = list;
	      
	      List<String> listForRowspan = new ArrayList<String>();
	      listForRowspan.add("tab4Column1");
	      listForRowspan.add("tab5Column1");
	      listForRowspan.add("tab6Column1");
	      listForRowspan.add("tab12Column1");
	      listForRowspan.add("tab7Column1");
	      listForRowspan.add("tab8Column1");
	      listForRowspan.add("tab13Column1");
	      listForRowspan.add("tab9Column1");
	      listForRowspan.add("tab10Column1");
	      listForRowspan.add("tab11Column1");
	      
	      for(int x  = 0; x < listmaps.size(); x++) {
	    	  Map<String, String> m =listmaps.get(x);
	    	  for(Map.Entry<String, String> entry: m.entrySet()) {
	    		  System.out.println(entry.getKey() + " " + entry.getValue());
	    	  }
	      }	
        boolean flagForRDV = false; 
	      for(int k = 0; k < listmaps.size(); k++) {
	    	  if(listmaps.get(k).get("formname") != null 
	    			  && listmaps.get(k).get("formname").equals("РДВ")) {
	    		  System.out.println("FFFFFFFFFFFFFFFFFF" + listmaps.get(k).get("formname"));
	    		  flagForRDV = true;
	    		  break;
	    	  }
	      }
	     
	      if(flagForRDV) { //только для формы РДВ
	    	  if(doc.getElementsByTag("table").size() > 0) {
		    	  for(int i = 0; i < doc.getElementsByTag("table").size(); i++) {
		    		 int colStr = 0;
		    		 int indexFirst = 0;
		    		 int indexEnd = 0;
		    		 String name =  doc.getElementsByTag("table").get(i).attr("name"); 
		    		 for(int j = 0; j < listmaps.size(); j++) {
		    			 if(listmaps.get(j).get(name) != null) {
		    				 flag = true;
		    				 indexFirst = ++j;
		    				 for(int n = ++j; n < listmaps.size(); n++) {
		    					 if(listmaps.get(n).get(name) != null) {
		    						 indexEnd = n;
		    						 break;
		    					 }
		    					
		    				 }
		    				 break;
		    			 } 
		    		 }
		    		 for(int p = indexFirst; p < indexEnd; p++) {
		    			 if(listmaps.get(p).get("row") != null) {
    						 colStr++;
    					 }
		    		 }
		    		 colStr = colStr/2;
		             System.out.println("Количество строк в таблице " + (colStr));
		    		 if(flag) {
						 Element currentTable = doc.getElementsByTag("table").get(i);
						 Element currentParent = null;
						 Element template = null;
						 String FirstKey = null;
						 for(int w = indexFirst; w < indexEnd; w++) {
			    			 Map<String, String> map = listmaps.get(w);
			    			 Set<String> keyset = map.keySet();
			    			 Object[] mas = keyset.toArray();
			    			 String key = (String)mas[0];
			    			 if(FirstKey == null) {
				    				// System.out.println("Если Firstkey = null");
				    				 FirstKey = key;
				    				 Elements elementsByTag = currentTable.getElementsByAttributeValue("name", key);
					    			 if(elementsByTag.size() > 0) {
						    			template = elementsByTag.get(0).parent().parent();
					    				Element cloneNode = template.clone();
					    				for(int t = 0; t < listForRowspan.size(); t++) {
					    					Elements el = cloneNode.getElementsByAttributeValue("name", listForRowspan.get(t));
					    					if(el.size() > 0) {
					    						el.get(0).attr("rowspan", new String().valueOf(colStr));
					    						break;
					    					}
					    				}
					    				cloneNode.getElementsByAttributeValue("name", key).get(0).text(listmaps.get(w).get(key));					  
					    				if(template.child(0).getElementsByAttribute("rowspan").size() > 0) {
					    					template.child(0).getElementsByAttribute("rowspan").get(0).remove();
					    				}
					    				template.after(cloneNode);
					    			    currentParent = cloneNode;
					    			 } else {
					    				 log.debug("Тег с именем " + key  + " не найден в таблице");
					    				 FirstKey = null;
					    				 continue;
					    			 }
			    			 } else {
			    				 if(FirstKey.equals(key)) { 
				    				 System.out.println("Если ключи совпали");
			    					 Element cloneNode = template.clone();
			    	    			 Elements elementsByTag = cloneNode.getElementsByAttributeValue("name", key);
			    	    			 if(elementsByTag.size() > 0) {
			    	    				elementsByTag.get(0).text(listmaps.get(w).get(key));
			    	    				currentParent.after(cloneNode);	
			    	    				currentParent = cloneNode;
			    	    				
			    	    			 }
			    				 } else {
			    					 System.out.println("Если несовпали");
			    	    			 Elements elementsByTag = currentParent.getElementsByAttributeValue("name", key);
			    	    			 if(elementsByTag.size() > 0) {
			    	    				elementsByTag.get(0).text(listmaps.get(w).get(key));
			    	    			 }
			    				 }
			    			 }
						 } 
						 if(template != null) {
							 template.remove();
						 }
						 System.out.println("*****************************11");
					        for (int v = indexFirst-1; v <= indexEnd; v++) {
					            listmaps.remove(indexFirst-1);
					        }
						 
						 for(int x  = 0; x < listmaps.size(); x++) {
							 Map<String, String> m =listmaps.get(x);
							 for(Map.Entry<String, String> entry: m.entrySet()) {
					        	 System.out.println(entry.getKey() + " " + entry.getValue());
					         }
						 }	
						 flag = false;
		    		 }
		    	  }
		      }
	      } else { // для всех остальных
		      if(doc.getElementsByTag("table").size() > 0) {
		    	  for(int i = 0; i < doc.getElementsByTag("table").size(); i++) {
		    		 int indexFirst = 0;
		    		 int indexEnd = 0;
		    		 String name =  doc.getElementsByTag("table").get(i).attr("name"); 
		    		 for(int j = 0; j < listmaps.size(); j++) {
		    			 if(listmaps.get(j).get(name) != null) {
		    				 flag = true;
		    				 indexFirst = ++j;
		    				 for(int n = ++j; n < listmaps.size(); n++) {
		    					 if(listmaps.get(n).get(name) != null) {
		    						 indexEnd = n;
		    						 break;
		    					 }
		    				 }
		    				 break;
		    			 } 
		    		 }
		  
		    		 if(flag) {
						 Element currentTable = doc.getElementsByTag("table").get(i);
						 Element currentParent = null;
						 Element template = null;
						 String FirstKey = null;
						 for(int w = indexFirst; w < indexEnd; w++) {
			    			 Map<String, String> map = listmaps.get(w);
			    			 Set<String> keyset = map.keySet();
			    			 Object[] mas = keyset.toArray();
			    			 String key = (String)mas[0];
			    			 if(FirstKey == null) {
			    				// System.out.println("Если Firstkey = null");
			    				 FirstKey = key;
			    				 Elements elementsByTag = currentTable.getElementsByAttributeValue("name", key);
				    			 if(elementsByTag.size() > 0) {
					    			template = elementsByTag.get(0).parent().parent();
				    				Element cloneNode = template.clone();
				    				cloneNode.getElementsByAttributeValue("name", key).get(0).text(listmaps.get(w).get(key));
				    			    template.after(cloneNode);
				    				currentParent = cloneNode;
				    			 } else { // тут по идее надо логгировать если тег не найден
				    				 log.debug("Тег с именем " + key + " не найден в таблице");
				    				 FirstKey = null;
				    				 continue;
				    			 }
			    			 } else {
			    				 if(FirstKey.equals(key)) {
				    				// System.out.println("Если ключи совпали");
			    					 Element cloneNode = template.clone();
			    	    			 Elements elementsByTag = cloneNode.getElementsByAttributeValue("name", key);
			    	    			 if(elementsByTag.size() > 0) {
			    	    				elementsByTag.get(0).text(listmaps.get(w).get(key));
			    	    				currentParent.after(cloneNode);
			    	    				currentParent = cloneNode;
			    	    			 }
			    				 } else {
			    					 //System.out.println("Если несовпали");
			    	    			 Elements elementsByTag = currentParent.getElementsByAttributeValue("name", key);
			    	    			 if(elementsByTag.size() > 0) {
			    	    				elementsByTag.get(0).text(listmaps.get(w).get(key));
			    	    			 }
			    				 }
			    			 }
						 } 
						 if(template != null) {
							 template.remove();
						 }
						 System.out.println("*****************************11");
					        for (int v = indexFirst-1; v <= indexEnd; v++) {
					            listmaps.remove(indexFirst-1);
					        }
						 
						 for(int x  = 0; x < listmaps.size(); x++) {
							 Map<String, String> m =listmaps.get(x);
							 for(Map.Entry<String, String> entry: m.entrySet()) {
					        	 System.out.println(entry.getKey() + " " + entry.getValue());
					         }
						 }	
						 flag = false;
		    		 }
		    	  }
		      }
	      }
	      
	      boolean fl = false;
	      String name = "";
	      for(int i = 0; i < listmaps.size(); i++) {
	    	  Map<String, String> map = listmaps.get(i);
	    	  for(int j = 0; j < doc.getElementsByAttribute("name").size(); j++) {
	    		  Element elemWithAttrName  = doc.getElementsByAttribute("name").get(j);
	    		  
	    		  if(map.get(elemWithAttrName.attr("name")) != null) {
	    			  System.out.println("Popal " + elemWithAttrName.attr("name"));
	    			  elemWithAttrName.text(map.get(elemWithAttrName.attr("name")));
	    			  break;
	    		  } 
	    		  if(j == doc.getElementsByAttribute("name").size()-1 ) {
	    			  System.out.println("Nepopal " + elemWithAttrName.attr("name"));
	    			  fl = true;
	    			  name = elemWithAttrName.attr("name");
	    		  }
	    		  
	    	  }
	    	  if(fl) {	
      			  log.debug("Тег с именем " + name
      					  + " не найден");
      			  log.error("Тег с именем " + name
      					  + " не найден");
      			  log.warn("Тег с именем " + name
      					  + " не найден");
      			  fl = false;
    		  } 
	      }

//			for(int i = 0; i < doc.getElementsByAttribute("name").size(); i++) {
//	    		Element elemWithAttrName  = doc.getElementsByAttribute("name").get(i);	
//		    		for(int j = 0; j < listmaps.size(); j++) {
//		    			 Map<String, String> map = listmaps.get(j);
//		    			 if(map.get(elemWithAttrName.attr("name")) != null) {
//		    				 elemWithAttrName.text(map.get(elemWithAttrName.attr("name")));
//		    				 break;
//		    			 } 
//		    		    
//			    		if(j == listmaps.size()-1 
//			    				&& elemWithAttrName.tagName().equals("div")) {	
//			    			log.debug("Тег с именем " + elemWithAttrName.attr("name")
//			    					+ " не найден");
//			    			log.error("Тег с именем " + elemWithAttrName.attr("name")
//			    					+ " не найден");
//			    			log.warn("Тег с именем " + elemWithAttrName.attr("name")
//			    					+ " не найден");
//			    		} 		
//
//	    		    }
//	    	}

//	    FileWriter fileWriter = new FileWriter("/home/user/Рабочий стол/sss/spravka2612 v2.html");
//	    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//	    bufferedWriter.write(doc.toString());
//	    bufferedWriter.close();
	    
	    
//	    Long finish =  System.currentTimeMillis();
//	    System.out.println("finish " + finish);
//	    System.out.println("Result " + (finish - start));
	    
	    return doc.toString();
	 
  }
}
