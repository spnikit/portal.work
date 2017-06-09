package ru.aisa.rgd.HTMLGenerator;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
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
  
  private static boolean contains (List<String> list, String str) {
		for(String l: list) {
			if(l.equals(str)) {
				return true;
			}
		}
		return false;
   }
  
  public static String fillHTML(List<Map<String,String>> list, byte[] file) 
		  throws IOException {
//	      Long start =  System.currentTimeMillis();
//	      System.out.println("Start " + start);
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
	      
	      List<String> listRowspanForVU22 = new ArrayList<String>();
	      listRowspanForVU22.add("tab1Column1");
	      listRowspanForVU22.add("tab2Column1");
	      listRowspanForVU22.add("tab3Column1");
	      listRowspanForVU22.add("tab4Column1");
	      listRowspanForVU22.add("tab5Column1");
	      listRowspanForVU22.add("tab6Column1");
	      listRowspanForVU22.add("tab7Column1");
	      listRowspanForVU22.add("tab8Column1");
	      listRowspanForVU22.add("tab9Column1");
	      
	      List<String> listForVU23 = new ArrayList<String>();
	      listForVU23.add("rw_station_id");
	      listForVU23.add("owner_id");
	      listForVU23.add("wagon_state_id");
	      listForVU23.add("manufacture_id");
	      listForVU23.add("last_major_repair_id");
	      listForVU23.add("last_major_repair_category");
	      listForVU23.add("last_depo_repair_id");
	      listForVU23.add("last_depo_repair_category");
	      
	      List<String> listForKSF = new ArrayList<String>();
	      listForKSF.add("fio_face");
	      listForKSF.add("id_paketa");
	      listForKSF.add("id_ksf");
	      listForKSF.add("id_korKSF_26");
	      listForKSF.add("id_PPS");
	      listForKSF.add("kod_valuta");
	      listForKSF.add("xml_text");
	      listForKSF.add("xml_sign");
	      listForKSF.add("cntPred");
	      listForKSF.add("mark");
	      
//	      for(int x  = 0; x < listmaps.size(); x++) {
//	    	  Map<String, String> m =listmaps.get(x);
//	    	  for(Map.Entry<String, String> entry: m.entrySet()) {
//	    		  System.out.println(entry.getKey() + " " + entry.getValue());
//	    	  }
//	      }	
	      String formname = "";
          boolean flagForRDV = false; 
          boolean flagForVU23 = false; 
          boolean flagForTORG12 = false; 
          boolean flagForVU22 = false; 
          boolean flagForKSF = false; 
          
	      for(int k = 0; k < listmaps.size(); k++) {
	    	  if(listmaps.get(k).get("formname") != null) {  
	    		  formname = listmaps.get(k).get("formname");
	    		  break;
	    	  }
	      }
	      
	      if(formname.equals("РДВ")) {
	    	  flagForRDV = true;
	      }
	      if(formname.equals("ВУ-23_О")) {
	    	  flagForVU23 = true;
	      }
	      if(formname.equals("ТОРГ-12")) {
	    	  flagForTORG12 = true;
	      }
	      if(formname.equals("ВУ-22")) {
	    	  flagForVU22 = true;
	      } 
	      if(formname.equals("Корректировочный счет-фактура")) {
	    	  flagForKSF = true;
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
				    				// System.out.println("Если ключи совпали");
			    					 Element cloneNode = template.clone();
			    	    			 Elements elementsByTag = cloneNode.getElementsByAttributeValue("name", key);
			    	    			 if(elementsByTag.size() > 0) {
			    	    				elementsByTag.get(0).text(listmaps.get(w).get(key));
			    	    				currentParent.after(cloneNode);	
			    	    				currentParent = cloneNode;
			    	    				
			    	    			 }
			    				 } else {
			    					// System.out.println("Если несовпали");
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
						// System.out.println("*****************************11");
					        for (int v = indexFirst-1; v <= indexEnd; v++) {
					            listmaps.remove(indexFirst-1);
					        }
						 
//						 for(int x  = 0; x < listmaps.size(); x++) {
//							 Map<String, String> m =listmaps.get(x);
//							 for(Map.Entry<String, String> entry: m.entrySet()) {
//					        	 System.out.println(entry.getKey() + " " + entry.getValue());
//					         }
//						 }	
						 flag = false;
		    		 }
		    	  }
		      }
	      } else if(flagForVU22){ // для VU-22
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
		    		 if(flag) {
						 Element currentTable = doc.getElementsByTag("table").get(i);
						 Element currentParent = null;
						 Element template = null;
						 String FirstKey = null;
						 String P_15 = null;
						 String P_16 = null;
						 String P_17 = null;
						 String P_18 = null;
						 boolean specFlag = true;
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
				    				if(key.contains("P_15")) {
				    					P_15 = listmaps.get(w).get(key);
				    				} else if(key.contains("P_16")) {
				    					P_16 = listmaps.get(w).get(key);
				    				} else if(key.contains("P_17")) {
				    					P_17 = listmaps.get(w).get(key);
				    				} else if(key.contains("P_18")) {
				    					P_18 = listmaps.get(w).get(key);
				    				}			 
					    			template = elementsByTag.get(0).parent().parent();
				    				Element cloneNode = template.clone();
				    				for(int t = 0; t < listRowspanForVU22.size(); t++) {
				    					Elements el = cloneNode.getElementsByAttributeValue("name", listRowspanForVU22.get(t));
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
				    			 } else { // тут по идее надо логгировать если тег не найден
				    				 log.debug("Тег с именем " + key + " не найден в таблице");
				    				 FirstKey = null;
				    				 continue;
				    			 }
			    			 } else {
			    				 if(FirstKey.equals(key)) {
			    					 specFlag = false;
				    				// System.out.println("Если ключи совпали");	 
			    					 if((P_15.length()==0 && P_16.length() == 0 
			    							 && P_17.length() == 0 && P_18.length() == 0) || P_15.length() != 0) {
			    						 Elements elts = currentParent.getElementsByAttribute("name");
			    						 for(int b = 0; b < elts.size(); b++) {
			    							 String attr = elts.get(b).attr("name");
			    							 if(attr.contains("P_16") || attr.contains("P_17") || attr.contains("P_18")) {
			    								 Set<String> set = new HashSet<String>();
			    			            		 set = elts.get(b).classNames();
			    			            		 set.add("invis");
			    			            		 elts.get(b).classNames(set);			    								 
			    							 }
			    							 if(attr.contains("P_15")) {
			    								 Set<String> set = new HashSet<String>();
			    			            		 set = elts.get(b).classNames();
			    			            		 set.remove("invis");
			    			            		 elts.get(b).classNames(set);	
			    							 }
			    								 
			    						 }
			    					 }
			    					 Element cloneNode = template.clone();
			    	    			 Elements elementsByTag = cloneNode.getElementsByAttributeValue("name", key);
			    	    			 if(elementsByTag.size() > 0) {
			    	    				if(key.contains("P_15")) {
					    					P_15 = listmaps.get(w).get(key);
					    				} else if(key.contains("P_16")) {
					    					P_16 = listmaps.get(w).get(key);
					    				} else if(key.contains("P_17")) {
					    					P_17 = listmaps.get(w).get(key);
					    				} else if(key.contains("P_18")) {
					    					P_18 = listmaps.get(w).get(key);
					    				}	
			    	    				elementsByTag.get(0).text(listmaps.get(w).get(key));
			    	    				currentParent.after(cloneNode);
			    	    				currentParent = cloneNode;
			    	    			 }
			    				 } else {
			    				//	 System.out.println("Если несовпали");
			    	    			 Elements elementsByTag = currentParent.getElementsByAttributeValue("name", key);
			    	    			 if(elementsByTag.size() > 0) {
			    	    				if(key.contains("P_15")) {
					    					P_15 = listmaps.get(w).get(key);
					    				} else if(key.contains("P_16")) {
					    					P_16 = listmaps.get(w).get(key);
					    				} else if(key.contains("P_17")) {
					    					P_17 = listmaps.get(w).get(key);
					    				} else if(key.contains("P_18")) {
					    					P_18 = listmaps.get(w).get(key);
					    				}	
			    	    				//System.out.println("P_15 " + P_15 + "P_16 "+ P_16 +"P_17 "+ P_17 +"P_18 " + P_18);
			    	    				elementsByTag.get(0).text(listmaps.get(w).get(key));
			    	    			 }
			    				 }
			    				 if((w == (indexEnd-1)) && specFlag) {
		    	    					 if((P_15.length()==0 && P_16.length() == 0 
				    							 && P_17.length() == 0 && P_18.length() == 0) || P_15.length() != 0) {				    	
				    						 Elements elts = currentParent.getElementsByAttribute("name");
				    						 for(int b = 0; b < elts.size(); b++) {
				    							 String attr = elts.get(b).attr("name");
				    							 if(attr.contains("P_16") || attr.contains("P_17") || attr.contains("P_18")) {
				    								 Set<String> set = new HashSet<String>();
				    			            		 set = elts.get(b).classNames();
				    			            		 set.add("invis");
				    			            		 elts.get(b).classNames(set);			    								 
				    							 }
				    							 if(attr.contains("P_15")) {
				    								 Set<String> set = new HashSet<String>();
				    			            		 set = elts.get(b).classNames();
				    			            		 set.remove("invis");
				    			            		 elts.get(b).classNames(set);	
				    							 }
				    								 
				    						 }
				    					 }
		    	    				}
			    			 }
						 } 
						 if(template != null) {
							 template.remove();
						 }
						 //System.out.println("*****************************11");
					        for (int v = indexFirst-1; v <= indexEnd; v++) {
					            listmaps.remove(indexFirst-1);
					        }
						 
