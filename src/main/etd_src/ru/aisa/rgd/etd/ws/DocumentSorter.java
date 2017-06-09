package ru.aisa.rgd.etd.ws;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.log4j.Logger;

import ru.aisa.edt.DocumentsTableResponseDocument.DocumentsTableResponse.Data.Doc;


public class DocumentSorter {
	private static Logger	log	= Logger.getLogger(DocumentSorter.class);
	
	private String sortDir;
	
	public DocumentSorter(){
		sortDir = "ASC";
	}
	
	public void setDir(String sortDir){
		this.sortDir = sortDir;
	}
	
	public String getDir(){
		return sortDir;
	}
	
	public Doc[] sortByShortContent(Doc[] xml, String dir){
		if(dir != null && !dir.equalsIgnoreCase("ASC"))setDir(dir);
		int arrSize = xml.length;
		if(arrSize>0){

			Arrays.sort(xml, new Comparator<Doc>(){
				
				public int compare(Doc arg0, Doc arg1) {
			        int CompareDir = 1;
			        if(sortDir.equalsIgnoreCase("DESC")) CompareDir = -1;
					int result = arg0.getShort().compareToIgnoreCase(arg1.getShort());
			        if(result>0) result = 1;
			        else if(result<0) result = -1;
			        return result * CompareDir;
			        }//compare
			}//comparator
					);
		}
		return xml;
	}
	
	public Doc[] sortByDocumentNumber(Doc[] xml, String dir){
//System.out.println("Incoming xml:");
//log.debug("(DocumentSorter.java). Incoming xml:");
for(Doc d:xml){
	//System.out.println("------------------");
	//log.debug("------------------");
	//System.out.println(d.xmlText());
	//log.debug(d.xmlText());
}
//System.out.println("");
//log.debug("");
		if(dir != null && !dir.equalsIgnoreCase("ASC"))setDir(dir);
//System.out.println("dir="+dir);
//log.debug("dir="+dir);
		int arrSize = xml.length;
//System.out.println("arrSize="+arrSize);
//log.debug("arrSize="+arrSize);
		if(arrSize>0){

			Arrays.sort(xml, new Comparator<Doc>(){
				
				public int compare(Doc arg0, Doc arg1) {
			        //System.out.println("comparator started");
			        //log.debug("comparator started");
					int CompareDir = 1;
			        if(sortDir.equalsIgnoreCase("DESC")) CompareDir = -1;
					
			        int result = 0;
			        int a1 = 0;
			        int a2 = 0;
			        
			        try{
			        	a1 = Integer.parseInt(arg0.getNumber().trim());
			        }catch(NumberFormatException e){
			        	a1 = 0;
			        }
			        
			        try{
			        	a2 = Integer.parseInt(arg1.getNumber().trim());
			        }catch(NumberFormatException e){
			        	a2 = 0;
			        }
			        
			        if(a1>a2) result = 1;
			        else if(a1==a2) result = 0;
			        else if(a1<a2) result = -1;
			        
			        //System.out.println(":: Sorter :: arg0 = '"+arg0.getNumber().trim()+"', parsed = "+a1+" :: arg1 = '"+arg1.getNumber().trim()+"', parsed = "+a2+" :: Result = " + String.valueOf(result * CompareDir));
			        //log.debug(":: Sorter :: arg0 = '"+arg0.getNumber().trim()+"', parsed = "+a1+" :: arg1 = '"+arg1.getNumber().trim()+"', parsed = "+a2+" :: Result = " + String.valueOf(result * CompareDir));
			        
			        return result * CompareDir;
			        }//compare
			}//comparator
					);
		}
		//System.out.println("Output xml:");
		//log.debug("Output xml:");
		for(Doc d:xml){
			//System.out.println("------------------");
			//log.debug("------------------");
			//System.out.println(d.xmlText());
			//log.debug(d.xmlText());
		}
		return xml;
	}
}