//						 for(int x  = 0; x < listmaps.size(); x++) {
//							 Map<String, String> m =listmaps.get(x);
//							 for(Map.Entry<String, String> entry: m.entrySet()) {
//					        	 System.out.println(entry.getKey() + " " + entry.getValue());
//					         }
//						 }	
						 flag = false;
		    		 }
		    	  }
		      }
	      } else if(flagForKSF) { // для ksf
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
			    				 System.out.println("Если Firstkey = null");
			    				 FirstKey = key;
			    				 Elements elementsByTag = currentTable.getElementsByAttributeValue("name", key);
				    			 if(elementsByTag.size() > 0) {
				    				System.out.println("> 0");
					    			template = elementsByTag.get(0).parent().parent();
				    				Element cloneNode = template.clone();
				    				cloneNode.getElementsByAttributeValue("name", key).get(0).text(listmaps.get(w).get(key));
				    			    template.after(cloneNode);
				    				currentParent = cloneNode;
				    				if(!key.equals("infPolStr")) {
					    				if(listmaps.get(w).get(key).length() == 0) {
					    					System.out.println("==0 " + key);
					    					System.out.println(currentParent);
					    					//делаем видимым тег с PPP_
					    					Set<String> set = new HashSet<String>();
					    					String str = "PPP_"+key;
					    					Elements elements = currentParent.getElementsByAttributeValue("name", str);
					    					set = elements.get(0).classNames();
					             		    set.remove("invis");
					             		    elements.get(0).classNames(set);
					             		    //Делаем скрытым основной тег
					             		    elements = currentParent.getElementsByAttributeValue("name", key);
					    					set = elements.get(0).classNames();
					             		    set.add("invis");
					             		    elements.get(0).classNames(set);
					    				} 
				    				}
				    			 } else { // тут по идее надо логгировать если тег не найден
				    				 log.debug("Тег с именем " + key + " не найден в таблице");
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
			    	    				if(listmaps.get(w).get(key).length() == 0) {
					    					//делаем видимым тег с PPP_
					    					Set<String> set = new HashSet<String>();
					    					String str = "PPP_"+key;
					    					Elements elements = currentParent.getElementsByAttributeValue("name", str);
					    					set = elements.get(0).classNames();
					             		    set.remove("invis");
					             		    elements.get(0).classNames(set);
					             		    //Делаем скрытым основной тег
					             		    elements = currentParent.getElementsByAttributeValue("name", key);
					    					set = elements.get(0).classNames();
					             		    set.add("invis");
					             		    elements.get(0).classNames(set);
					    			    } 
			    	    			 }
			    				 } else {
			    					 System.out.println("Если несовпали");
			    	    			 Elements elementsByTag = currentParent.getElementsByAttributeValue("name", key);
			    	    			 if(elementsByTag.size() > 0) {
			    	    				elementsByTag.get(0).text(listmaps.get(w).get(key));
			    	    				if(listmaps.get(w).get(key).length() == 0) {
					    					//делаем видимым тег с PPP_
					    					Set<String> set = new HashSet<String>();
					    					String str = "PPP_"+key;
					    					Elements elements = currentParent.getElementsByAttributeValue("name", str);
					    					set = elements.get(0).classNames();
					             		    set.remove("invis");
					             		    elements.get(0).classNames(set);
					             		    //Делаем скрытым основной тег
					             		    elements = currentParent.getElementsByAttributeValue("name", key);
					    					set = elements.get(0).classNames();
					             		    set.add("invis");
					             		    elements.get(0).classNames(set);
					    			    } 
			    	    			 }
			    				 }
			    			 }
						 } 
						 if(template != null) {
							 template.remove();
						 }
						 //System.out.println("*****************************11");
					        for (int v = indexFirst-1; v <= indexEnd; v++) {
					            listmaps.remove(indexFirst-1);
					        }
						 
//						 for(int x  = 0; x < listmaps.size(); x++) {
//							 Map<String, String> m =listmaps.get(x);
//							 for(Map.Entry<String, String> entry: m.entrySet()) {
//					        	 System.out.println(entry.getKey() + " " + entry.getValue());
//					         }
//						 }	
						 flag = false;
		    		 }
		    	  }
		      }
		      listForKSF.clear();
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
						 //System.out.println("*****************************11");
					        for (int v = indexFirst-1; v <= indexEnd; v++) {
					            listmaps.remove(indexFirst-1);
					        }
						 
//						 for(int x  = 0; x < listmaps.size(); x++) {
//							 Map<String, String> m =listmaps.get(x);
//							 for(Map.Entry<String, String> entry: m.entrySet()) {
//					        	 System.out.println(entry.getKey() + " " + entry.getValue());
//					         }
//						 }	
						 flag = false;
		    		 }
		    	  }
		      }
	      }
	      
//	      boolean fl = false;
//	      String name = "";
//	      for(int i = 0; i < listmaps.size(); i++) {
//	    	  Map<String, String> map = listmaps.get(i);
//	    	  for(int j = 0; j < doc.getElementsByAttribute("name").size(); j++) {
//	    		  Element elemWithAttrName  = doc.getElementsByAttribute("name").get(j);
//	    		  
//	    		  if(map.get(elemWithAttrName.attr("name")) != null) {
////	    			  System.out.println("Popal " + elemWithAttrName.attr("name"));
//	    			  elemWithAttrName.text(map.get(elemWithAttrName.attr("name")));
//	    			  break;
//	    		  } 
//	    		  if(j == doc.getElementsByAttribute("name").size()-1 ) {
////	    			  System.out.println("Nepopal " + elemWithAttrName.attr("name"));
//	    			  fl = true;
//	    			  name = elemWithAttrName.attr("name");
//	    		  }
//	    		  
//	    	  }
//	    	  if(fl) {	
//      			  log.debug("Тег с именем " + name
//      					  + " не найден");
//      			  fl = false;
//    		  } 
//	      }
	      
           if(flagForVU23) {
        	   String auto = "";
        	   for(int k = 0; k < listmaps.size(); k++) {
     	    	  if(listmaps.get(k).get("auto") != null) {  
     	    		  auto = listmaps.get(k).get("auto");
     	    		  break;
     	    	  }
     	      }
        	   if(!auto.equals("1")) {
	        	   for(int i = 0; i < doc.getElementsByAttribute("name").size(); i++) {
	        		   Element elemWithAttrName  = doc.getElementsByAttribute("name").get(i);
	        		   Set<String> set = new HashSet<String>();
	        		   elemWithAttrName.classNames();
	        		   for(int t = 0; t < listForVU23.size(); t++) {
	        			   if(elemWithAttrName.attr("name").equals(listForVU23.get(t))) {
	        				   set = elemWithAttrName.classNames();
	        				   set.add("invis");
	        				   elemWithAttrName.classNames(set);
	        			   }
	        		   }
	        		   
	        		   for(int j = 0; j < listmaps.size(); j++) {
	        			   Map<String, String> map = listmaps.get(j);
	        			   if(map.get(elemWithAttrName.attr("name")) != null) {
	        				   elemWithAttrName.text(map.get(elemWithAttrName.attr("name")));
	        				   break;
	        			   } 
	
	        			   if(j == listmaps.size()-1 
	        					   && elemWithAttrName.tagName().equals("div")) {	
	        				   log.debug("Тег с именем " + elemWithAttrName.attr("name")
	        						   + " не найден");
	        			   } 		
	
	        		   }
	        	   }
        	   } else {
        		   for(int i = 0; i < doc.getElementsByAttribute("name").size(); i++) {
            		   Element elemWithAttrName  = doc.getElementsByAttribute("name").get(i);	
            		   for(int j = 0; j < listmaps.size(); j++) {
            			   Map<String, String> map = listmaps.get(j);
            			   if(map.get(elemWithAttrName.attr("name")) != null) {
            				   elemWithAttrName.text(map.get(elemWithAttrName.attr("name")));
            				   break;
            			   } 

            			   if(j == listmaps.size()-1 
            					   && elemWithAttrName.tagName().equals("div")) {	
            				   log.debug("Тег с именем " + elemWithAttrName.attr("name")
            						   + " не найден");
            			   } 		

            		   }
            	   }
        	   }
           } else if(flagForTORG12) { // для торг12
        	   String flag_ASU = "";
        	   String P_5_1 = "";
        	   
        	   for(int k = 0; k < listmaps.size(); k++) {
     	    	  if(listmaps.get(k).get("flag_ASU") != null) {  
     	    		  flag_ASU = listmaps.get(k).get("flag_ASU");
     	    	  }
     	    	  if(listmaps.get(k).get("P_5_1") != null) {  
     	    		 P_5_1 = listmaps.get(k).get("P_5_1");
    	    	  }
     	       }
        	   
        	   if(!flag_ASU.equals("1") || P_5_1.length() == 0) {
        		   Set<String> set = new HashSet<String>();
        		   for(int i = 0; i < doc.getElementsByClass("invisASU").size(); i++) {
            		   Element elWithClassInvisASU = doc.getElementsByClass("invisASU").get(i);
            		   set = elWithClassInvisASU.classNames();
            		   set.remove("invis");
            		   elWithClassInvisASU.classNames(set);
            	   }
        		   for(int i = 0; i < doc.getElementsByClass("visASU").size(); i++) {
            		   Element elWithClassVisASU = doc.getElementsByClass("visASU").get(i);
            		   set = elWithClassVisASU.classNames();
            		   set.add("invis");
            		   elWithClassVisASU.classNames(set);
            	   }
        		   if(!flag_ASU.equals("1")) {
        			   doc.getElementsByAttributeValue("name", "fax-rs").get(0).text("Р/С:");
        			   doc.getElementsByAttributeValue("name", "rs-bik").get(0).text("БИК");
        		   }
        	   }
        	   
        	   for(int i = 0; i < doc.getElementsByAttribute("name").size(); i++) {
        		   Element elemWithAttrName  = doc.getElementsByAttribute("name").get(i);	
        		   for(int j = 0; j < listmaps.size(); j++) {
        			   Map<String, String> map = listmaps.get(j);
        			   if(map.get(elemWithAttrName.attr("name")) != null) {
        				   elemWithAttrName.text(map.get(elemWithAttrName.attr("name")));
        				   break;
        			   } 

        			   if(j == listmaps.size()-1 
        					   && elemWithAttrName.tagName().equals("div")) {	
        				   log.debug("Тег с именем " + elemWithAttrName.attr("name")
        						   + " не найден");
        			   } 		

        		   }
        	   }
    	   } else if(flagForKSF){ //для KSF
    		   List<String> listForEmpty = new ArrayList<String>();
        	   for(int i = 0; i < doc.getElementsByAttribute("name").size(); i++) {
        		   Element elemWithAttrName  = doc.getElementsByAttribute("name").get(i);	
        		   for(int j = 0; j < listmaps.size(); j++) {
        			   Map<String, String> map = listmaps.get(j);        			  
        			   if(map.get(elemWithAttrName.attr("name")) != null) {
        				   if(contains(listForKSF, elemWithAttrName.attr("name"))){
        					   elemWithAttrName.text(map.get(elemWithAttrName.attr("name")));
        					   break;
        				   } else if(map.get(elemWithAttrName.attr("name")).length() == 0) {
        					   String str = "PPP_"+elemWithAttrName.attr("name");
            				   listForEmpty.add(str);
            				   Set<String> set = new HashSet<String>();
            				   set = elemWithAttrName.classNames();
                    		   set.add("invis");
                    		   elemWithAttrName.classNames(set);
                    		   break;
            			   } else {
	        				   elemWithAttrName.text(map.get(elemWithAttrName.attr("name")));
	        				   break;
            			   }
        			   } 

        			   if(j == listmaps.size()-1 
        					   && elemWithAttrName.tagName().equals("div")) {	
        				   log.debug("Тег с именем " + elemWithAttrName.attr("name")
        						   + " не найден");
        			   } 		
        		   }
        	   }
        	   
        	   for(int i = 0; i < doc.getElementsByAttribute("name").size(); i++) {
        		   Element elemWithAttrName  = doc.getElementsByAttribute("name").get(i);	
        		   for(int j = 0; j < listForEmpty.size(); j++) {
        			   Set<String> set = new HashSet<String>();
        			   if(elemWithAttrName.attr("name").equals(listForEmpty.get(j))) {
        				   set = elemWithAttrName.classNames();
                		   set.remove("invis");
                		   elemWithAttrName.classNames(set);
                		   break;
        			   }
        		   } 
	

        		   
        	   }   
           } else { //для всех остальных
        	   for(int i = 0; i < doc.getElementsByAttribute("name").size(); i++) {
        		   Element elemWithAttrName  = doc.getElementsByAttribute("name").get(i);	
        		   for(int j = 0; j < listmaps.size(); j++) {
        			   Map<String, String> map = listmaps.get(j);
        			   if(map.get(elemWithAttrName.attr("name")) != null) {
        				   elemWithAttrName.text(map.get(elemWithAttrName.attr("name")));
        				   break;
        			   } 

        			   if(j == listmaps.size()-1 
        					   && elemWithAttrName.tagName().equals("div")) {	
        				   log.debug("Тег с именем " + elemWithAttrName.attr("name")
        						   + " не найден");
        			   } 		

        		   }
        	   }
           }

//	    FileWriter fileWriter = new FileWriter("/home/user/Рабочий стол/sss/spravka2612 v3.html");
//	    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//	    bufferedWriter.write(doc.toString());
//	    bufferedWriter.close();
	    
	    
//	    Long finish =  System.currentTimeMillis();
//	    System.out.println("finish " + finish);
//	    System.out.println("Result " + (finish - start));
	    
	    return doc.toString();
	 
  }
}
